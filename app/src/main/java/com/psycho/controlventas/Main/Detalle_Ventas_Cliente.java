package com.psycho.controlventas.Main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.psycho.controlventas.Adaptadores.AdaptadorBuscablePedidos;
import com.psycho.controlventas.Adaptadores.AdaptadorPagos;
import com.psycho.controlventas.Assets.Font;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Cliente;
import com.psycho.controlventas.Modelos.Pago;
import com.psycho.controlventas.Modelos.Venta;
import com.psycho.controlventas.R;

import java.util.ArrayList;

public class Detalle_Ventas_Cliente extends AppCompatActivity {

    private final int EDIT_VENTA = 60;
    private final int EDIT_PAGO = 35;
    private final int EDIT_CLIENTE = 70;

    TextView lbl_Cliente_Monto_Abonado;
    TextView lbl_Cliente_Monto_Pendiente;
    TextView lbl_Cliente_Compras;
    TextView lbl_Cliente_Cambios;
    TextView lbl_Cliente_Pagos;
    TextView txt_Cliente_Monto_Abonado;
    TextView txt_Cliente_Monto_Pendiente;
    ListView ListView_Compras;
    ListView ListView_Cambios;
    ListView ListView_Pagos;
    Cliente cliente;
    AdaptadorBuscablePedidos adaptadorpedidos;
    AdaptadorBuscablePedidos adaptadorcambios;
    AdaptadorPagos adaptadorpagos;
    Venta RegistroCompras;
    Venta RegistroCambios;
    Pago RegistroPago;
    ArrayList<Venta> ListaCompras = new ArrayList<>();
    ArrayList<Venta> ListaCambios = new ArrayList<>();
    ArrayList<Pago> ListaPagos = new ArrayList<>();
    Font font = new Font();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__ventas__cliente);
        Bundle bundle = getIntent().getExtras();
        cliente = (Cliente) bundle.getSerializable("Cliente");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ventas_cliente);
        CollapsingToolbarLayout tool = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_ventas_cliente);
        setSupportActionBar(toolbar);
        tool.setTitleEnabled(true);
        tool.setTitle(cliente.getNombre());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(cliente.getNombre());


        lbl_Cliente_Monto_Abonado = (TextView) findViewById(R.id.lbl_Cliente_Monto_Abonado);
        lbl_Cliente_Monto_Pendiente = (TextView) findViewById(R.id.lbl_Cliente_Monto_Pendiente);
        lbl_Cliente_Compras = (TextView) findViewById(R.id.lbl_Cliente_Compras);
        lbl_Cliente_Cambios = (TextView) findViewById(R.id.lbl_Cliente_Cambios);
        lbl_Cliente_Pagos = (TextView) findViewById(R.id.lbl_Cliente_Pagos);
        txt_Cliente_Monto_Abonado = (TextView) findViewById(R.id.txt_Cliente_Monto_Abonado);
        txt_Cliente_Monto_Pendiente = (TextView) findViewById(R.id.txt_Cliente_Monto_Pendiente);
        ListView_Compras = (ListView) findViewById(R.id.Lista_Cliente_Compras);
        ListView_Cambios = (ListView) findViewById(R.id.Lista_Clientes_Cambios);
        ListView_Pagos = (ListView) findViewById(R.id.Lista_Clientes_Pagos);

        lbl_Cliente_Monto_Abonado.setTypeface(font.setAsset(this));
        lbl_Cliente_Monto_Pendiente.setTypeface(font.setAsset(this));
        lbl_Cliente_Compras.setTypeface(font.setAsset(this));
        lbl_Cliente_Cambios.setTypeface(font.setAsset(this));
        lbl_Cliente_Pagos.setTypeface(font.setAsset(this));
        txt_Cliente_Monto_Pendiente.setTypeface(font.setAsset(this));
        txt_Cliente_Monto_Abonado.setTypeface(font.setAsset(this));


        ActualizarListViews();

        ListView_Compras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venta v = adaptadorpedidos.getItem(position);
                Intent intent = new Intent(getApplicationContext(),DetallesVenta.class);
                intent.putExtra("Venta", v);
                startActivityForResult(intent, EDIT_VENTA);
            }
        });

        ListView_Cambios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venta v = adaptadorcambios.getItem(position);
                Intent intent = new Intent(getApplicationContext(),DetallesVenta.class);
                intent.putExtra("Venta", v);
                startActivityForResult(intent, EDIT_VENTA);
            }
        });

        ListView_Pagos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pago pagoselected = adaptadorpagos.getItem(position);
                Intent editarpago = new Intent(getApplicationContext(), AgregarPago.class);
                editarpago.putExtra("Pago", pagoselected);
                editarpago.putExtra("Visible", true);
                startActivityForResult(editarpago, EDIT_PAGO);
            }
        });

    }

    private void ActualizarListViews() {
        int MontoAbonado = 0;
        int MontoPendiente = 0;
        int MontoCompras = 0;
        BaseDatos BDVentas = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
        SQLiteDatabase TablaVentas = BDVentas.getReadableDatabase();
        Cursor pedidos = TablaVentas.rawQuery("SELECT IDREG, Cliente, IdCliente, Catalogo, Pagina, Marca, ID, Numero, Costo, Precio, Entregado FROM Ventas WHERE Entregado != 3 AND IdCliente = " + cliente.getIdCliente(), null);
        ListaCompras.clear();
        if (pedidos.moveToFirst()) {
            do {
                RegistroCompras = new Venta();
                RegistroCompras.setIDREG(pedidos.getInt(0));
                RegistroCompras.setCliente(pedidos.getString(1));
                RegistroCompras.setIdCliente(pedidos.getInt(2));
                RegistroCompras.setCatalogo(pedidos.getString(3));
                RegistroCompras.setPagina(pedidos.getInt(4));
                RegistroCompras.setMarca(pedidos.getString(5));
                RegistroCompras.setID(pedidos.getInt(6));
                RegistroCompras.setNumero(pedidos.getFloat(7));
                RegistroCompras.setCosto(pedidos.getInt(8));
                RegistroCompras.setPrecio(pedidos.getInt(9));
                MontoCompras += pedidos.getInt(9);
                RegistroCompras.setEntregado(pedidos.getInt(10));
                ListaCompras.add(RegistroCompras);
            } while (pedidos.moveToNext());
        }
        pedidos.close();

        adaptadorpedidos = new AdaptadorBuscablePedidos(getApplicationContext(), ListaCompras);
        ListView_Compras.setAdapter(adaptadorpedidos);

        ListaCambios.clear();
        Cursor cambios = TablaVentas.rawQuery("SELECT IDREG, Cliente, IdCliente, Catalogo, Pagina, Marca, ID, Numero, Costo, Precio, Entregado FROM Ventas WHERE Entregado = 3 AND IdCliente = " + cliente.getIdCliente(), null);
        if (cambios.moveToFirst()) {
            do {
                RegistroCambios = new Venta();
                RegistroCambios.setIDREG(cambios.getInt(0));
                RegistroCambios.setCliente(cambios.getString(1));
                RegistroCambios.setIdCliente(cambios.getInt(2));
                RegistroCambios.setCatalogo(cambios.getString(3));
                RegistroCambios.setPagina(cambios.getInt(4));
                RegistroCambios.setMarca(cambios.getString(5));
                RegistroCambios.setID(cambios.getInt(6));
                RegistroCambios.setNumero(cambios.getFloat(7));
                RegistroCambios.setCosto(cambios.getInt(8));
                RegistroCambios.setPrecio(cambios.getInt(9));
                RegistroCambios.setEntregado(cambios.getInt(10));
                ListaCambios.add(RegistroCambios);
            } while (cambios.moveToNext());
        }
        cambios.close();
        adaptadorcambios = new AdaptadorBuscablePedidos(getApplicationContext(), ListaCambios);
        ListView_Cambios.setAdapter(adaptadorcambios);
        BDVentas.close();

        ListaPagos.clear();
        BaseDatos BDPagos = new BaseDatos(getApplicationContext(), "Pagos", null, 1);
        SQLiteDatabase TablaPagos = BDPagos.getReadableDatabase();
        Cursor pagos = TablaPagos.rawQuery("SELECT IDREG, Cliente, IdCliente, Fecha, Monto FROM Pagos WHERE IdCliente = " + cliente.getIdCliente(), null);
        if (pagos.moveToFirst()) {
            do {
                RegistroPago = new Pago();
                RegistroPago.setIDREG(pagos.getInt(0));
                RegistroPago.setCliente(pagos.getString(1));
                RegistroPago.setIdCliente(pagos.getInt(2));
                RegistroPago.setFechaPago(pagos.getString(3));
                RegistroPago.setMonto(pagos.getInt(4));
                MontoAbonado += pagos.getInt(4);
                ListaPagos.add(RegistroPago);
            } while (pagos.moveToNext());
        }
        pagos.close();
        adaptadorpagos = new AdaptadorPagos(getApplicationContext(), ListaPagos);
        ListView_Pagos.setAdapter(adaptadorpagos);
        BDPagos.close();

        MontoPendiente = MontoCompras - MontoAbonado;

        if(MontoPendiente < 0)
        {
            MontoPendiente = MontoPendiente * -1;
            lbl_Cliente_Monto_Pendiente.setText("Saldo a favor");
        }
        else
        {
            lbl_Cliente_Monto_Pendiente.setText("Monto Pendiente");
        }

        txt_Cliente_Monto_Pendiente.setText("$ " + MontoPendiente);
        txt_Cliente_Monto_Abonado.setText("$ " + MontoAbonado);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                onBackPressed();
            }break;
            case R.id.btn_detalle_ventas_editar:
            {
                Intent i = new Intent(getApplicationContext(), DetallesCliente.class);
                i.putExtra("Cliente", cliente);
                startActivityForResult(i, EDIT_CLIENTE);
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle__ventas__cliente, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_VENTA)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ActualizarListViews();
            }
        }
        if(requestCode == EDIT_CLIENTE)
        {
            if(resultCode == 0)
            {
                ActualizarListViews();
            }
            if(resultCode == 1)
            {
                Intent i = new Intent();
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
        if(requestCode == EDIT_PAGO)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ActualizarListViews();
            }
        }
    }
}
