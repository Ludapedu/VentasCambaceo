package com.upiicsa.cambaceo.Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.upiicsa.cambaceo.R;

public class AgregarUsuario extends AppCompatActivity {

    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);

        btnAgregar = (Button) findViewById(R.id.btnGuardarUsuario);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AgregarUsuario.this, Login.class);
                startActivity(i);
                finish();
            }
        });
    }
}
