package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.AsynkTask.AltaCatalogo;
import com.upiicsa.cambaceo.AsynkTask.AltaCliente;
import com.upiicsa.cambaceo.Modelos.Catalogo;
import com.upiicsa.cambaceo.R;

public class AgregarCatalogo extends AppCompatActivity {

    public EditText txtNombreCatalogo;
    private Catalogo catalogo;
    Font font = new Font();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_catalogo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Agregar Catalogo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNombreCatalogo = (EditText) findViewById(R.id.catalogos_nombre);
        txtNombreCatalogo.setTypeface(font.setAsset(this));
        txtNombreCatalogo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtNombreCatalogo.setError(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agregar_catalogo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
            break;
            case R.id.agregar_catalogo: {
                String nombre = txtNombreCatalogo.getText().toString();
                if (nombre.isEmpty()) {
                    txtNombreCatalogo.setError("El nombre del catalogo no puede ser vacío");
                    return false;
                }
                AgregarCatalogo();
                finish();
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void AgregarCatalogo() {
        String[] reg = new String[1];
        reg[0] = txtNombreCatalogo.getText().toString();
        new AltaCatalogo().execute(reg);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        Toast.makeText(getApplicationContext(), "Catalogo agregado correctamente", Toast.LENGTH_SHORT).show();
    }
}
