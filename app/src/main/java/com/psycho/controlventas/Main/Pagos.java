package com.psycho.controlventas.Main;

import android.content.ContentValues;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.psycho.controlventas.Adaptadores.AdaptadorSpinnerCliente;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Cliente;
import com.psycho.controlventas.Modelos.Pago;
import com.psycho.controlventas.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Pagos extends Fragment {

    Calendar cal;
    TextView lbl_Pagos_Cliente;
    EditText txt_Pagos_Monto;
    DatePicker DatePicker_Pagos_Fecha;
    Spinner SpinnerCliente;
    FloatingActionButton Agregar;
    AdaptadorSpinnerCliente adaptadorclientes;
    Cliente RegistroCliente;
    ArrayList<Cliente> Lista_De_Clientes = new ArrayList<>();
    Pago pago;

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

        lbl_Pagos_Cliente = (TextView) view.findViewById(R.id.lbl_Cliente_Pagos);
        txt_Pagos_Monto = (EditText) view.findViewById(R.id.txt_Pagos_Monto);
        DatePicker_Pagos_Fecha = (DatePicker) view.findViewById(R.id.Pagos_Picker);
        SpinnerCliente = (Spinner) view.findViewById(R.id.Pagos_Spinner_Clientes);
        Agregar = (FloatingActionButton)view.findViewById(R.id.btn_Pagos_Agregar);

        ActualizarSpinnerClientes();

        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int Dia = DatePicker_Pagos_Fecha.getDayOfMonth();
                int Mes = DatePicker_Pagos_Fecha.getMonth() + 1;
                int ano = DatePicker_Pagos_Fecha.getYear();
                if(txt_Pagos_Monto.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(),"Falta espcificar el monto del pago", Toast.LENGTH_SHORT).show();
                    return;
                }


                Cliente c = (Cliente) SpinnerCliente.getSelectedItem();
                if(c == null) {
                    Toast.makeText(getContext(), "Se requiere agregar primero a un cliente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), AgregarCliente.class);
                    startActivity(intent);
                    return;
                }
                ContentValues datos = new ContentValues();
                datos.put("Cliente", c.getNombre());
                datos.put("IdCliente", c.getIdCliente());
                datos.put("Fecha", "" + Dia + "-" + Mes + "-" + ano);
                datos.put("Monto", Integer.parseInt(txt_Pagos_Monto.getText().toString()));
                BaseDatos db = new BaseDatos(getContext(), "Pagos", null, 1);
                SQLiteDatabase pagos = db.getWritableDatabase();
                pagos.insert("Pagos", null, datos);
                db.close();
                Toast.makeText(getContext(),"Pago agregado correctamente", Toast.LENGTH_SHORT).show();
                cal = Calendar.getInstance();
                LimpiarControles();
            }
        });

        return view;
    }

    private void LimpiarControles()
    {
        txt_Pagos_Monto.setText("");
    }

    private void ActualizarSpinnerClientes() {

        BaseDatos dbClientes = new BaseDatos(getContext(), "Clientes", null, 1);
        SQLiteDatabase clientes = dbClientes.getWritableDatabase();
        Lista_De_Clientes.clear();

        Cursor filacliente = clientes.rawQuery("SELECT IDREG, Nombre, ApellidoPaterno, ApellidoMaterno, Direccion, Telefono FROM Clientes", null);
        if (filacliente.moveToFirst()) {
            do {
                RegistroCliente = new Cliente();
                RegistroCliente.setIdCliente(filacliente.getInt(0));
                RegistroCliente.setNombre(filacliente.getString(1));
                RegistroCliente.setApellidoPaterno(filacliente.getString(2));
                RegistroCliente.setApellidoMaterno(filacliente.getString(3));
                RegistroCliente.setDireccion(filacliente.getString(4));
                RegistroCliente.setTelefono(filacliente.getString(5));
                Lista_De_Clientes.add(RegistroCliente);
            } while (filacliente.moveToNext());
        }
        filacliente.close();
        dbClientes.close();

        adaptadorclientes = new AdaptadorSpinnerCliente(getContext(), Lista_De_Clientes);
        SpinnerCliente.setAdapter(adaptadorclientes);
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
}
