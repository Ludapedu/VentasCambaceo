package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.upiicsa.cambaceo.Adaptadores.AdaptadorBuscableCliente;
import com.upiicsa.cambaceo.AsynkTask.getClientes;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Cliente;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class Clientes extends Fragment implements SearchView.OnQueryTextListener{

    private final int ADD_NEW_CLIENT = 100;
    private final int EDIT_CLIENT = 50;
    private final int VIEW_RESUME = 80;

    private getClientes obtenerClientes;


    private OnFragmentInteractionListener mListener;
    private ArrayList<Cliente> ListaDeClientes = new ArrayList<>();
    private Cliente RegistroCliente;
    private AdaptadorBuscableCliente clientesadapter;
    private ListView Lista_Clientes;
    BroadcastReceiver receiverClientes;
    IntentFilter filtroCLientes = new IntentFilter();


    private static final Comparator<Cliente> ALPHABETICAL_COMPARATOR = new Comparator<Cliente>() {
        @Override
        public int compare(Cliente a, Cliente b) {
            return a.nombre.compareTo(b.nombre);
        }
    };

    public Clientes() {
        // Required empty public constructor
    }

    public static Clientes newInstance() {
        Clientes fragment = new Clientes();
        return fragment;
    }

    private void BroadCastReceiverClientes() {
        filtroCLientes.addAction("ListaClientes");
        filtroCLientes.addAction("EliminarCliente");
        filtroCLientes.addAction("EditarCliente");

        receiverClientes = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ListaClientes")) {
                    ListaDeClientes.clear();
                    ListaDeClientes = (ArrayList<Cliente>) intent.getExtras().get("ListaDeClientes");
                    if (getActivity() != null) {
                        clientesadapter = new AdaptadorBuscableCliente(getActivity(), ListaDeClientes);
                        Lista_Clientes.setAdapter(clientesadapter);
                    }

                }
                if (intent.getAction().equals("EliminarCliente")) {
                    Toast.makeText(getActivity(),"Cliente Eliminado",Toast.LENGTH_SHORT).show();
                    ActualizarListView();
                }
                if (intent.getAction().equals("EditarCliente")) {
                    ActualizarListView();
                }
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            getActivity().registerReceiver(receiverClientes, filtroCLientes);
            obtenerClientes = new getClientes(getActivity());
            obtenerClientes.execute();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clientes, container, false);

        Lista_Clientes = (ListView)view.findViewById(R.id.Lista_Clientes);

        getActivity().setTitle("Clientes");

        BroadCastReceiverClientes();
        getActivity().registerReceiver(receiverClientes, filtroCLientes);

        obtenerClientes = new getClientes(getActivity());
        obtenerClientes.execute();

        Lista_Clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cliente selectedclient = clientesadapter.getItem(i);
                Intent intent = new Intent(getActivity(), Detalle_Ventas_Cliente.class);
                intent.putExtra("Cliente", selectedclient);
                startActivityForResult(intent, VIEW_RESUME);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.agregar_cliente);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AgregarCliente.class);
                startActivityForResult(i,ADD_NEW_CLIENT);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == VIEW_RESUME)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ActualizarListView();
            }
        }
        if(requestCode == ADD_NEW_CLIENT)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ActualizarListView();
            }
        }
        if(requestCode == EDIT_CLIENT)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ActualizarListView();
            }
        }
    }

    private void ActualizarListView()
    {
        obtenerClientes = new getClientes(getActivity());
        obtenerClientes.execute();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        SearchView searchView = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());

        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        clientesadapter.getFilter().filter(newText);
        return false;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            getActivity().unregisterReceiver(receiverClientes);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
