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
        String NombreRegistro = null;
        String ApellidoMaterno = params[1];
        String ApellidoPaterno = params[2];
        String Direccion = params[3];
        String Telefono = params[4];
        if(ApellidoPaterno.isEmpty())
        {
            ApellidoPaterno = "Sin%20Datos";
        }
        if(ApellidoMaterno.isEmpty())
        {
            ApellidoMaterno = "Sin%20Datos";
        }
        if(Direccion.isEmpty())
        {
            Direccion = "Sin%20Datos";
        }
        if(Telefono.isEmpty())
        {
            Telefono = "Sin%20Datos";
        }

        String Stringurl =  Constantes.URL +
                "clientes/nuevo" +
                "?Nombre=" + params[0].replace(" ", "%20") +
                "&ApellidoPaterno=" + ApellidoPaterno.replace(" ", "%20") +
                "&ApellidoMaterno=" + ApellidoMaterno.replace(" ", "%20") +
                "&Direccion=" + Direccion.replace(" ", "%20") +
                "&Telefono=" + Telefono.replace(" ", "%20");
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

            int indiceNombre = respuesta.indexOf("{\"Nombre:\",\"") + 12;
            int indiceApellidoPaterno = respuesta.indexOf(",\"ApellidoPaterno\"") - 1;
            NombreRegistro = respuesta.substring(indiceNombre,indiceApellidoPaterno);

            Log.v("Registro en servidor: ", respuesta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "OK";
    }
}
