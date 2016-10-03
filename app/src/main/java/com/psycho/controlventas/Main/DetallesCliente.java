package com.psycho.controlventas.Main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Cliente;
import com.psycho.controlventas.R;

public class DetallesCliente extends AppCompatActivity {

    public EditText TextNombre;
    public EditText TextApellidoPaterno;
    public EditText TextApellidoMaterno;
    public EditText TextDireccion;
    public EditText TextTelefono;
    public Cliente cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cliente);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detalles_cliente);
        CollapsingToolbarLayout tool = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        tool.setTitleEnabled(true);
        tool.setTitle("Detalle");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Detalle");

        Typeface font = Typeface.createFromAsset(getAssets(), "gloriahallelujah.ttf");

        TextNombre = (EditText)findViewById(R.id.detalles_cliente_nombre);
        TextApellidoPaterno = (EditText)findViewById(R.id.detalles_cliente_apellidopaterno);
        TextApellidoMaterno = (EditText)findViewById(R.id.detalles_cliente_apellidomaterno);
        TextDireccion = (EditText)findViewById(R.id.detalles_cliente_direccion);
        TextTelefono = (EditText)findViewById(R.id.detalles_cliente_telefono);
        final FloatingActionButton editar = (FloatingActionButton) findViewById(R.id.detalle_cliente_editar);
        final FloatingActionButton guardar = (FloatingActionButton) findViewById(R.id.detalle_cliente_guardar);
        final FloatingActionButton eliminar = (FloatingActionButton) findViewById(R.id.detalle_cliente_eliminar);

        TextNombre.setTypeface(font);
        TextApellidoPaterno.setTypeface(font);
        TextApellidoMaterno.setTypeface(font);
        TextDireccion.setTypeface(font);
        TextTelefono.setTypeface(font);


        Bundle bundle = getIntent().getExtras();
        cliente = (Cliente) bundle.getSerializable("Cliente");

        TextNombre.setText(cliente.getNombre());
        TextApellidoPaterno.setText(cliente.getApellidoPaterno());
        TextApellidoMaterno.setText(cliente.getApellidoMaterno());
        TextDireccion.setText(cliente.getDireccion());
        TextTelefono.setText(cliente.getTelefono());

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextNombre.setEnabled(true);
                TextApellidoPaterno.setEnabled(true);
                TextApellidoMaterno.setEnabled(true);
                TextDireccion.setEnabled(true);
                TextTelefono.setEnabled(true);
                editar.setVisibility(View.INVISIBLE);
                guardar.setVisibility(View.VISIBLE);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues datos = new ContentValues();
                datos.put("Nombre", TextNombre.getText().toString());
                datos.put("ApellidoPaterno", TextApellidoPaterno.getText().toString());
                datos.put("ApellidoMaterno", TextApellidoMaterno.getText().toString());
                datos.put("Direccion", TextDireccion.getText().toString());
                datos.put("Telefono", TextTelefono.getText().toString());

                BaseDatos db = new BaseDatos(getApplicationContext(), "Clientes", null, 1);
                SQLiteDatabase clientes = db.getWritableDatabase();
                clientes.update("Clientes", datos, "IDREG = " + cliente.getIdCliente(), null);
                db.close();
                TextNombre.setEnabled(false);
                TextApellidoPaterno.setEnabled(false);
                TextApellidoMaterno.setEnabled(false);
                TextDireccion.setEnabled(false);
                TextTelefono.setEnabled(false);
                editar.setVisibility(View.VISIBLE);
                guardar.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(),"Cliente guardado", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseDatos db = new BaseDatos(getApplicationContext(), "Clientes", null, 1);
                SQLiteDatabase clientes = db.getWritableDatabase();
                clientes.delete("Clientes", "IDREG = " + cliente.getIdCliente(), null);
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                Toast.makeText(getApplicationContext(),"Cliente eliminado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
