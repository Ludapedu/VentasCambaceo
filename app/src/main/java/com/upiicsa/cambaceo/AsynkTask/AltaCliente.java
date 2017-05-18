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
 * Created by C on 03/05/2017.
 */

public class AltaCliente extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        String ApellidoPaterno = params[1];
        String ApellidoMaterno = params[2];
        String Direccion = params[3];
        String Telefono = params[4];
        if(ApellidoPaterno.isEmpty())
        {
            ApellidoPaterno = "vacio";
        }
        if(ApellidoMaterno.isEmpty())
        {
            ApellidoMaterno = "vacio";
        }
        if(Direccion.isEmpty())
        {
            Direccion = "vacio";
        }
        if(Telefono.isEmpty())
        {
            Telefono = "vacio";
        }

        String Stringurl =  Constantes.URL +
                "clientes/nuevo" +
                "?nombre=" + params[0].replace(" ", "%20") +
                "&apellidoPaterno=" + ApellidoPaterno.replace(" ", "%20") +
                "&apellidoMaterno=" + ApellidoMaterno.replace(" ", "%20") +
                "&direccion=" + Direccion.replace(" ", "%20") +
                "&telefono=" + Telefono.replace(" ", "%20");
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
