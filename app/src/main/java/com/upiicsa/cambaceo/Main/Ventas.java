package com.upiicsa.cambaceo.Main;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.upiicsa.cambaceo.Adaptadores.AdaptadorBuscableCliente;
import com.upiicsa.cambaceo.Adaptadores.AdaptadorSpinnerCatalogo;
import com.upiicsa.cambaceo.Adaptadores.AdaptadorSpinnerCliente;
import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.AsynkTask.AltaMarca;
import com.upiicsa.cambaceo.AsynkTask.AltaVenta;
import com.upiicsa.cambaceo.AsynkTask.getCatalogos;
import com.upiicsa.cambaceo.AsynkTask.getClientes;
import com.upiicsa.cambaceo.AsynkTask.getMarcas;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Catalogo;
import com.upiicsa.cambaceo.Modelos.Cliente;
import com.upiicsa.cambaceo.Modelos.Marca;
import com.upiicsa.cambaceo.R;

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
    ArrayList<Cliente> ListaDeClientes = new ArrayList<Cliente>();
    private ArrayList<String> ListaMarcas = new ArrayList<>();
    Font font = new Font();
    private BroadcastReceiver receiverCatalogos, receiverClientes, receiverMarcas, receiverError;
    private IntentFilter filtroCatalogos = new IntentFilter();
    private IntentFilter filtroCLientes = new IntentFilter();
    private IntentFilter filtroMarcas = new IntentFilter();
    private IntentFilter filtroError = new IntentFilter();
    private getCatalogos obtenerCatalogos;
    private getClientes obtenerClientes;
    private getMarcas obtenerMarcas;
    ArrayList<Catalogo> catalogos = new ArrayList<Catalogo>();
    AdaptadorSpinnerCatalogo catalogosAdapter;
    AdaptadorSpinnerCliente clientesAdapter;


    private OnFragmentInteractionListener mListener;

    public static Ventas newInstance() {
        Ventas fragment = new Ventas();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getActivity().registerReceiver(receiverCatalogos, filtroCatalogos);
            getActivity().registerReceiver(receiverClientes, filtroCLientes);
            getActivity().registerReceiver(receiverMarcas, filtroMarcas);
            getActivity().registerReceiver(receiverError, filtroError);
            obtenerCatalogos = new getCatalogos(getActivity(), false);
            obtenerCatalogos.execute();
            obtenerClientes = new getClientes(getActivity(), false);
            obtenerClientes.execute();
            obtenerMarcas = new getMarcas(getActivity());
            obtenerMarcas.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ventas, container, false);
        getActivity().setTitle("Agregar Venta");

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

        txt_Ventas_ID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txt_Ventas_ID.setError(null);
            }
        });

        getActivity().registerReceiver(receiverClientes, filtroCLientes);
        getActivity().registerReceiver(receiverCatalogos, filtroCatalogos);
        getActivity().registerReceiver(receiverMarcas, filtroMarcas);

        BroadCastReceiverCatalogos();
        BroadCastReceiverClientes();
        BroadCastReceiverMarcas();
        BroadCastReceiverError();
        obtenerCatalogos = new getCatalogos(getActivity(), false);
        obtenerCatalogos.execute();
        obtenerClientes = new getClientes(getActivity(), false);
        obtenerClientes.execute();
        obtenerMarcas = new getMarcas(getActivity());
        obtenerMarcas.execute();

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
                    txt_Ventas_ID.setError("El ID es un campo requerido");
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
                if (txt_Ventas_Marca.getText().toString().matches("")) {
                    txt_Ventas_Marca.setText("vacio");
                }
                String Marca = txt_Ventas_Marca.getText().toString();
                if (!ListaMarcas.contains(Marca)) {
                    String[] marca = new String[1];
                    marca[0] = txt_Ventas_Marca.getText().toString();
                    new AltaMarca().execute(marca);
                }
                if(ListaDeClientes.size() == 0) {
                    Toast.makeText(getActivity(), "Se requiere registrar primero un cliente para realizar la venta", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getContext(), AgregarCliente.class);
                    getActivity().startActivity(i);
                }

                Cliente c = (Cliente) spinner_Venta_Clientes.getSelectedItem();
                Catalogo cat = (Catalogo) spinner_Venta_Catalogos.getSelectedItem();
                String[] params = new String[12];
                params[0] = c.getNombre();
                params[1] = String.valueOf(c.getIdCliente());
                params[2] = String.valueOf(cat.getIDREG());
                params[3] = cat.getNombre();
                params[4] = txt_Ventas_Pagina.getText().toString();
                params[5] = txt_Ventas_Marca.getText().toString();
                params[6] = txt_Ventas_ID.getText().toString();
                params[7] = txt_Ventas_Numero.getText().toString();
                params[8] = txt_Ventas_Costo.getText().toString();
                params[9] = txt_Ventas_Precio.getText().toString();
                params[10] = "0";
                params[11] = "vacio";

                new AltaVenta().execute(params);
                LimpiarCampos();
                obtenerMarcas = new getMarcas(getActivity());
                obtenerMarcas.execute();
                Toast.makeText(getContext(), "Venta agregada correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void BroadCastReceiverCatalogos() {
        filtroCatalogos.addAction("ListaCatalogos");

        receiverCatalogos = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ListaCatalogos")) {
                    catalogos.clear();
                    catalogos = (ArrayList<Catalogo>) intent.getExtras().get("ListaDeCatalogos");
                    if (getActivity() != null) {
                        catalogosAdapter = new AdaptadorSpinnerCatalogo(getActivity(), catalogos);
                        spinner_Venta_Catalogos.setAdapter(catalogosAdapter);
                    }
                }
            }
        };
    }

    private void BroadCastReceiverClientes() {
        filtroCLientes.addAction("ListaClientes");
        receiverClientes = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ListaClientes")) {
                    ListaDeClientes.clear();
                    ListaDeClientes = (ArrayList<Cliente>) intent.getExtras().get("ListaDeClientes");
                    if (getActivity() != null) {
                        clientesAdapter = new AdaptadorSpinnerCliente(getActivity(), ListaDeClientes);
                        spinner_Venta_Clientes.setAdapter(clientesAdapter);
                    }

                }
            }
        };
    }

    private void BroadCastReceiverMarcas() {
        filtroMarcas.addAction("ListaMarcas");
        receiverMarcas = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ListaMarcas")) {
                    ListaMarcas.clear();
                    ListaMarcas = (ArrayList<String>) intent.getExtras().get("ListaDeMarcas");
                    if (getActivity() != null) {
                        ArrayAdapter<String> adaptadorMarcas = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ListaMarcas);
                        txt_Ventas_Marca.setAdapter(adaptadorMarcas);
                        txt_Ventas_Marca.setThreshold(1);
                    }
                }
            }
        };
    }

    private void BroadCastReceiverError() {
        filtroError.addAction("Error");
        receiverError = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("Error")) {
                    String error;
                    error = intent.getStringExtra("Exception");
                    Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                }
            }
        };
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


    @Override
    public void onStop() {
        super.onStop();
        try {
            getActivity().unregisterReceiver(receiverCatalogos);
            getActivity().unregisterReceiver(receiverClientes);
            getActivity().unregisterReceiver(receiverMarcas);
            getActivity().unregisterReceiver(receiverError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
