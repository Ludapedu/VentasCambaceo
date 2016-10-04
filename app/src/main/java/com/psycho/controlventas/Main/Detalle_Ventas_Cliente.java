package com.psycho.controlventas.Main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.psycho.controlventas.Adaptadores.AdaptadorBuscablePedidos;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Cliente;
import com.psycho.controlventas.Modelos.Venta;
import com.psycho.controlventas.R;

import java.util.ArrayList;

public class Detalle_Ventas_Cliente extends AppCompatActivity {

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
    Venta RegistroCompras;
    Venta RegistroCambios;
    ArrayList<Venta> ListaCompras = new ArrayList<>();
    ArrayList<Venta> ListaCambios = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__ventas__cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ventas_cliente);
        CollapsingToolbarLayout tool = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_ventas_cliente);
        setSupportActionBar(toolbar);
        tool.setTitleEnabled(true);
        tool.setTitle("Detalle de ventas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Detalle de ventas");

        Bundle bundle = getIntent().getExtras();
        cliente = (Cliente) bundle.getSerializable("Cliente");

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

        ActualizarListViews();

    }

    private void ActualizarListViews() {
        int MontoAvonado = 0;
        int MontoPendiente = 0;
        BaseDatos BDVentas = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
        SQLiteDatabase TablaVentas = BDVentas.getReadableDatabase();
        Cursor pedidos = TablaVentas.rawQuery("SELECT IDREG, Cliente, IdCliente, Catalogo, Pagina, Marca, ID, Numero, Costo, Precio, Entregado FROM Ventas WHERE Entregado = 2 AND IdCliente = " + cliente.getIdCliente(), null);
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
                MontoPendiente += pedidos.getInt(9);
                RegistroCompras.setEntregado(pedidos.getInt(10));
                ListaCompras.add(RegistroCompras);
            } while (pedidos.moveToNext());
        }
        pedidos.close();

        txt_Cliente_Monto_Pendiente.setText("$ " + MontoPendiente);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
