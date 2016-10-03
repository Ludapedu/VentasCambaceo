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

public class Entregas extends Fragment {

    private final int EDIT_VENTA = 60;
    ListView ListViewEntregas;
    ArrayList<Venta> Lista_De_Entregas = new ArrayList<Venta>();
    private AdaptadorBuscablePedidos entregasadapter;
    Venta EntregaSeleccionada;
    Venta RegistroVenta;

    private OnFragmentInteractionListener mListener;

    public Entregas() {
        // Required empty public constructor
    }


    public static Entregas newInstance(String param1, String param2) {
        Entregas fragment = new Entregas();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entregas, container, false);
        getActivity().setTitle("Entregas");

        ListViewEntregas = (ListView) view.findViewById(R.id.Lista_Entregas);

        ActualizarListView();

        ListViewEntregas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EntregaSeleccionada = entregasadapter.getItem(i);
                Intent intent = new Intent(getActivity(),DetallesVenta.class);
                intent.putExtra("Venta", EntregaSeleccionada);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void ActualizarListView() {

        Lista_De_Entregas.clear();
        BaseDatos db = new BaseDatos(getContext(), "Ventas", null, 1);
        SQLiteDatabase TablaVentas = db.getWritableDatabase();
        Cursor fila = TablaVentas.rawQuery("SELECT IDREG, Cliente, IdCliente, Catalogo, Pagina, Marca, ID, Numero, Costo, Precio, Entregado FROM Ventas WHERE Entregado = 2", null);
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
                Lista_De_Entregas.add(RegistroVenta);
            } while (fila.moveToNext());
        }
        fila.close();
        db.close();
        entregasadapter = new AdaptadorBuscablePedidos(getContext(), Lista_De_Entregas);
        ListViewEntregas.setAdapter(entregasadapter);

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
