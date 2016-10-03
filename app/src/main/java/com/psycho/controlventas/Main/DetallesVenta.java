package com.psycho.controlventas.Main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.psycho.controlventas.Adaptadores.AdaptadorSpinnerCatalogo;
import com.psycho.controlventas.Adaptadores.AdaptadorSpinnerCliente;
import com.psycho.controlventas.Adaptadores.AdaptadorSpinnerEstatus;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Catalogo;
import com.psycho.controlventas.Modelos.Cliente;
import com.psycho.controlventas.Modelos.Estatus;
import com.psycho.controlventas.Modelos.Venta;
import com.psycho.controlventas.R;

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

                if (entregado.getIdEstatus() == 5)
                {
                    BaseDatos dbVentas = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
                    SQLiteDatabase TablaVentas = dbVentas.getWritableDatabase();
                    TablaVentas.delete("Ventas", "IDREG = " + RegistroVenta.getIDREG(), null);
                    dbVentas.close();
                }

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

    private void EstadoInicial()
    {
        catalogos.add(new Catalogo("Caballeros"));
        catalogos.add(new Catalogo("Vestir Dama"));
        catalogos.add(new Catalogo("Botas Dama"));
        catalogos.add(new Catalogo("Comfort"));
        catalogos.add(new Catalogo("Infantiles"));
        catalogos.add(new Catalogo("Importados"));
        catalogos.add(new Catalogo("Ropa Caballeros"));
        catalogos.add(new Catalogo("Ropa Ninos"));

        estatus.add(new Estatus("Pedido", 0));
        estatus.add(new Estatus("Comprar", 1));
        estatus.add(new Estatus("Entregar", 2));
        estatus.add(new Estatus("Cambiar", 3));
        estatus.add(new Estatus("Cancelar", 4));
        estatus.add(new Estatus("Eliminar", 5));

        BaseDatos dbClientes = new BaseDatos(getApplicationContext(), "Clientes", null, 1);
        SQLiteDatabase clientes = dbClientes.getWritableDatabase();
        ListaDeClientes.clear();

        Cursor filacliente = clientes.rawQuery("SELECT IDREG, Nombre, ApellidoPaterno, ApellidoMaterno, Direccion, Telefono FROM Clientes",null);
        if(filacliente.moveToFirst())
        {
            do {
                RegistroCliente = new Cliente();
                RegistroCliente.setIdCliente(filacliente.getInt(0));
                RegistroCliente.setNombre(filacliente.getString(1));
                RegistroCliente.setApellidoPaterno(filacliente.getString(2));
                RegistroCliente.setApellidoMaterno(filacliente.getString(3));
                RegistroCliente.setDireccion(filacliente.getString(4));
                RegistroCliente.setTelefono(filacliente.getString(5));
                ListaDeClientes.add(RegistroCliente);
            }while (filacliente.moveToNext());
        }
        filacliente.close();
        dbClientes.close();

        clientesadapter = new AdaptadorSpinnerCliente(getApplicationContext(), ListaDeClientes);
        estatusadapter = new AdaptadorSpinnerEstatus(getApplicationContext(), estatus);
        catalogoadapter = new AdaptadorSpinnerCatalogo(getApplicationContext(), catalogos);


        SpinnerCatalogos.setAdapter(catalogoadapter);
        SpinnerEstatus.setAdapter(estatusadapter);
        SpinnerClientes.setAdapter(clientesadapter);

        TextPagina.setText(String.valueOf(RegistroVenta.getPagina()));
        TextNumero.setText(String.valueOf(RegistroVenta.getNumero()));
        TextMarca.setText(RegistroVenta.getMarca());
        TextID.setText(String.valueOf(RegistroVenta.getID()));
        TextCosto.setText(String.valueOf(RegistroVenta.getCosto()));
        TextPrecio.setText(String.valueOf(RegistroVenta.getPrecio()));
        SpinnerCatalogos.setSelection(PositionSpinnerCatalogos(GetCatalogoFromPedido(RegistroVenta, catalogos)));
        SpinnerEstatus.setSelection(PositionSpinnerEstatus(GetEstatusFromPedido(RegistroVenta, estatus)));
        SpinnerClientes.setSelection(PositionSpinnerClientes(GetClienteFromPedido(RegistroVenta, ListaDeClientes)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
        Catalogo cat = new Catalogo("");
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
}
