package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.upiicsa.cambaceo.Adaptadores.AdaptadorSpinnerCliente;
import com.upiicsa.cambaceo.AsynkTask.AltaPago;
import com.upiicsa.cambaceo.AsynkTask.EditarPago;
import com.upiicsa.cambaceo.AsynkTask.EliminarPago;
import com.upiicsa.cambaceo.AsynkTask.getClientes;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Cliente;
import com.upiicsa.cambaceo.Modelos.Pago;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;
import java.util.Calendar;

public class AgregarPago extends AppCompatActivity {

    TextView lbl_Pagos_Cliente;
    EditText txt_Pagos_Monto;
    DatePicker DatePicker_Pagos_Fecha;
    Spinner SpinnerCliente;
    FloatingActionButton Agregar;
    AdaptadorSpinnerCliente clientesAdapter;
    Cliente RegistroCliente;
    ArrayList<Cliente> ListaClientes = new ArrayList<>();
    Pago pago;
    Boolean Visible;
    MenuItem editar;
    MenuItem borrar;
    MenuItem guardar;
    Toolbar toolbar;
    private BroadcastReceiver receiverClientes;
    private IntentFilter filtroClientes = new IntentFilter();
    private getClientes obtenerClientes;
    private EliminarPago eliminarPago;
    private EditarPago editarPago;

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

        registerReceiver(receiverClientes, filtroClientes);
        BroadCastReceiverClientes();
        obtenerClientes = new getClientes(AgregarPago.this, false);
        obtenerClientes.execute();



        lbl_Pagos_Cliente = (TextView) findViewById(R.id.lbl_Cliente_Pagos);
        txt_Pagos_Monto = (EditText) findViewById(R.id.txt_Pagos_Monto);
        DatePicker_Pagos_Fecha = (DatePicker) findViewById(R.id.Pagos_Picker);
        SpinnerCliente = (Spinner) findViewById(R.id.Pagos_Spinner_Clientes);
        Agregar = (FloatingActionButton) findViewById(R.id.btn_Pago_Agregar);

        if (pago != null) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            Agregar.setVisibility(View.INVISIBLE);
            txt_Pagos_Monto.setEnabled(false);
            txt_Pagos_Monto.setText("" + pago.getMonto());
            toolbar.setTitle("Editar Pago");
        }

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
                if(SpinnerCliente.getCount() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Falta seleccionar un Cliente", Toast.LENGTH_SHORT).show();
                    return;
                }

                RegistroCliente = (Cliente) SpinnerCliente.getSelectedItem();

                String[] reg = new String[3];
                reg[0] = RegistroCliente.getIdCliente();
                reg[1] = Dia + "-" + Mes + "-" + ano;
                reg[2] = txt_Pagos_Monto.getText().toString();
                new AltaPago().execute(reg);
                Toast.makeText(getApplicationContext(), "Pago agregado correctamente", Toast.LENGTH_SHORT).show();

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {

            registerReceiver(receiverClientes, filtroClientes);
            obtenerClientes = new getClientes(AgregarPago.this, false);
            obtenerClientes.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BroadCastReceiverClientes() {
        filtroClientes.addAction("ListaClientes");
        receiverClientes = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ListaClientes")) {
                    ListaClientes.clear();
                    ListaClientes = (ArrayList<Cliente>) intent.getExtras().get("ListaDeClientes");
                    clientesAdapter = new AdaptadorSpinnerCliente(AgregarPago.this, ListaClientes);
                    SpinnerCliente.setAdapter(clientesAdapter);
                    if(pago != null)
                    {
                        SpinnerCliente.setSelection(PositionSpinnerClientes(GetClienteFromPago(pago, ListaClientes)));
                        SpinnerCliente.setEnabled(false);
                    }
                }
            }
        };
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

                String[] params = new String[1];
                params[0] = pago.getIDREG();
                new EliminarPago().execute(params);
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
                String fecha = "" + Dia + "-" + Mes + "-" + ano;
                String monto = txt_Pagos_Monto.getText().toString();
                String[] params = new String[3];
                params[0] = pago.getIDREG();
                params[1] = monto;
                params[2] = fecha;
                new EditarPago().execute(params);
                Toast.makeText(getApplicationContext(), "Pago modificado correctamente", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            break;

        }
        return super.onOptionsItemSelected(item);
    }

    private Cliente GetClienteFromPago(Pago pago, ArrayList<Cliente> clientes) {
        Cliente cliente = new Cliente();
        if(("").equals(""))
        for (int x = 0; x < clientes.size(); x++) {
            if (pago.getIdCliente().equals(clientes.get(x).getIdCliente())) {
                cliente = clientes.get(x);
            }
        }
        return cliente;
    }

    private int PositionSpinnerClientes(Cliente item) {
        int position;
        position = clientesAdapter.getPosition(item);
        return position;
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiverClientes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
