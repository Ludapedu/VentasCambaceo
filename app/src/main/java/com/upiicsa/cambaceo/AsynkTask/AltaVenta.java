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

public class AltaVenta extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {


        String Stringurl = Constantes.URL +
                "ventas/nuevo" +
                "?Cliente=" + params[0].replace(" ", "%20") +
                "&IdCliente=" + params[1].replace(" ", "%20") +
                "&IdCatalogo=" + params[2].replace(" ", "%20") +
                "&Catalogo=" + params[3].replace(" ", "%20") +
                "&Pagina=" + params[4].replace(" ", "%20") +
                "&Marca=" + params[5].replace(" ", "%20") +
                "&ID=" + params[6].replace(" ", "%20") +
                "&Numero=" + params[7].replace(" ", "%20") +
                "&Costo=" + params[8].replace(" ", "%20") +
                "&Precio=" + params[9].replace(" ", "%20") +
                "&Entregado=" + params[10].replace(" ", "%20") +
                "&Ubicacion=" + params[11].replace(" ", "%20");

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
        return "OK";
    }
}

