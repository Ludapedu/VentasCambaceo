package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.upiicsa.cambaceo.Adaptadores.AdaptadorPagos;
import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Pago;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;

public class Pagos extends Fragment {

    public int montopendiente;
    public int montoabonado;

    private final int AGREGAR_PAGO = 30;
    private final int EDIT_PAGO = 35;
    FloatingActionButton Agregar;
    TextView lblmontopendiente;
    TextView lblmontoabonado;
    TextView txtmontopendiente;
    TextView txtmontoabonado;

    ListView Lista_Pagos;
    AdaptadorPagos adaptadorpagos;
    ArrayList<Pago> ListaPagos = new ArrayList<>();
    Pago RegistroPago;

    Font font = new Font();

    private OnFragmentInteractionListener mListener;

    public Pagos() {
    }

    public static Pagos newInstance(String param1, String param2) {
        Pagos fragment = new Pagos();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.pagos, container, false);
        getActivity().setTitle("Pagos");


        Agregar = (FloatingActionButton) view.findViewById(R.id.btn_Pagos_Agregar);
        Lista_Pagos = (ListView) view.findViewById(R.id.Lista_Pagos);
        lblmontopendiente = (TextView) view.findViewById(R.id.pagos_lblmontopendiente);
        lblmontoabonado = (TextView) view.findViewById(R.id.pagos_lblmontoabonado);
        txtmontopendiente = (TextView) view.findViewById(R.id.pagos_montopendiente);
        txtmontoabonado = (TextView) view.findViewById(R.id.pagos_montoabonado);

        lblmontopendiente.setTypeface(font.setAsset(getContext()));
        lblmontoabonado.setTypeface(font.setAsset(getContext()));
        txtmontopendiente.setTypeface(font.setAsset(getContext()));
        txtmontoabonado.setTypeface(font.setAsset(getContext()));


        ActualizarListView();

        Lista_Pagos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pago pagoselected = adaptadorpagos.getItem(position);
                Intent editarpago = new Intent(getActivity(), AgregarPago.class);
                editarpago.putExtra("Pago", pagoselected);
                editarpago.putExtra("Visible", true);
                startActivityForResult(editarpago, EDIT_PAGO);
            }
        });

        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agregarpago = new Intent(getActivity(), AgregarPago.class);
                agregarpago.putExtra("Visible", false);
                startActivityForResult(agregarpago, AGREGAR_PAGO);
            }
        });

        return view;
    }

    private void ActualizarListView() {
        montopendiente = 0;
        montoabonado = 0;
        ListaPagos.clear();
        BaseDatos BDPagos = new BaseDatos(getContext(), "Pagos", null, 1);
        SQLiteDatabase TablaPagos = BDPagos.getReadableDatabase();
        Cursor pagos = TablaPagos.rawQuery("SELECT IDREG, Cliente, IdCliente, Fecha, Monto, Dia, Mes, Anio FROM Pagos ORDER BY Fecha", null);
        if (pagos.moveToFirst()) {
            do {
                RegistroPago = new Pago();
                RegistroPago.setIDREG(pagos.getInt(0));
                RegistroPago.setCliente(pagos.getString(1));
                RegistroPago.setIdCliente(pagos.getInt(2));
                RegistroPago.setFechaPago(pagos.getString(3));
                RegistroPago.setMonto(pagos.getInt(4));
                RegistroPago.setDia(pagos.getInt(5));
                RegistroPago.setMes(pagos.getInt(6));
                RegistroPago.setAnio(pagos.getInt(7));
                montoabonado += pagos.getInt(4);
                ListaPagos.add(RegistroPago);
            } while (pagos.moveToNext());
        }
        pagos.close();
        adaptadorpagos = new AdaptadorPagos(getContext(), ListaPagos);
        Lista_Pagos.setAdapter(adaptadorpagos);
        BDPagos.close();

        BaseDatos BDVentas = new BaseDatos(getContext(), "Ventas", null, 1);
        SQLiteDatabase TablaVentas = BDVentas.getReadableDatabase();
        Cursor ventas = TablaVentas.rawQuery("SELECT Precio FROM Ventas", null);
        if (ventas.moveToFirst()) {
            do {
                montopendiente += ventas.getInt(0);
            } while (ventas.moveToNext());
        }
        ventas.close();
        BDVentas.close();

        montopendiente = montopendiente - montoabonado;

        txtmontopendiente.setText("" + montopendiente);
        txtmontoabonado.setText("" + montoabonado);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AGREGAR_PAGO) {
            if (resultCode == Activity.RESULT_OK) {
                ActualizarListView();
            }
        }
        if (requestCode == EDIT_PAGO) {
            if (resultCode == Activity.RESULT_OK) {
                ActualizarListView();
            }
        }
    }
}
