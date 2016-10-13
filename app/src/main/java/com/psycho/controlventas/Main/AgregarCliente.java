package com.psycho.controlventas.Main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.psycho.controlventas.Assets.Font;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Cliente;
import com.psycho.controlventas.R;

public class AgregarCliente extends AppCompatActivity {

    public EditText TextNombre;
    public EditText TextApellidoMaterno;
    public EditText TextApellidoPaterno;
    public EditText TextDireccion;
    public EditText TextTelefono;
    private Cliente cliente;
    Font font = new Font();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_cliente);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Agregar Cliente");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextNombre = (EditText) findViewById(R.id.clientes_nombre_cliente);
        TextApellidoPaterno = (EditText) findViewById(R.id.clientes_apellidopaterno);
        TextApellidoMaterno = (EditText) findViewById(R.id.clientes_apellidomaterno);
        TextDireccion = (EditText) findViewById(R.id.clientes_direccion);
        TextTelefono = (EditText) findViewById(R.id.clientes_telefono);

        TextNombre.setTypeface(font.setAsset(this));
        TextApellidoPaterno.setTypeface(font.setAsset(this));
        TextApellidoMaterno.setTypeface(font.setAsset(this));
        TextDireccion.setTypeface(font.setAsset(this));
        TextTelefono.setTypeface(font.setAsset(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
            break;
            case R.id.agregar_cliente_agregar: {
                String nombre = TextNombre.getText().toString();
                if (nombre.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "El cliente no puede ser vacío", Toast.LENGTH_SHORT).show();
                    return false;
                }
                AgregarCliente();
                finish();
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void AgregarCliente() {
        ContentValues datos = new ContentValues();
        datos.put("Nombre", TextNombre.getText().toString());
        datos.put("ApellidoPaterno", TextApellidoPaterno.getText().toString());
        datos.put("ApellidoMaterno", TextApellidoMaterno.getText().toString());
        datos.put("Direccion", TextDireccion.getText().toString());
        datos.put("Telefono", TextTelefono.getText().toString());
        datos.put("IdCliente", 0);
        BaseDatos db = new BaseDatos(getApplicationContext(), "Clientes", null, 1);
        SQLiteDatabase clientes = db.getWritableDatabase();
        clientes.insert("Clientes", null, datos);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Cliente", cliente);
        setResult(Activity.RESULT_OK, returnIntent);
        Toast.makeText(getApplicationContext(), "Cliente agregado correctamente", Toast.LENGTH_SHORT).show();
    }

}
