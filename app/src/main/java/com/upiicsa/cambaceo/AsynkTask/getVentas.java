package com.upiicsa.cambaceo.AsynkTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.upiicsa.cambaceo.Constantes.Constantes;
import com.upiicsa.cambaceo.Modelos.Cliente;
import com.upiicsa.cambaceo.Modelos.Venta;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by C on 07/05/2017.
 */

public class getVentas extends AsyncTask<Void, Void, Void> {
    Context context;
    ArrayList<Venta> listaVentas = new ArrayList<>();

    public getVentas(Context ctx)
    {
        context = ctx;
    }

    @Override
    protected Void doInBackground(Void... params) {

        String Stringurl =  Constantes.URL + "ventas";
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

            String jsonOk = "{ ventas:" + respuesta + "}";

            JSONObject json = new JSONObject(jsonOk);
            JSONArray array = json.getJSONArray("ventas");
            for(int x = 0; x<array.length(); x++) {
                JSONObject jsonunitario = array.getJSONObject(x);
                Venta venta = new Venta();
                venta.setIDREG(jsonunitario.getInt("IDREG"));
                venta.setCliente(jsonunitario.getString("Cliente"));
                venta.setIdCliente(jsonunitario.getInt("IdCliente"));
                venta.setIdCatalogo(jsonunitario.getInt("IdCatalogo"));
                venta.setCatalogo(jsonunitario.getString("Catalogo"));
                venta.setPagina(jsonunitario.getInt("Pagina"));
                venta.setMarca(jsonunitario.getString("Marca"));
                venta.setID(jsonunitario.getInt("ID"));
                venta.setNumero(Float.valueOf(jsonunitario.getString("Numero")));
                venta.setCosto(jsonunitario.getInt("Costo"));
                venta.setPrecio(jsonunitario.getInt("Precio"));
                venta.setEntregado(jsonunitario.getInt("Entregado"));
                venta.setUbicacion(jsonunitario.getString("Ubicacion"));

                listaVentas.add(venta);
            }

            Log.v("Registro en servidor: ", respuesta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent i = new Intent("ListaVentas");
        i.putExtra("ListaDeVentas", listaVentas);
        context.sendBroadcast(i);
        return null;
    }
}


