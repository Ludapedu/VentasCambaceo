package com.psycho.controlventas.Main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import com.psycho.controlventas.Adaptadores.AdaptadorBuscableCliente;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Cliente;
import com.psycho.controlventas.R;

import java.util.ArrayList;
import java.util.Comparator;

public class Clientes extends Fragment implements SearchView.OnQueryTextListener{

    private final int ADD_NEW_CLIENT = 100;
    private final int EDIT_CLIENT = 50;


    private OnFragmentInteractionListener mListener;
    private ArrayList<Cliente> ListaDeClientes = new ArrayList<>();
    private Cliente RegistroCliente;
    private AdaptadorBuscableCliente clientesadapter;
    private ListView Lista_Clientes;


    private static final Comparator<Cliente> ALPHABETICAL_COMPARATOR = new Comparator<Cliente>() {
        @Override
        public int compare(Cliente a, Cliente b) {
            return a.nombre.compareTo(b.nombre);
        }
    };

    public Clientes() {
        // Required empty public constructor
    }

    public static Clientes newInstance(String param1, String param2) {
        Clientes fragment = new Clientes();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clientes, container, false);

        Lista_Clientes = (ListView)view.findViewById(R.id.Lista_Clientes);

        getActivity().setTitle("Clientes");

        BaseDatos db = new BaseDatos(getContext(), "Clientes", null, 1);
        SQLiteDatabase clientes = db.getWritableDatabase();
        ListaDeClientes.clear();

        Cursor fila = clientes.rawQuery("SELECT IDREG, Nombre, ApellidoPaterno, ApellidoMaterno, Direccion, Telefono FROM Clientes",null);
        if(fila.moveToFirst())
        {
            do {
                RegistroCliente = new Cliente();
                RegistroCliente.setIdCliente(Integer.parseInt(fila.getString(0)));
                RegistroCliente.setNombre(fila.getString(1));
                RegistroCliente.setApellidoPaterno(fila.getString(2));
                RegistroCliente.setApellidoMaterno(fila.getString(3));
                RegistroCliente.setDireccion(fila.getString(4));
                RegistroCliente.setTelefono(fila.getString(5));
                ListaDeClientes.add(RegistroCliente);
            }while (fila.moveToNext());
        }
        fila.close();
        db.close();
        clientesadapter = new AdaptadorBuscableCliente(getContext(), ListaDeClientes);
        Lista_Clientes.setAdapter(clientesadapter);

        Lista_Clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cliente selectedclient = clientesadapter.getItem(i);
                Intent intent = new Intent(getActivity(), DetallesCliente.class);
                intent.putExtra("Cliente", selectedclient);
                startActivityForResult(intent, EDIT_CLIENT);
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
        if (requestCode == ADD_NEW_CLIENT) {
            if (resultCode == Activity.RESULT_OK) {
                Cliente cliente = (Cliente) data.getExtras().getSerializable("Cliente");
                ContentValues datos = new ContentValues();
                datos.put("Nombre", cliente.getNombre());
                datos.put("ApellidoPaterno", cliente.getApellidoPaterno());
                datos.put("ApellidoMaterno", cliente.getApellidoMaterno());
                datos.put("Direccion", cliente.getDireccion());
                datos.put("Telefono", cliente.getTelefono());
                datos.put("IdCliente", cliente.getIdCliente());
                BaseDatos db = new BaseDatos(getContext(), "Clientes", null, 1);
                SQLiteDatabase clientes = db.getWritableDatabase();
                clientes.insert("Clientes",null,datos);

                ListaDeClientes.clear();

                Cursor fila = clientes.rawQuery("SELECT IDREG, Nombre, ApellidoPaterno, ApellidoMaterno, Direccion, Telefono FROM Clientes",null);
                if(fila.moveToFirst())
                {
                    do {
                        RegistroCliente = new Cliente();
                        RegistroCliente.setIdCliente(Integer.parseInt(fila.getString(0)));
                        RegistroCliente.setNombre(fila.getString(1));
                        RegistroCliente.setApellidoPaterno(fila.getString(2));
                        RegistroCliente.setApellidoMaterno(fila.getString(3));
                        RegistroCliente.setDireccion(fila.getString(4));
                        RegistroCliente.setTelefono(fila.getString(5));
                        ListaDeClientes.add(RegistroCliente);
                    }while (fila.moveToNext());
                }
                fila.close();
                db.close();
                clientesadapter = new AdaptadorBuscableCliente(getContext(), ListaDeClientes);
                Lista_Clientes.setAdapter(clientesadapter);
            }
        }
        else if(requestCode == EDIT_CLIENT)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                BaseDatos db = new BaseDatos(getContext(), "Clientes", null, 1);
                SQLiteDatabase clientes = db.getWritableDatabase();

                ListaDeClientes.clear();

                Cursor fila = clientes.rawQuery("SELECT IDREG, Nombre, ApellidoPaterno, ApellidoMaterno, Direccion, Telefono FROM Clientes",null);
                if(fila.moveToFirst())
                {
                    do {
                        RegistroCliente = new Cliente();
                        RegistroCliente.setIdCliente(Integer.parseInt(fila.getString(0)));
                        RegistroCliente.setNombre(fila.getString(1));
                        RegistroCliente.setApellidoPaterno(fila.getString(2));
                        RegistroCliente.setApellidoMaterno(fila.getString(3));
                        RegistroCliente.setDireccion(fila.getString(4));
                        RegistroCliente.setTelefono(fila.getString(5));
                        ListaDeClientes.add(RegistroCliente);
                    }while (fila.moveToNext());
                }
                fila.close();
                db.close();
                clientesadapter = new AdaptadorBuscableCliente(getContext(), ListaDeClientes);
                Lista_Clientes.setAdapter(clientesadapter);
            }
        }
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
}
