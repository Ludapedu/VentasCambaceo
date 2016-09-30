package com.psycho.controlventas.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.psycho.controlventas.Adaptadores.AdaptadorBuscablePedidos;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Venta;
import com.psycho.controlventas.R;

import java.util.ArrayList;

public class Compras extends Fragment{

    private final int EDIT_VENTA = 60;
    ListView ListViewCompras;
    ArrayList<Venta> Lista_De_Compras = new ArrayList<Venta>();
    private AdaptadorBuscablePedidos comprasadapter;
    Venta CompraSeleccionada;
    Venta RegistroVenta;


    private OnFragmentInteractionListener mListener;

    public Compras() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.compras, container, false);
        getActivity().setTitle("Compras");
        ListViewCompras = (ListView) view.findViewById(R.id.Lista_Compras);

        ActualizarListView();

        ListViewCompras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CompraSeleccionada = comprasadapter.getItem(i);
                Intent intent = new Intent(getActivity(),DetallesVenta.class);
                intent.putExtra("Venta", CompraSeleccionada);
                startActivityForResult(intent, EDIT_VENTA);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void ActualizarListView() {

        Lista_De_Compras.clear();
        BaseDatos db = new BaseDatos(getContext(), "Ventas", null, 1);
        SQLiteDatabase TablaVentas = db.getWritableDatabase();
        Cursor fila = TablaVentas.rawQuery("SELECT IDREG, Cliente, IdCliente, Catalogo, Pagina, Marca, ID, Numero, Costo, Precio, Entregado FROM Ventas WHERE Entregado = 1", null);
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
                Lista_De_Compras.add(RegistroVenta);
            } while (fila.moveToNext());
        }
        fila.close();
        db.close();
        comprasadapter = new AdaptadorBuscablePedidos(getContext(), Lista_De_Compras);
        ListViewCompras.setAdapter(comprasadapter);

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


}
