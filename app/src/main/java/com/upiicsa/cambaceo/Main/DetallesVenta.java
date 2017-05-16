package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.upiicsa.cambaceo.Adaptadores.AdaptadorSpinnerCatalogo;
import com.upiicsa.cambaceo.Adaptadores.AdaptadorSpinnerCliente;
import com.upiicsa.cambaceo.Adaptadores.AdaptadorSpinnerEstatus;
import com.upiicsa.cambaceo.AsynkTask.getCatalogos;
import com.upiicsa.cambaceo.AsynkTask.getClientes;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Catalogo;
import com.upiicsa.cambaceo.Modelos.Cliente;
import com.upiicsa.cambaceo.Modelos.Estatus;
import com.upiicsa.cambaceo.Modelos.Venta;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;

/**
 * Created by luis.perez on 30/09/2016.
 */

public class DetallesVenta extends AppCompatActivity {

    Venta RegistroVenta;
    private Cliente RegistroCliente;
    ArrayList<Cliente> ListaDeClientes = new ArrayList<Cliente>();
    ArrayList<Catalogo> catalogos = new ArrayList<Catalogo>();
    ArrayList<Estatus> estatus = new ArrayList<Estatus>();
    private AdaptadorSpinnerCatalogo catalogoadapter;
    private AdaptadorSpinnerCliente clientesadapter;
    private AdaptadorSpinnerEstatus estatusadapter;
    public TextView LblCliente;
    public TextView LblCatalogo;
    public TextView LblEstatus;
    public Spinner SpinnerClientes;
    public Spinner SpinnerCatalogos;
    public Spinner SpinnerEstatus;
    public EditText TextPagina;
    public EditText TextNumero;
    public EditText TextMarca;
    public EditText TextID;
    public EditText TextCosto;
    public EditText TextPrecio;
    public FloatingActionButton modificar;
    public FloatingActionButton aceptar;
    public FloatingActionButton cancelar;
    private BroadcastReceiver receiverCatalogos, receiverClientes;
    private IntentFilter filtroCatalogos = new IntentFilter();
    private IntentFilter filtroClientes = new IntentFilter();
    private getCatalogos obtenerCatalogos;
    private getClientes obtenerClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_venta);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detalles_venta);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Detalle");
        Typeface font = Typeface.createFromAsset(getAssets(), "gloriahallelujah.ttf");

        Bundle bundle = getIntent().getExtras();
        RegistroVenta = (Venta) bundle.getSerializable("Venta");

        LblCliente = (TextView) findViewById(R.id.lbl_Dialogo_Ventas_Cliente);
        LblCatalogo = (TextView) findViewById(R.id.lbl_Dialogo_Ventas_Catalogo);
        LblEstatus = (TextView) findViewById(R.id.lbl_Dialogo_Ventas_Estatus);
        SpinnerClientes = (Spinner) findViewById(R.id.spinner_Dialogo_Ventas_Clientes);
        SpinnerCatalogos = (Spinner) findViewById(R.id.spinner_Dialogo_Ventas_Catalogos);
        SpinnerEstatus = (Spinner) findViewById(R.id.spinner_Dialogo_Ventas_Estatus);
        TextPagina = (EditText) findViewById(R.id.txt_Dialogo_Ventas_Pagina);
        TextNumero = (EditText) findViewById(R.id.txt_Dialogo_Ventas_Numero);
        TextMarca = (EditText) findViewById(R.id.txt_Dialogo_Ventas_Marca);
        TextID = (EditText) findViewById(R.id.txt_Dialogo_Ventas_ID);
        TextCosto = (EditText) findViewById(R.id.txt_Dialogo_Ventas_Costo);
        TextPrecio = (EditText) findViewById(R.id.txt_Dialogo_Ventas_Precio);
        modificar = (FloatingActionButton) findViewById(R.id.btn_Dialogo_Ventas_Editar);
        aceptar = (FloatingActionButton) findViewById(R.id.btn_Dialogo_Ventas_Aceptar);
        cancelar = (FloatingActionButton) findViewById(R.id.btn_Dialogo_Ventas_Cancelar);

        LblCliente.setTypeface(font);
        LblCatalogo.setTypeface(font);
        LblEstatus.setTypeface(font);
        TextPagina.setTypeface(font);
        TextNumero.setTypeface(font);
        TextMarca.setTypeface(font);
        TextID.setTypeface(font);
        TextCosto.setTypeface(font);
        TextPrecio.setTypeface(font);

        BroadCastReceiverClientes();
        BroadCastReceiverCatalogos();
        EstadoInicial();
        InhabilitarControles();

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InhabilitarControles();
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabilitarControles();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cliente cliente = (Cliente)SpinnerClientes.getSelectedItem();
                Catalogo catalogo = (Catalogo) SpinnerCatalogos.getSelectedItem();
                Estatus entregado = (Estatus) SpinnerEstatus.getSelectedItem();


                ContentValues datos = new ContentValues();
                datos.put("Cliente", cliente.getNombre());
                datos.put("IdCliente", cliente.getIdCliente());
                datos.put("Catalogo", catalogo.getNombre());
                datos.put("Pagina", Integer.parseInt(TextPagina.getText().toString()));
                datos.put("Marca", TextMarca.getText().toString());
                datos.put("ID", Integer.parseInt(TextID.getText().toString()));
                datos.put("Numero", Float.parseFloat(TextNumero.getText().toString()));
                datos.put("Costo", Integer.parseInt(TextCosto.getText().toString()));
                datos.put("Precio", TextPrecio.getText().toString());
                datos.put("Entregado", entregado.getIdEstatus());

                BaseDatos dbVentas = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
                SQLiteDatabase TablaVentas = dbVentas.getWritableDatabase();
                TablaVentas.update("Ventas", datos, "IDREG = " + RegistroVenta.getIDREG(), null);
                dbVentas.close();

                Toast.makeText(getApplicationContext(),"Venta Actualizada", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            registerReceiver(receiverCatalogos, filtroCatalogos);
            registerReceiver(receiverClientes, filtroClientes);
            obtenerCatalogos = new getCatalogos(DetallesVenta.this, false);
            obtenerCatalogos.execute();
            obtenerClientes = new getClientes(DetallesVenta.this, false);
            obtenerClientes.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void EstadoInicial()
    {
        obtenerCatalogos = new getCatalogos(DetallesVenta.this, false);
        obtenerCatalogos.execute();

        estatus.add(new Estatus("Pedido", 0));
        estatus.add(new Estatus("Comprar", 1));
        estatus.add(new Estatus("Entregar", 2));
        estatus.add(new Estatus("Cambiar", 3));
        estatus.add(new Estatus("Cancelar", 4));

        obtenerClientes = new getClientes(DetallesVenta.this, false);
        obtenerClientes.execute();
    }

    private void BroadCastReceiverCatalogos() {
        filtroCatalogos.addAction("ListaCatalogos");

        receiverCatalogos = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ListaCatalogos")) {
                    catalogos.clear();
                    catalogos = (ArrayList<Catalogo>) intent.getExtras().get("ListaDeCatalogos");
                    catalogoadapter = new AdaptadorSpinnerCatalogo(DetallesVenta.this, catalogos);
                    SpinnerCatalogos.setAdapter(catalogoadapter);
                    if(catalogos.size() != 0)
                    {
                        SpinnerCatalogos.setSelection(PositionSpinnerCatalogos(GetCatalogoFromPedido(RegistroVenta,catalogos)));
                    }
                }
            }
        };
    }
    private void BroadCastReceiverClientes() {
        filtroClientes.addAction("ListaClientes");

        receiverClientes = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ListaClientes")) {
                    ListaDeClientes.clear();
                    ListaDeClientes = (ArrayList<Cliente>) intent.getExtras().get("ListaDeClientes");
                    clientesadapter = new AdaptadorSpinnerCliente(DetallesVenta.this, ListaDeClientes);
                    SpinnerClientes.setAdapter(clientesadapter);
                    if(ListaDeClientes.size() != 0)
                    {
                        SpinnerClientes.setSelection(PositionSpinnerClientes(GetClienteFromPedido(RegistroVenta,ListaDeClientes)));
                    }
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }break;
            case R.id.btn_detalle_venta_eliminar:
            {
                BaseDatos dbVentas = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
                SQLiteDatabase TablaVentas = dbVentas.getWritableDatabase();
                TablaVentas.delete("Ventas", "IDREG = " + RegistroVenta.getIDREG(), null);
                dbVentas.close();
                Toast.makeText(getApplicationContext(),"Venta borrada", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void InhabilitarControles(){
        SpinnerClientes.setEnabled(false);
        SpinnerCatalogos.setEnabled(false);
        SpinnerEstatus.setEnabled(false);
        TextPagina.setEnabled(false);
        TextNumero.setEnabled(false);
        TextMarca.setEnabled(false);
        TextID.setEnabled(false);
        TextCosto.setEnabled(false);
        TextPrecio.setEnabled(false);
        modificar.setVisibility(View.VISIBLE);
        aceptar.setVisibility(View.INVISIBLE);
        cancelar.setVisibility(View.INVISIBLE);
    }

    private void HabilitarControles() {

        SpinnerClientes.setEnabled(true);
        SpinnerCatalogos.setEnabled(true);
        SpinnerEstatus.setEnabled(true);
        TextPagina.setEnabled(true);
        TextNumero.setEnabled(true);
        TextMarca.setEnabled(true);
        TextID.setEnabled(true);
        TextCosto.setEnabled(true);
        TextPrecio.setEnabled(true);
        modificar.setVisibility(View.INVISIBLE);
        aceptar.setVisibility(View.VISIBLE);
        cancelar.setVisibility(View.VISIBLE);
    }

    private int PositionSpinnerClientes(Cliente item) {
        int position;

        position = clientesadapter.getPosition(item);

        return position;
    }

    private int PositionSpinnerCatalogos(Catalogo cat) {
        int position;

        position = catalogoadapter.getPosition(cat);

        return position;
    }

    private int PositionSpinnerEstatus(Estatus est) {
        int position;

        position = estatusadapter.getPosition(est);

        return position;
    }

    private Cliente GetClienteFromPedido(Venta venta, ArrayList<Cliente> clientes)
    {
        Cliente cliente = new Cliente();
        for(int x=0; x< clientes.size(); x++) {
            if (venta.getIdCliente() == clientes.get(x).getIdCliente())
            {
                cliente = clientes.get(x);
            }
        }
        return cliente;
    }
    private Catalogo GetCatalogoFromPedido(Venta venta, ArrayList<Catalogo> catalogos)
    {
        Catalogo cat = new Catalogo();
        for(int x=0; x< catalogos.size(); x++) {
            if (venta.getCatalogo().equals(catalogos.get(x).getNombre()))
            {
                cat = catalogos.get(x);
            }
        }
        return cat;
    }

    private Estatus GetEstatusFromPedido(Venta venta, ArrayList<Estatus> estatus)
    {
        Estatus est = new Estatus("",0);
        for(int x=0; x< estatus.size(); x++) {
            if (venta.getEntregado()== estatus.get(x).getIdEstatus())
            {
                est = estatus.get(x);
            }
        }
        return est;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_venta, menu);
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiverCatalogos);
            unregisterReceiver(receiverClientes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
