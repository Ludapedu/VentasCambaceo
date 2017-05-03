package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Venta;
import com.upiicsa.cambaceo.R;

public class Detalle_Compra extends AppCompatActivity {

    private final int EDIT_VENTA = 60;

    FloatingActionButton guardar;
    TextView txtCliente;
    TextView txtCatalogo;
    TextView txtPagina;
    TextView txtNumero;
    TextView txtMarca;
    TextView txtID;
    TextView txtCosto;
    TextView txtPrecio;
    TextView lblCliente;
    TextView lblCatalogo;
    TextView lblPagina;
    TextView lblNumero;
    TextView lblMarca;
    TextView lblID;
    TextView lblCosto;
    TextView lblPrecio;
    EditText txtubicacion;
    CheckBox comprado;
    Venta venta;
    Font font = new Font();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__compra);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detalle_compra);
        toolbar.setTitle("Detalle de Compra");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Bundle bundle = getIntent().getExtras();
        venta = (Venta) bundle.getSerializable("Venta");

        guardar = (FloatingActionButton) findViewById(R.id.detalle_compra_guardar);
        txtCliente = (TextView) findViewById(R.id.lbl_compras_cliente);
        txtCatalogo = (TextView) findViewById(R.id.lbl_compras_catalogo);
        txtPagina = (TextView) findViewById(R.id.lbl_compras_pagina);
        txtNumero = (TextView) findViewById(R.id.lbl_compras_numero);
        txtMarca = (TextView) findViewById(R.id.lbl_compras_marca);
        txtID = (TextView) findViewById(R.id.lbl_compras_id);
        txtCosto = (TextView) findViewById(R.id.lbl_compras_costo);
        txtPrecio = (TextView) findViewById(R.id.lbl_compras_precio);
        lblCliente = (TextView) findViewById(R.id.lbl_compras_lblcliente);
        lblCatalogo = (TextView) findViewById(R.id.lbl_compras_lblcatalogo);
        lblPagina = (TextView) findViewById(R.id.lbl_compras_lblpagina);
        lblNumero = (TextView) findViewById(R.id.lbl_compras_lblnumero);
        lblMarca = (TextView) findViewById(R.id.lbl_compras_lblmarca);
        lblID = (TextView) findViewById(R.id.lbl_compras_lblid);
        lblCosto = (TextView) findViewById(R.id.lbl_compras_lblcosto);
        lblPrecio = (TextView) findViewById(R.id.lbl_compras_lblprecio);

        txtubicacion = (EditText) findViewById(R.id.txt_compras_ubicacion);
        comprado = (CheckBox) findViewById(R.id.check_compras_comprado);


        txtCliente.setTypeface(font.setAsset(this));
        txtCatalogo.setTypeface(font.setAsset(this));
        txtPagina.setTypeface(font.setAsset(this));
        txtNumero.setTypeface(font.setAsset(this));
        txtMarca.setTypeface(font.setAsset(this));
        txtID.setTypeface(font.setAsset(this));
        txtCosto.setTypeface(font.setAsset(this));
        txtPrecio.setTypeface(font.setAsset(this));
        txtubicacion.setTypeface(font.setAsset(this));
        lblCliente.setTypeface(font.setAsset(this));
        lblCatalogo.setTypeface(font.setAsset(this));
        lblPagina.setTypeface(font.setAsset(this));
        lblNumero.setTypeface(font.setAsset(this));
        lblMarca.setTypeface(font.setAsset(this));
        lblID.setTypeface(font.setAsset(this));
        lblCosto.setTypeface(font.setAsset(this));
        lblPrecio.setTypeface(font.setAsset(this));

        BaseDatos dbVentas = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
        SQLiteDatabase TablaVentas = dbVentas.getReadableDatabase();
        Cursor ubicacion = TablaVentas.rawQuery("SELECT Ubicacion FROM Ventas WHERE IDREG = " + venta.getIDREG(), null);
        if (ubicacion.moveToFirst()) {
            do {
                txtubicacion.setText(ubicacion.getString(0));
            } while (ubicacion.moveToNext());
        }
        ubicacion.close();
        dbVentas.close();

        txtCliente.setText(venta.getCliente());
        txtCatalogo.setText(venta.getCatalogo());
        txtPagina.setText("" + venta.getPagina());
        txtNumero.setText("" + venta.getNumero());
        txtMarca.setText(venta.getMarca());
        txtID.setText("" + venta.getID());
        txtCosto.setText("" + venta.getCosto());
        txtPrecio.setText("" + venta.getPrecio());

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseDatos dbVentas = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
                SQLiteDatabase TablaCompras = dbVentas.getWritableDatabase();
                ContentValues datos = new ContentValues();
                datos.put("Ubicacion", txtubicacion.getText().toString());
                TablaCompras.update("Ventas", datos, "IDREG = " + venta.getIDREG(), null);
                Toast.makeText(getApplicationContext(), "Guardado", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        comprado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ContentValues datos = new ContentValues();
                    datos.put("Entregado", 2);

                    BaseDatos dbVentas = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
                    SQLiteDatabase TablaVentas = dbVentas.getWritableDatabase();
                    TablaVentas.update("Ventas", datos, "IDREG = " + venta.getIDREG(), null);
                    dbVentas.close();

                    Toast.makeText(getApplicationContext(), "Comprado", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_compra, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
            break;
            case R.id.compras_borrar: {
                BaseDatos dbVentas = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
                SQLiteDatabase TablaVentas = dbVentas.getWritableDatabase();
                TablaVentas.delete("Ventas", "IDREG = " + venta.getIDREG(), null);
                dbVentas.close();
                Toast.makeText(getApplicationContext(), "Compra Borrada", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            break;

            case R.id.compras_editar: {
                Intent intent = new Intent(this, DetallesVenta.class);
                intent.putExtra("Venta", venta);
                startActivityForResult(intent, EDIT_VENTA);
                finish();
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_VENTA) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = getIntent().getExtras();
                venta = (Venta) bundle.getSerializable("Venta");
                txtCliente.setText(venta.getCliente());
                txtCatalogo.setText(venta.getCatalogo());
                txtPagina.setText("" + venta.getPagina());
                txtNumero.setText("" + venta.getNumero());
                txtMarca.setText(venta.getMarca());
                txtID.setText("" + venta.getID());
                txtCosto.setText("" + venta.getCosto());
                txtPrecio.setText("" + venta.getPrecio());
            }
        }
    }


}
