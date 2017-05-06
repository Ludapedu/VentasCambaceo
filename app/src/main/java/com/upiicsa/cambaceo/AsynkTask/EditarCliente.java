package com.upiicsa.cambaceo.AsynkTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.upiicsa.cambaceo.Constantes.Constantes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by C on 05/05/2017.
 */

public class EditarCliente extends AsyncTask<String, Void, Void> {
    Context context;

    public EditarCliente(Context ctx) {
        context = ctx;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String Stringurl = Constantes.URL +
                "clientes/actualizar?" +
                "IDREG=" + strings[0] +
                "&Nombre" + strings[1].replace(" ", "%20") +
                "&ApellidoPaterno" + strings[2].replace(" ", "%20") +
                "&ApellidoMaterno" + strings[3].replace(" ", "%20") +
                "&Direccion" + strings[4].replace(" ", "%20") +
                "&Telefono" + strings[5].replace(" ", "%20");
        try {
            URL url = new URL(Stringurl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");

            int codigoEstado = connection.getResponseCode();
            if (codigoEstado != 200)
                throw new Exception("Error al procesar el registro el codigo http es: " + codigoEstado);
            InputStream inputstream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));

            String respuesta = "";
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                respuesta += linea;
            }
            bufferedReader.close();
            inputstream.close();
            Log.v("Registro en servidor: ", respuesta);
            Intent i = new Intent("EditarCliente");
            context.sendBroadcast(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
