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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.psycho.controlventas.Adaptadores.AdaptadorSpinnerCatalogo;
import com.psycho.controlventas.BaseDatos.BaseDatos;
import com.psycho.controlventas.Modelos.Cambio;
import com.psycho.controlventas.Modelos.Catalogo;
import com.psycho.controlventas.Modelos.Venta;
import com.psycho.controlventas.R;

import java.util.ArrayList;

public class DetalleCambio extends AppCompatActivity {

    Venta venta;
    Cambio cambio = new Cambio();
    AdaptadorSpinnerCatalogo adaptadorcatalogos;
    ArrayList<Catalogo> catalogos = new ArrayList<Catalogo>();
    Spinner SpinnerCatalogos;
    Spinner SpinnerCatalogos2;
    EditText Pagina;
    EditText Numero;
    EditText Marca;
    EditText ID;
    EditText Costo;
    EditText Precio;
    EditText Pagina2;
    EditText Numero2;
    AutoCompleteTextView Marca2;
    EditText ID2;
    EditText Costo2;
    EditText Precio2;
    FloatingActionButton btn_Agregar_Cambio;
    private ArrayList<String> ListaMarcas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cambio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cambios);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        catalogos.add(new Catalogo("Caballeros"));
        catalogos.add(new Catalogo("Vestir Dama"));
        catalogos.add(new Catalogo("Botas Dama"));
        catalogos.add(new Catalogo("Comfort"));
        catalogos.add(new Catalogo("Infantiles"));
        catalogos.add(new Catalogo("Importados"));
        catalogos.add(new Catalogo("Ropa Caballeros"));
        catalogos.add(new Catalogo("Ropa Ninos"));

        Bundle bundle = getIntent().getExtras();
        venta = (Venta) bundle.getSerializable("Venta");
        this.setTitle(venta.getCliente());

        SpinnerCatalogos = (Spinner) findViewById(R.id.Cambios_Spinner_Catalogo);
        SpinnerCatalogos2 = (Spinner) findViewById(R.id.Cambios_Spinner_Catalogo2);
        Pagina = (EditText) findViewById(R.id.txt_Cambios_Pagina);
        Numero = (EditText) findViewById(R.id.txt_Cambios_Numero);
        Marca = (EditText) findViewById(R.id.txt_Cambios_Marca);
        ID = (EditText) findViewById(R.id.txt_Cambios_ID);
        Costo = (EditText) findViewById(R.id.txt_Cambios_Costo);
        Precio = (EditText) findViewById(R.id.txt_Cambios_Precio);
        Pagina2 = (EditText) findViewById(R.id.txt_Cambios_Pagina2);
        Numero2 = (EditText) findViewById(R.id.txt_Cambios_Numero2);
        Marca2 = (AutoCompleteTextView) findViewById(R.id.txt_Cambios_Marca2);
        ID2 = (EditText) findViewById(R.id.txt_Cambios_ID2);
        Costo2 = (EditText) findViewById(R.id.txt_Cambios_Costo2);
        Precio2 = (EditText) findViewById(R.id.txt_Cambios_Precio2);
        btn_Agregar_Cambio = (FloatingActionButton) findViewById(R.id.btn_Cambio_Editar);

        CargarMarcas();

        Pagina.setText("" + venta.getPagina());
        Numero.setText(String.valueOf(venta.getNumero()));
        Marca.setText(venta.getMarca());
        ID.setText("" +venta.getID());
        Costo.setText("" +venta.getCosto());
        Precio.setText("" +venta.getPrecio());

        adaptadorcatalogos = new AdaptadorSpinnerCatalogo(getApplicationContext(), catalogos);
        SpinnerCatalogos.setAdapter(adaptadorcatalogos);
        SpinnerCatalogos2.setAdapter(adaptadorcatalogos);
        SpinnerCatalogos.setSelection(PositionSpinnerCatalogos(GetCatalogoFromPedido(venta, catalogos)));
        SpinnerCatalogos.setEnabled(false);

        BaseDatos dbCambios = new BaseDatos(getApplicationContext(), "Cambios", null, 1);
        SQLiteDatabase TablaCambios = dbCambios.getReadableDatabase();
        Cursor cambios = TablaCambios.rawQuery("SELECT IDREG, Catalogo, Pagina, Marca, ID, Numero, Costo, Precio, IDREGVenta FROM Cambios WHERE IDREGVenta = " + venta.getIDREG(), null);
        if (cambios.moveToFirst()) {
            do {
                cambio.setIDREG(cambios.getInt(0));
                cambio.setCatalogo(cambios.getString(1));
                cambio.setPagina(cambios.getInt(2));
                cambio.setMarca(cambios.getString(3));
                cambio.setID(cambios.getInt(4));
                cambio.setNumero(cambios.getFloat(5));
                cambio.setCosto(cambios.getInt(6));
                cambio.setPrecio(cambios.getInt(7));
                cambio.setIDREGVenta(cambios.getInt(8));
            } while (cambios.moveToNext());
        }
        cambios.close();
        dbCambios.close();

        cambio.setCliente(venta.getCliente());
        cambio.setIdCliente(venta.getIdCliente());

        if(cambio.getCatalogo() != null) {
            Pagina2.setText("" + cambio.getPagina());
            Numero2.setText("" + cambio.getNumero());
            Marca2.setText(cambio.getMarca());
            ID2.setText("" + cambio.getID());
            Costo2.setText("" + cambio.getCosto());
            Precio2.setText("" + cambio.getPrecio());

            SpinnerCatalogos2.setSelection(PositionSpinnerCatalogos(GetCatalogoFromCambio(cambio, catalogos)));
            SpinnerCatalogos2.setEnabled(false);
            Pagina2.setEnabled(false);
            Numero2.setEnabled(false);
            Marca2.setEnabled(false);
            ID2.setEnabled(false);
            Costo2.setEnabled(false);
            Precio2.setEnabled(false);
            btn_Agregar_Cambio.setVisibility(View.INVISIBLE);
        }

        btn_Agregar_Cambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ID2.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Es necesario el ID del zapato", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ID2.getText().toString().length() < 5)
                {
                    Toast.makeText(getApplicationContext(), "El ID del zapato debe de ser minimo de 5 números", Toast.LENGTH_SHORT).show();
                    return;
                }
                String Marca = Marca2.getText().toString();
                {
                    if(!ListaMarcas.contains(Marca))
                    {
                        BaseDatos BDMarcas = new BaseDatos(getApplicationContext(), "Marcas", null, 1);
                        SQLiteDatabase Marcas = BDMarcas.getWritableDatabase();
                        Marcas.execSQL("INSERT INTO Marcas(Marca) VALUES(" + "'" + Marca + "'" +")");
                        BDMarcas.close();
                    }
                }
                Catalogo cat = (Catalogo)SpinnerCatalogos2.getSelectedItem();
                BaseDatos dbCambios = new BaseDatos(getApplicationContext(),"Cambios",null,1);
                SQLiteDatabase TablaCambios = dbCambios.getWritableDatabase();
                ContentValues datos = new ContentValues();
                datos.put("Cliente", venta.getCliente());
                datos.put("IdCliente", venta.getIdCliente());
                datos.put("Catalogo", cat.getNombre());
                datos.put("Pagina", Integer.parseInt(Pagina2.getText().toString()));
                datos.put("Marca", Marca2.getText().toString());
                datos.put("ID", Integer.parseInt(ID2.getText().toString()));
                datos.put("Numero", Float.parseFloat(Numero2.getText().toString()));
                datos.put("Costo", Integer.parseInt(Costo2.getText().toString()));
                datos.put("Precio", Integer.parseInt(Precio2.getText().toString()));
                datos.put("IDREGVenta", venta.getIDREG());
                TablaCambios.insert("Cambios", null, datos);
                dbCambios.close();
                Toast.makeText(getApplicationContext(), "Cambio agregado correctamente, revise el nuevo pedido en pestaña de pedidos", Toast.LENGTH_SHORT).show();

                ContentValues cventa = new ContentValues();
                cventa.put("Cliente", venta.getCliente());
                cventa.put("IdCliente", venta.getIdCliente());
                cventa.put("Catalogo", cat.getNombre());
                cventa.put("Pagina", Integer.parseInt(Pagina2.getText().toString()));
                cventa.put("Marca", Marca2.getText().toString());
                cventa.put("ID", Integer.parseInt(ID2.getText().toString()));
                cventa.put("Numero", Float.parseFloat(Numero2.getText().toString()));
                cventa.put("Costo", Integer.parseInt(Costo2.getText().toString()));
                cventa.put("Precio", Integer.parseInt(Precio2.getText().toString()));
                cventa.put("Entregado", 0);
                BaseDatos db = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
                SQLiteDatabase ventas = db.getWritableDatabase();
                ventas.insert("Ventas", null, cventa);
                db.close();

                SpinnerCatalogos2.setEnabled(false);
                Pagina2.setEnabled(false);
                Numero2.setEnabled(false);
                Marca2.setEnabled(false);
                ID2.setEnabled(false);
                Costo2.setEnabled(false);
                Precio2.setEnabled(false);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private int PositionSpinnerCatalogos(Catalogo cat) {
        int position;

        position = adaptadorcatalogos.getPosition(cat);

        return position;
    }

    private Catalogo GetCatalogoFromPedido(Venta venta, ArrayList<Catalogo> catalogos) {
        Catalogo cat = new Catalogo("");
        for (int x = 0; x < catalogos.size(); x++) {
            if (venta.getCatalogo().equals(catalogos.get(x).getNombre())) {
                cat = catalogos.get(x);
            }
        }
        return cat;
    }

    private Catalogo GetCatalogoFromCambio(Cambio cambio, ArrayList<Catalogo> catalogos) {
        Catalogo cat = new Catalogo("");
        for (int x = 0; x < catalogos.size(); x++) {
            if (cambio.getCatalogo().equals(catalogos.get(x).getNombre())) {
                cat = catalogos.get(x);
            }
        }
        return cat;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
            break;
            case R.id.btn_cambios_eliminar:
            {

                BaseDatos dbCambios = new BaseDatos(getApplicationContext(),"Cambios",null,1);
                SQLiteDatabase TablaCambios = dbCambios.getWritableDatabase();
                TablaCambios.delete("Cambios", "IDREG = " + cambio.getIDREG(), null);
                BaseDatos db = new BaseDatos(getApplicationContext(), "Ventas", null, 1);
                SQLiteDatabase ventas = db.getWritableDatabase();
                ventas.delete("Ventas", "IDREG = " + venta.getIDREG(), null);
                Toast.makeText(getApplicationContext(), "Cambio eliminado corretamente", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cambios, menu);
        return true;
    }

    protected void CargarMarcas()
    {
        BaseDatos BDMarcas = new BaseDatos(getApplicationContext(), "Marcas", null, 1);
        SQLiteDatabase Marcas = BDMarcas.getWritableDatabase();
        Cursor filaMarcas = Marcas.rawQuery("SELECT Marca FROM Marcas",null);
        ListaMarcas.clear();
        if(filaMarcas.moveToFirst())
        {
            do
            {
                ListaMarcas.add(filaMarcas.getString(0));
            }while(filaMarcas.moveToNext());
        }
        filaMarcas.close();
        BDMarcas.close();

        ArrayAdapter<String> adaptadorMarcas = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,ListaMarcas);

        Marca2.setAdapter(adaptadorMarcas);
        Marca2.setThreshold(1);
    }

}
