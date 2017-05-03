package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.upiicsa.cambaceo.Adaptadores.AdaptadorBuscablePedidos;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Venta;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;


public class Pedidos extends Fragment implements SearchView.OnQueryTextListener{

    private final int EDIT_VENTA = 60;

    ListView ListView_Pedidos;
    ArrayList<Venta> Lista_De_Pedidos = new ArrayList<Venta>();
    Venta RegistroVenta;
    Venta VentaSeleccionada;
    private AdaptadorBuscablePedidos pedidosadapter;

    private OnFragmentInteractionListener mListener;

    public Pedidos() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pedidos, container, false);
        getActivity().setTitle("Pedidos");



        ListView_Pedidos = (ListView) view.findViewById(R.id.Lista_Pedidos);
        ActualizarListView();


        ListView_Pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VentaSeleccionada = pedidosadapter.getItem(i);

                Intent intent = new Intent(getActivity(),DetallesVenta.class);
                intent.putExtra("Venta", VentaSeleccionada);
                startActivityForResult(intent, EDIT_VENTA);
            }
        });



        return view;
    }

    private void ActualizarListView() {

        Lista_De_Pedidos.clear();
        BaseDatos db = new BaseDatos(getContext(), "Ventas", null, 1);
        SQLiteDatabase TablaVentas = db.getWritableDatabase();
        Cursor fila = TablaVentas.rawQuery("SELECT IDREG, Cliente, IdCliente, Catalogo, Pagina, Marca, ID, Numero, Costo, Precio, Entregado FROM Ventas WHERE Entregado = 0", null);
        if (fila.moveToFirst()) {
            do {
                RegistroVenta = new Venta();
                RegistroVenta.setIDREG(fila.getInt(0));
                RegistroVenta.setCliente(fila.getString(1));
                RegistroVenta.setIdCliente(fila.getInt(2));
                RegistroVenta.setCatalogo(fila.getString(3));
                RegistroVenta.setPagina(fila.getInt(4));
                RegistroVenta.setMarca(fila.getString(5));
                RegistroVenta.setID(fila.getInt(6));
                RegistroVenta.setNumero(fila.getFloat(7));
                RegistroVenta.setCosto(fila.getInt(8));
                RegistroVenta.setPrecio(fila.getInt(9));
                RegistroVenta.setEntregado(fila.getInt(10));
                Lista_De_Pedidos.add(RegistroVenta);
            } while (fila.moveToNext());
        }
        fila.close();
        db.close();

        pedidosadapter = new AdaptadorBuscablePedidos(getContext(), Lista_De_Pedidos);
        ListView_Pedidos.setAdapter(pedidosadapter);


    }




    // TODO: Rename method, update argument and hook method into UI event
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
        pedidosadapter.getFilter().filter(newText);
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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
}
