package com.upiicsa.cambaceo.AsynkTask;

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

public class EditarPago extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        String Stringurl = Constantes.URL +
                "pagos/actualizar?" +
                "IDREG=" + params[0] +
                "?Cliente=" + params[1].replace(" ", "%20") +
                "?IdCliente=" + params[2].replace(" ", "%20") +
                "?Fecha=" + params[3].replace(" ", "%20") +
                "?Monto=" + params[4].replace(" ", "%20") +
                "?Dia=" + params[5].replace(" ", "%20") +
                "?Mes=" + params[6].replace(" ", "%20") +
                "?Anio=" + params[7].replace(" ", "%20");
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


