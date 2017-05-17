package com.upiicsa.cambaceo.AsynkTask;

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
 * Created by C on 07/05/2017.
 */

public class EliminarCatalogo extends AsyncTask<String,Void,Void> {

    @Override
    protected Void doInBackground(String... params) {

        String Stringurl =  Constantes.URL +
                "catalogos/eliminar" +
                "?_id=" + params[0];
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
