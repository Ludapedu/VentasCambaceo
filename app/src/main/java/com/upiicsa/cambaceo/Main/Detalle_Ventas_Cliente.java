package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.upiicsa.cambaceo.Adaptadores.AdaptadorBuscableVentas;
import com.upiicsa.cambaceo.Adaptadores.AdaptadorPagos;
import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Cliente;
import com.upiicsa.cambaceo.Modelos.Pago;
import com.upiicsa.cambaceo.Modelos.Venta;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;

public class Detalle_Ventas_Cliente extends AppCompatActivity {

    private final int EDIT_VENTA = 60;
    private final int EDIT_PAGO = 35;
    private final int EDIT_CLIENTE = 70;

    TextView lbl_Cliente_Monto_Abonado;
    TextView lbl_Cliente_Monto_Pendiente;
    TextView lbl_Cliente_Compras;
    TextView lbl_Cliente_Cambios;
    TextView lbl_Cliente_Pagos;
    TextView txt_Cliente_Monto_Abonado;
    TextView txt_Cliente_Monto_Pendiente;
    ListView ListView_Compras;
    ListView ListView_Cambios;
    ListView ListView_Pagos;
    Cliente cliente;
    AdaptadorBuscableVentas adaptadorpedidos;
    AdaptadorBuscableVentas adaptadorcambios;
    AdaptadorPagos adaptadorpagos;
    Venta RegistroCompras;
    Venta RegistroCambios;
    Pago RegistroPago;
    ArrayList<Venta> ListaCompras = new ArrayList<>();
    ArrayList<Venta> ListaCambios = new ArrayList<>();
    ArrayList<Pago> ListaPagos = new ArrayList<>();
    Font font = new Font();
    CollapsingToolbarLayout tool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__ventas__cliente);
        Bundle bundle = getIntent().getExtras();
        cliente = (Cliente) bundle.getSerializable("Cliente");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ventas_cliente);
        tool = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_ventas_cliente);
        setSupportActionBar(toolbar);
        tool.setTitleEnabled(true);
        tool.setTitle(cliente.getNombre());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(cliente.getNombre());


        lbl_Cliente_Monto_Abonado = (TextView) findViewById(R.id.lbl_Cliente_Monto_Abonado);
        lbl_Cliente_Monto_Pendiente = (TextView) findViewById(R.id.lbl_Cliente_Monto_Pendiente);
        lbl_Cliente_Compras = (TextView) findViewById(R.id.lbl_Cliente_Compras);
        lbl_Cliente_Cambios = (TextView) findViewById(R.id.lbl_Cliente_Cambios);
        lbl_Cliente_Pagos = (TextView) findViewById(R.id.lbl_Cliente_Pagos);
        txt_Cliente_Monto_Abonado = (TextView) findViewById(R.id.txt_Cliente_Monto_Abonado);
        txt_Cliente_Monto_Pendiente = (TextView) findViewById(R.id.txt_Cliente_Monto_Pendiente);
        ListView_Compras = (ListView) findViewById(R.id.Lista_Cliente_Compras);
        ListView_Cambios = (ListView) findViewById(R.id.Lista_Clientes_Cambios);
        ListView_Pagos = (ListView) findViewById(R.id.Lista_Clientes_Pagos);

        lbl_Cliente_Monto_Abonado.setTypeface(font.setAsset(this));
        lbl_Cliente_Monto_Pendiente.setTypeface(font.setAsset(this));
        lbl_Cliente_Compras.setTypeface(font.setAsset(this));
        lbl_Cliente_Cambios.setTypeface(font.setAsset(this));
        lbl_Cliente_Pagos.setTypeface(font.setAsset(this));
        txt_Cliente_Monto_Pendiente.setTypeface(font.setAsset(this));
        txt_Cliente_Monto_Abonado.setTypeface(font.setAsset(this));


        ActualizarListViews();

        ListView_Compras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venta v = adaptadorpedidos.getItem(position);
                Intent intent = new Intent(getApplicationContext(),DetallesVenta.class);
                intent.putExtra("Venta", v);
                startActivityForResult(intent, EDIT_VENTA);
            }
        });

        ListView_Cambios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venta v = adaptadorcambios.getItem(position);
                Intent intent = new Intent(getApplicationContext(),DetallesVenta.class);
                intent.putExtra("Venta", v);
                startActivityForResult(intent, EDIT_VENTA);
            }
        });

        ListView_Pagos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pago pagoselected = adaptadorpagos.getItem(position);
                Intent editarpago = new Intent(getApplicationContext(), AgregarPago.class);
                editarpago.putExtra("Pago", pagoselected);
                editarpago.putExtra("Visible", true);
                startActivityForResult(editarpago, EDIT_PAGO);
            }
        });

    }

    private void ActualizarListViews() {
        tool.setTitle(cliente.getNombre());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                onBackPressed();
            }break;
            case R.id.btn_detalle_ventas_editar:
            {
                Intent i = new Intent(getApplicationContext(), DetallesCliente.class);
                i.putExtra("Cliente", cliente);
                startActivityForResult(i, EDIT_CLIENTE);
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle__ventas__cliente, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_VENTA)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ActualizarListViews();
            }
        }
        if(requestCode == EDIT_CLIENTE)
        {
            if(resultCode == 0)
            {
                cliente = (Cliente) data.getExtras().get("Cliente");
                ActualizarListViews();
            }
            if(resultCode == 1)
            {
                Intent i = new Intent();
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
        if(requestCode == EDIT_PAGO)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ActualizarListViews();
            }
        }
    }
}
