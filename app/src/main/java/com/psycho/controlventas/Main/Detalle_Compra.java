package com.psycho.controlventas.Main;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.psycho.controlventas.Assets.Font;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Venta;
import com.psycho.controlventas.R;

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

        BaseDatos dbCompras = new BaseDatos(getApplicationContext(), "Compras", null, 1);
        SQLiteDatabase TablaCompras = dbCompras.getWritableDatabase();
        Cursor ubicacion = TablaCompras.rawQuery("SELECT Ubicacion FROM Compras WHERE IDREGVenta = " + venta.getIDREG(), null);
        if (ubicacion.moveToFirst()) {
            do {
                txtubicacion.setText(ubicacion.getString(0));
            } while (ubicacion.moveToNext());
        }
        ubicacion.close();
        dbCompras.close();


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
                BaseDatos dbCompras = new BaseDatos(getApplicationContext(), "Compras", null, 1);
                SQLiteDatabase TablaCompras = dbCompras.getWritableDatabase();
                ContentValues datos = new ContentValues();
                datos.put("IDREGVenta", venta.getIDREG());
                datos.put("Ubicacion", txtubicacion.getText().toString());
                TablaCompras.insert("Compras", null, datos);
                Toast.makeText(getApplicationContext(), "Ubicación Guardada", Toast.LENGTH_SHORT).show();
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
