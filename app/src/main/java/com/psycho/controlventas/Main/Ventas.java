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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.psycho.controlventas.Adaptadores.AdaptadorSpinnerCatalogo;
import com.psycho.controlventas.Adaptadores.AdaptadorSpinnerCliente;
import com.psycho.controlventas.Assets.Font;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Catalogo;
import com.psycho.controlventas.Modelos.Cliente;
import com.psycho.controlventas.R;

import java.util.ArrayList;


public class Ventas extends Fragment {
    Spinner spinner_Venta_Catalogos;
    Spinner spinner_Venta_Clientes;
    FloatingActionButton aceptar;
    FloatingActionButton cancelar;
    EditText txt_Ventas_Pagina;
    EditText txt_Ventas_Numero;
    AutoCompleteTextView txt_Ventas_Marca;
    EditText txt_Ventas_ID;
    EditText txt_Ventas_Costo;
    EditText txt_Ventas_Precio;
    TextView lblCliente;
    TextView lblCatalogo;
    private Cliente RegistroCliente;
    ArrayList<Cliente> ListaDeClientes = new ArrayList<Cliente>();
    private ArrayList<String> ListaMarcas = new ArrayList<String>();
    Font font = new Font();


    private OnFragmentInteractionListener mListener;

    public Ventas() {

    }

    public static Ventas newInstance() {
        Ventas fragment = new Ventas();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ventas, container, false);
        getActivity().setTitle("Ventas");

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        spinner_Venta_Catalogos = (Spinner) view.findViewById(R.id.spinner_Ventas_Catalogos);
        spinner_Venta_Clientes = (Spinner) view.findViewById(R.id.spinner_Ventas_Clientes);
        aceptar = (FloatingActionButton) view.findViewById(R.id.btn_Ventas_Aceptar);
        cancelar = (FloatingActionButton) view.findViewById(R.id.btn_Ventas_Cancelar);
        txt_Ventas_Pagina = (EditText) view.findViewById(R.id.txt_Ventas_Pagina);
        txt_Ventas_Numero = (EditText) view.findViewById(R.id.txt_Ventas_Numero);
        txt_Ventas_Marca = (AutoCompleteTextView) view.findViewById(R.id.txt_Ventas_Marca);
        txt_Ventas_ID = (EditText) view.findViewById(R.id.txt_Ventas_ID);
        txt_Ventas_Costo = (EditText) view.findViewById(R.id.txt_Ventas_Costo);
        txt_Ventas_Precio = (EditText) view.findViewById(R.id.txt_Ventas_Precio);
        lblCliente = (TextView) view.findViewById(R.id.lbl_Ventas_Cliente);
        lblCatalogo = (TextView) view.findViewById(R.id.lbl_Ventas_Catalogo);

        txt_Ventas_Pagina.setTypeface(font.setAsset(getContext()));
        txt_Ventas_Numero.setTypeface(font.setAsset(getContext()));
        txt_Ventas_Marca.setTypeface(font.setAsset(getContext()));
        txt_Ventas_ID.setTypeface(font.setAsset(getContext()));
        txt_Ventas_Costo.setTypeface(font.setAsset(getContext()));
        txt_Ventas_Precio.setTypeface(font.setAsset(getContext()));
        lblCliente.setTypeface(font.setAsset(getContext()));
        lblCatalogo.setTypeface(font.setAsset(getContext()));

        ArrayList<Catalogo> catalogos = new ArrayList<Catalogo>();
        catalogos.add(new Catalogo("Caballeros"));
        catalogos.add(new Catalogo("Vestir Dama"));
        catalogos.add(new Catalogo("Botas Dama"));
        catalogos.add(new Catalogo("Confort"));
        catalogos.add(new Catalogo("Infantiles"));
        catalogos.add(new Catalogo("Importados"));
        catalogos.add(new Catalogo("Ropa Caballeros"));
        catalogos.add(new Catalogo("Ropa Ninos"));

        spinner_Venta_Catalogos.setAdapter(new AdaptadorSpinnerCatalogo(getContext(), catalogos));

        BaseDatos db = new BaseDatos(getContext(), "Clientes", null, 1);
        SQLiteDatabase clientes = db.getWritableDatabase();
        ListaDeClientes.clear();

