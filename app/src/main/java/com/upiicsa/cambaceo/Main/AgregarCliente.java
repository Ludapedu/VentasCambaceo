package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.AsynkTask.AltaCliente;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Cliente;
import com.upiicsa.cambaceo.R;

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

        TextNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                TextNombre.setError(null);
            }
        });


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
                    TextNombre.setError("El nombre del cliente no puede ser vacío");
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
        String[] reg = new String[5];
        reg[0] = TextNombre.getText().toString();
        reg[1] = TextApellidoPaterno.getText().toString();
        reg[2] = TextApellidoMaterno.getText().toString();
        reg[3] = TextDireccion.getText().toString();
        reg[4] = TextTelefono.getText().toString();
        new AltaCliente().execute(reg);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Cliente", cliente);
        setResult(Activity.RESULT_OK, returnIntent);
        Toast.makeText(getApplicationContext(), "Cliente agregado correctamente", Toast.LENGTH_SHORT).show();
    }

}
