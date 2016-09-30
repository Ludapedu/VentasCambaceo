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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.psycho.controlventas.Adaptadores.AdaptadorBuscablePedidos;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Venta;
import com.psycho.controlventas.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Cambios.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cambios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cambios extends Fragment {
    private final int EDIT_VENTA = 60;
    ListView ListViewCambios;
    ArrayList<Venta> Lista_De_Cambios = new ArrayList<Venta>();
    private AdaptadorBuscablePedidos cambiosadapter;
    Venta CambioSeleccionado;
    Venta RegistroVenta;



    private OnFragmentInteractionListener mListener;

    public Cambios() {
    }

    public static Cambios newInstance(String param1, String param2) {
        Cambios fragment = new Cambios();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cambios, container, false);
        getActivity().setTitle("Cambios");

        ListViewCambios = (ListView) view.findViewById(R.id.Lista_Cambios);

        ActualizarListView();

        ListViewCambios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CambioSeleccionado = cambiosadapter.getItem(i);
                Intent intent = new Intent(getActivity(),DetallesVenta.class);
                intent.putExtra("Venta", CambioSeleccionado);
                startActivityForResult(intent, EDIT_VENTA);
            }
        });

        return view;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void ActualizarListView() {

        Lista_De_Cambios.clear();
        BaseDatos db = new BaseDatos(getContext(), "Ventas", null, 1);
        SQLiteDatabase TablaVentas = db.getWritableDatabase();
        Cursor fila = TablaVentas.rawQuery("SELECT IDREG, Cliente, IdCliente, Catalogo, Pagina, Marca, ID, Numero, Costo, Precio, Entregado FROM Ventas WHERE Entregado = 3", null);
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
                Lista_De_Cambios.add(RegistroVenta);
            } while (fila.moveToNext());
        }
        fila.close();
        db.close();
        cambiosadapter = new AdaptadorBuscablePedidos(getContext(), Lista_De_Cambios);
        ListViewCambios.setAdapter(cambiosadapter);

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