        Cursor fila = clientes.rawQuery("SELECT IDREG, Nombre, ApellidoPaterno, ApellidoMaterno, Direccion, Telefono FROM Clientes", null);
        if (fila.moveToFirst()) {
            do {
                RegistroCliente = new Cliente();
                RegistroCliente.setIdCliente(Integer.parseInt(fila.getString(0)));
                RegistroCliente.setNombre(fila.getString(1));
                RegistroCliente.setApellidoPaterno(fila.getString(2));
                RegistroCliente.setApellidoMaterno(fila.getString(3));
                RegistroCliente.setDireccion(fila.getString(4));
                RegistroCliente.setTelefono(fila.getString(5));
                ListaDeClientes.add(RegistroCliente);
            } while (fila.moveToNext());
        }
        fila.close();
        db.close();
        spinner_Venta_Clientes.setAdapter(new AdaptadorSpinnerCliente(getContext(), ListaDeClientes));

        if (ListaDeClientes.size() == 0) {
            Toast.makeText(getContext(), "Se requiere agregar primero a un cliente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AgregarCliente.class);
            startActivity(intent);
        }

        CargarMarcas();
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LimpiarCampos();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txt_Ventas_ID.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "El ID no puede ser vacío", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txt_Ventas_Pagina.getText().toString().matches("")) {
                    txt_Ventas_Pagina.setText("0");
                }
                if (txt_Ventas_Numero.getText().toString().matches("")) {
                    txt_Ventas_Numero.setText("0");
                }
                if (txt_Ventas_Costo.getText().toString().matches("")) {
                    txt_Ventas_Costo.setText("0");
                }
                if (txt_Ventas_Precio.getText().toString().matches("")) {
                    txt_Ventas_Precio.setText("0");
                }
                String Marca = txt_Ventas_Marca.getText().toString();
                if (!ListaMarcas.contains(Marca)) {
                    BaseDatos BDMarcas = new BaseDatos(getContext(), "Marcas", null, 1);
                    SQLiteDatabase Marcas = BDMarcas.getWritableDatabase();
                    Marcas.execSQL("INSERT INTO Marcas(Marca) VALUES(" + "'" + Marca + "'" + ")");
                    BDMarcas.close();
                }

                Cliente c = (Cliente) spinner_Venta_Clientes.getSelectedItem();
                if (c == null) {
                    Toast.makeText(getContext(), "Se requiere agregar primero a un cliente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), AgregarCliente.class);
                    startActivity(intent);
                    return;
                }
                Catalogo cat = (Catalogo) spinner_Venta_Catalogos.getSelectedItem();
                ContentValues datos = new ContentValues();
                datos.put("Cliente", c.getNombre());
                datos.put("IdCliente", c.getIdCliente());
                datos.put("Catalogo", cat.getNombre());
                datos.put("Pagina", Integer.parseInt(txt_Ventas_Pagina.getText().toString()));
                datos.put("Marca", txt_Ventas_Marca.getText().toString());
                datos.put("ID", Integer.parseInt(txt_Ventas_ID.getText().toString()));
                datos.put("Numero", Float.parseFloat(txt_Ventas_Numero.getText().toString()));
                datos.put("Costo", Integer.parseInt(txt_Ventas_Costo.getText().toString()));
                datos.put("Precio", Integer.parseInt(txt_Ventas_Precio.getText().toString()));
                datos.put("Entregado", 0);
                BaseDatos db = new BaseDatos(getContext(), "Ventas", null, 1);
                SQLiteDatabase ventas = db.getWritableDatabase();
                ventas.insert("Ventas", null, datos);
                db.close();
                LimpiarCampos();
                CargarMarcas();
                Toast.makeText(getContext(), "Venta agregada correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    protected void CargarMarcas() {
        BaseDatos BDMarcas = new BaseDatos(getContext(), "Marcas", null, 1);
        SQLiteDatabase Marcas = BDMarcas.getWritableDatabase();
        Cursor filaMarcas = Marcas.rawQuery("SELECT Marca FROM Marcas", null);
        ListaMarcas.clear();
        if (filaMarcas.moveToFirst()) {
            do {
                ListaMarcas.add(filaMarcas.getString(0));
            } while (filaMarcas.moveToNext());
        }
        filaMarcas.close();
        BDMarcas.close();

        ArrayAdapter<String> adaptadorMarcas = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ListaMarcas);

        txt_Ventas_Marca.setAdapter(adaptadorMarcas);
        txt_Ventas_Marca.setThreshold(1);
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

    public void LimpiarCampos() {
        txt_Ventas_Pagina.setText("");
        txt_Ventas_Numero.setText("");
        txt_Ventas_Marca.setText("");
        txt_Ventas_ID.setText("");
        txt_Ventas_Costo.setText("");
        txt_Ventas_Precio.setText("");
        spinner_Venta_Clientes.setSelection(0);
        spinner_Venta_Catalogos.setSelection(0);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
