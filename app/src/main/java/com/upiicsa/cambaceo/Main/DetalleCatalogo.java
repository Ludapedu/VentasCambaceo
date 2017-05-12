package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.AsynkTask.EditarCatalogo;
import com.upiicsa.cambaceo.AsynkTask.EliminarCatalogo;
import com.upiicsa.cambaceo.BaseDatos.BaseDatos;
import com.upiicsa.cambaceo.Modelos.Catalogo;
import com.upiicsa.cambaceo.Modelos.Cliente;
import com.upiicsa.cambaceo.Modelos.Estatus;
import com.upiicsa.cambaceo.R;

public class DetalleCatalogo extends AppCompatActivity {

    public FloatingActionButton modificar;
    public FloatingActionButton aceptar;
    public FloatingActionButton cancelar;
    public EditText txtNombreCatalogo;
    private Font font = new Font();
    Catalogo registroCatalogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_catalogo);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Detalle del Catalogo");

        Bundle bundle = getIntent().getExtras();
        registroCatalogo = (Catalogo) bundle.getSerializable("Catalogo");

        txtNombreCatalogo = (EditText) findViewById(R.id.edit_Catalogo_nombre);
        modificar = (FloatingActionButton) findViewById(R.id.btn_Catalogos_Editar);
        aceptar = (FloatingActionButton) findViewById(R.id.btn_Catalogos_Aceptar);
        cancelar = (FloatingActionButton) findViewById(R.id.btn_Catalogos_Cancelar);

        txtNombreCatalogo.setTypeface(font.setAsset(DetalleCatalogo.this));
        txtNombreCatalogo.setText(registroCatalogo.getNombre());
        InhabilitarControles();

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InhabilitarControles();
            }
        });
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabilitarControles();
            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] params = new String[2];

                params[0] = String.valueOf(registroCatalogo.getIDREG());
                params[1] = txtNombreCatalogo.getText().toString();
                new EditarCatalogo().execute(params);

                Toast.makeText(getApplicationContext(),"Catalogo Actualizado", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void InhabilitarControles(){
        txtNombreCatalogo.setEnabled(false);
        modificar.setVisibility(View.VISIBLE);
        aceptar.setVisibility(View.INVISIBLE);
        cancelar.setVisibility(View.INVISIBLE);
    }

    private void HabilitarControles() {

        txtNombreCatalogo.setEnabled(true);
        modificar.setVisibility(View.INVISIBLE);
        aceptar.setVisibility(View.VISIBLE);
        cancelar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_catalogos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }break;
            case R.id.btn_catalogos_eliminar:
            {
                String[] params = new String[1];
                params[0] = String.valueOf(registroCatalogo.getIDREG());
                new EliminarCatalogo().execute(params);
                Toast.makeText(getApplicationContext(),"Catalogo Borrado", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
