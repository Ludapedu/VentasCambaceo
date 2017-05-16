package com.upiicsa.cambaceo.Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.upiicsa.cambaceo.AsynkTask.AltaUsuario;
import com.upiicsa.cambaceo.R;

public class AgregarUsuario extends AppCompatActivity {

    private Button btnAgregar;
    private EditText txtUsuario, txtMail, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);

        txtUsuario = (EditText) findViewById(R.id.txtNombreUsuario);
        txtMail = (EditText)findViewById(R.id.txtCorreoElectronico);
        txtPassword = (EditText)findViewById(R.id.txtContrasena);

        txtUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtUsuario.setError(null);
            }
        });
        txtMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtMail.setError(null);
            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtPassword.setError(null);
            }
        });

        btnAgregar = (Button) findViewById(R.id.btnGuardarUsuario);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtUsuario.getText().toString().isEmpty())
                {
                    txtUsuario.setError("EL nombre de usuario es requerido");
                    return;
                }
                if(txtMail.getText().toString().isEmpty())
                {
                    txtMail.setError("EL correo es requerido");
                    return;
                }
                if(txtPassword.getText().toString().isEmpty())
                {
                    txtPassword.setError("El password es requerido");
                    return;
                }
                if(txtPassword.getText().toString().length() < 4)
                {
                    txtPassword.setError("El password debe de ser de 4 caracteres mínimo");
                    return;
                }

                String[] params = new String[3];
                params[0] = txtUsuario.getText().toString();
                params[1] = txtMail.getText().toString();
                params[2] = txtPassword.getText().toString();

                new AltaUsuario().execute(params);

                Toast.makeText(AgregarUsuario.this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(AgregarUsuario.this, Login.class);
                startActivity(i);
                finish();
            }
        });
    }
}
