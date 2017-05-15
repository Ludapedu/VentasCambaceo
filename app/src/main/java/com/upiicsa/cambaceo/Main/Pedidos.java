package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.upiicsa.cambaceo.Adaptadores.AdaptadorBuscableVentas;
import com.upiicsa.cambaceo.AsynkTask.getVentas;
import com.upiicsa.cambaceo.Modelos.Venta;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;


public class Pedidos extends Fragment implements SearchView.OnQueryTextListener{

    private final int EDIT_VENTA = 60;

    ListView ListView_Ventas;
    ArrayList<Venta> listaVentas= new ArrayList<Venta>();
    Venta VentaSeleccionada;
    private AdaptadorBuscableVentas ventasAdapter;
    private OnFragmentInteractionListener mListener;

    private BroadcastReceiver receiverVentas;
    private IntentFilter filtroVentas = new IntentFilter();
    private getVentas obtenerVentas;

    public Pedidos() {
    }

    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            getActivity().registerReceiver(receiverVentas, filtroVentas);
            obtenerVentas = new getVentas(getActivity(), true);
            obtenerVentas.execute();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_VENTA)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ActualizarListView();
            }
        }
    }

    public static Pedidos newInstance(String param1, String param2, Context context) {
        Pedidos fragment = new Pedidos();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pedidos, container, false);
        BroadCastReceiverVentas();
        getActivity().registerReceiver(receiverVentas, filtroVentas);

        obtenerVentas = new getVentas(getActivity(), true);
        obtenerVentas.execute();    getActivity().setTitle("Ventas");



        ListView_Ventas = (ListView) view.findViewById(R.id.Lista_Pedidos);
        ActualizarListView();

        ListView_Ventas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VentaSeleccionada = ventasAdapter.getItem(i);

                Intent intent = new Intent(getActivity(),DetallesVenta.class);
                intent.putExtra("Venta", VentaSeleccionada);
                startActivityForResult(intent, EDIT_VENTA);
            }
        });

        return view;
    }
    private void BroadCastReceiverVentas() {
        filtroVentas.addAction("ListaVentas");

        receiverVentas = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ListaVentas")) {
                    listaVentas.clear();
                    listaVentas = (ArrayList<Venta>) intent.getExtras().get("ListaDeVentas");
                    if (getActivity() != null) {
                        ventasAdapter = new AdaptadorBuscableVentas(getActivity(), listaVentas);
                        ListView_Ventas.setAdapter(ventasAdapter);
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
    private void ActualizarListView() {
        obtenerVentas = new getVentas(getActivity(), true);
        obtenerVentas.execute();
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public boolean onQueryTextChange(String newText) {
        ventasAdapter.getFilter().filter(newText);
        return false;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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
    public void onStop() {
        super.onStop();
        try {
            getActivity().unregisterReceiver(receiverVentas);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
