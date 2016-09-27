package com.psycho.controlventas.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.psycho.controlventas.Modelos.Cliente;
import com.psycho.controlventas.R;

public class AgregarCliente extends AppCompatActivity {

    public EditText TextNombre;
    public EditText TextApellidoMaterno;
    public EditText TextApellidoPaterno;
    public EditText TextDireccion;
    public EditText TextTelefono;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_cliente);




        TextNombre = (EditText)findViewById(R.id.clientes_nombre_cliente);
        TextApellidoPaterno = (EditText)findViewById(R.id.clientes_apellidopaterno);
        TextApellidoMaterno = (EditText)findViewById(R.id.clientes_apellidomaterno);
        TextDireccion = (EditText)findViewById(R.id.clientes_direccion);
        TextTelefono = (EditText)findViewById(R.id.clientes_telefono);

        Bundle bundle = getIntent().getExtras();
        if(!bundle.isEmpty()) {
            cliente = (Cliente) bundle.getSerializable("Cliente");
            TextNombre.setText(cliente.getNombre());
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Agregar Cliente");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button = (Button) findViewById(R.id.cliente_agregarcliente);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = TextNombre.getText().toString();
                String apellidopaterno = TextApellidoPaterno.getText().toString();
                String apellidomaterno = TextApellidoMaterno.getText().toString();
                String direccion = TextDireccion.getText().toString();
                String telefono = TextTelefono.getText().toString();
                cliente = new Cliente();
                cliente.setNombre(nombre);
                cliente.setApellidoPaterno(apellidopaterno);
                cliente.setApellidoMaterno(apellidomaterno);
                cliente.setDireccion(direccion);
                cliente.setTelefono(telefono);
                cliente.setIdCliente(0);
                 if(cliente.nombre.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"El cliente no puede ser vacío", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Cliente",cliente);
                setResult(Activity.RESULT_OK, returnIntent);
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
                // finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
