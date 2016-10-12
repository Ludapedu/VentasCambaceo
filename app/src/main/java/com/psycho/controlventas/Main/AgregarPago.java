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
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.psycho.controlventas.Adaptadores.AdaptadorSpinnerCliente;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Cliente;
import com.psycho.controlventas.Modelos.Pago;
import com.psycho.controlventas.R;

import java.util.ArrayList;
import java.util.Calendar;

public class AgregarPago extends AppCompatActivity {

    Calendar cal;
    TextView lbl_Pagos_Cliente;
    EditText txt_Pagos_Monto;
    DatePicker DatePicker_Pagos_Fecha;
    Spinner SpinnerCliente;
    FloatingActionButton Agregar;
    AdaptadorSpinnerCliente adaptadorclientes;
    Cliente RegistroCliente;
    ArrayList<Cliente> Lista_De_Clientes = new ArrayList<>();
    Pago pago;
    Boolean Visible;
    MenuItem editar;
    MenuItem borrar;
    MenuItem guardar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pago);
        toolbar = (Toolbar) findViewById(R.id.toolbar_AgregarPagos);
        toolbar.setTitle("Agregar Pago");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        Visible = bundle.getBoolean("Visible");
        pago = (Pago) bundle.getSerializable("Pago");


        lbl_Pagos_Cliente = (TextView) findViewById(R.id.lbl_Cliente_Pagos);
        txt_Pagos_Monto = (EditText) findViewById(R.id.txt_Pagos_Monto);
        DatePicker_Pagos_Fecha = (DatePicker) findViewById(R.id.Pagos_Picker);
        SpinnerCliente = (Spinner) findViewById(R.id.Pagos_Spinner_Clientes);
        Agregar = (FloatingActionButton) findViewById(R.id.btn_Pago_Agregar);

        if (pago != null) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            Agregar.setVisibility(View.INVISIBLE);
            txt_Pagos_Monto.setEnabled(false);
            SpinnerCliente.setEnabled(false);
            txt_Pagos_Monto.setText("" + pago.getMonto());
            toolbar.setTitle("Editar Pago");
        }

        CargarSpinnerClientes();

        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Dia = DatePicker_Pagos_Fecha.getDayOfMonth();
                int Mes = DatePicker_Pagos_Fecha.getMonth() + 1;
                int ano = DatePicker_Pagos_Fecha.getYear();
                if (txt_Pagos_Monto.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Falta espcificar el monto del pago", Toast.LENGTH_SHORT).show();
                    return;
                }


                Cliente c = (Cliente) SpinnerCliente.getSelectedItem();
                if (c == null) {
                    Toast.makeText(getApplicationContext(), "Se requiere agregar primero a un cliente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AgregarCliente.class);
                    startActivity(intent);
                    return;
                }
                ContentValues datos = new ContentValues();
                datos.put("Cliente", c.getNombre());
                datos.put("IdCliente", c.getIdCliente());
                datos.put("Fecha", "" + Dia + "-" + Mes + "-" + ano);
                datos.put("Monto", Integer.parseInt(txt_Pagos_Monto.getText().toString()));
                BaseDatos db = new BaseDatos(getApplicationContext(), "Pagos", null, 1);
                SQLiteDatabase pagos = db.getWritableDatabase();
                pagos.insert("Pagos", null, datos);
                db.close();
                Toast.makeText(getApplicationContext(), "Pago agregado correctamente", Toast.LENGTH_SHORT).show();
                cal = Calendar.getInstance();

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                Toast.makeText(getApplicationContext(), "Pago agregado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void CargarSpinnerClientes() {
        BaseDatos db = new BaseDatos(getApplicationContext(), "Clientes", null, 1);
        SQLiteDatabase clientes = db.getWritableDatabase();
        Lista_De_Clientes.clear();

        Cursor fila = clientes.rawQuery("SELECT IDREG, Nombre, ApellidoPaterno, ApellidoMaterno, Direccion, Telefono FROM Clientes", null);
        if (fila.moveToFirst()) {
            do {
                RegistroCliente = new Cliente();
                RegistroCliente.setIdCliente(Integer.parseInt(fila.getString(0)));
                RegistroCliente.setNombre(fila.getString(1));
                RegistroCliente.setApellidoPaterno(fila.getString(2));
                RegistroCliente.setApellidoMaterno(fila.getString(3));
                RegistroCliente.setDireccion(fila.getString(4));
                RegistroCliente.setTelefono(fila.getString(5));
                Lista_De_Clientes.add(RegistroCliente);
            } while (fila.moveToNext());
        }
        fila.close();
        db.close();
        SpinnerCliente.setAdapter(new AdaptadorSpinnerCliente(getApplicationContext(), Lista_De_Clientes));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pagos_menu, menu);
        editar = menu.findItem(R.id.pagos_editar);
        borrar = menu.findItem(R.id.pagos_borrar);
        guardar = menu.findItem(R.id.pagos_guardar);

        editar.setVisible(Visible);
        borrar.setVisible(Visible);
        guardar.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
            break;

            case R.id.pagos_borrar: {

                BaseDatos db = new BaseDatos(getApplicationContext(), "Pagos", null, 1);
                SQLiteDatabase pagos = db.getWritableDatabase();
                pagos.delete("Pagos", "IDREG = " + pago.getIDREG(), null);
                db.close();
                Toast.makeText(getApplicationContext(), "Pago borrado correctamente", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            break;
            case R.id.pagos_editar: {

                txt_Pagos_Monto.setEnabled(true);
                editar.setVisible(false);
                guardar.setVisible(true);
            }
            break;
            case R.id.pagos_guardar: {

                int Dia = DatePicker_Pagos_Fecha.getDayOfMonth();
                int Mes = DatePicker_Pagos_Fecha.getMonth() + 1;
                int ano = DatePicker_Pagos_Fecha.getYear();
                Cliente c = (Cliente) SpinnerCliente.getSelectedItem();
                ContentValues datos = new ContentValues();
                datos.put("Fecha", "" + Dia + "-" + Mes + "-" + ano);
                datos.put("Monto", Integer.parseInt(txt_Pagos_Monto.getText().toString()));
                BaseDatos db = new BaseDatos(getApplicationContext(), "Pagos", null, 1);
                SQLiteDatabase pagos = db.getWritableDatabase();
                pagos.update("Pagos", datos, "IDREG = " + pago.getIDREG(), null);
                db.close();
                Toast.makeText(getApplicationContext(), "Pago modificado correctamente", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            break;

        }
        return super.onOptionsItemSelected(item);
    }

}
