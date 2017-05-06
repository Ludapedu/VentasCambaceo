package com.upiicsa.cambaceo.Main;

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
import android.widget.EditText;
import android.widget.Toast;

import com.upiicsa.cambaceo.AsynkTask.EditarCliente;
import com.upiicsa.cambaceo.AsynkTask.EliminarCliente;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Cliente;
import com.upiicsa.cambaceo.R;

public class DetallesCliente extends AppCompatActivity {

    public EditText TextNombre;
    public EditText TextApellidoPaterno;
    public EditText TextApellidoMaterno;
    public EditText TextDireccion;
    public EditText TextTelefono;
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
                String[] params = new String[6];
                EditarCliente edit = new EditarCliente(DetallesCliente.this);
                params[0] = String.valueOf(cliente.getIdCliente());
                params[1] = TextNombre.getText().toString();
                params[2] = TextApellidoPaterno.getText().toString();
                params[3] = TextApellidoMaterno.getText().toString();
                params[4] = TextDireccion.getText().toString();
                params[5] = TextTelefono.getText().toString();
                cliente.setNombre(TextNombre.getText().toString());
                cliente.setApellidoPaterno(TextApellidoPaterno.getText().toString());
                cliente.setApellidoMaterno(TextApellidoMaterno.getText().toString());
                cliente.setDireccion(TextDireccion.getText().toString());
                cliente.setTelefono(TextTelefono.getText().toString());
                edit.execute(params);
                TextNombre.setEnabled(false);
                TextApellidoPaterno.setEnabled(false);
                TextApellidoMaterno.setEnabled(false);
                TextDireccion.setEnabled(false);
                TextTelefono.setEnabled(false);
                editar.setVisibility(View.VISIBLE);
                guardar.setVisibility(View.INVISIBLE);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("Cliente", cliente);
                setResult(0, returnIntent);
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
            }break;
            case R.id.btn_detalle_cliente_eliminar: {
                Eliminar();
            }break;
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
                                String[] params = new String[1];
                                EliminarCliente elim = new EliminarCliente(DetallesCliente.this);
                                params[0] = String.valueOf(cliente.getIdCliente());
                                elim.execute(params);
                                //Toast.makeText(getApplicationContext(),"Cliente eliminado", Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(1, returnIntent);
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
