package com.psycho.controlventas.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
    public Button BotonDetalleVentas;
    public Cliente cliente;
    Context contexto = this;


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
        BotonDetalleVentas = (Button) findViewById(R.id.btn_detalle_cliente_detalles);

        TextNombre.setTypeface(font);
        TextApellidoPaterno.setTypeface(font);
        TextApellidoMaterno.setTypeface(font);
        TextDireccion.setTypeface(font);
        TextTelefono.setTypeface(font);
        BotonDetalleVentas.setTypeface(font);

        BotonDetalleVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent d = new Intent(getApplicationContext(), Detalle_Ventas_Cliente.class);
                d.putExtra("Cliente", cliente);
                startActivity(d);
            }
        });


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

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.btn_detalle_cliente_eliminar: {
                Eliminar();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void Eliminar()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setMessage("¿Desea eliminar el cliente?, esto borrara todos sus movimientos")
                .setTitle("Confirmar")
                .setCancelable(false)
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BaseDatos dbClientes = new BaseDatos(getApplicationContext(), "Clientes", null, 1);
                                BaseDatos dbVentas = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
                                BaseDatos dbPagos = new BaseDatos(getApplicationContext(), "Pagos", null, 1);

                                SQLiteDatabase clientes = dbClientes.getWritableDatabase();
                                SQLiteDatabase ventas = dbVentas.getWritableDatabase();
                                SQLiteDatabase pagos = dbPagos.getWritableDatabase();

                                clientes.delete("Clientes", "IDREG = " + cliente.getIdCliente(), null);
                                ventas.delete("Ventas", "IDREG = " + cliente.getIdCliente(), null);
                                pagos.delete("Pagos", "IDREG = " + cliente.getIdCliente(), null);

                                dbClientes.close();
                                dbVentas.close();
                                dbPagos.close();

                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                Toast.makeText(getApplicationContext(),"Cliente eliminado", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalles_cliente, menu);
        return true;
    }
}
