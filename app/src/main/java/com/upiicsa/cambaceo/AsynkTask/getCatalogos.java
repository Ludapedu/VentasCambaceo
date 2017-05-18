package com.upiicsa.cambaceo.AsynkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.upiicsa.cambaceo.Constantes.Constantes;
import com.upiicsa.cambaceo.Modelos.Cambio;
import com.upiicsa.cambaceo.Modelos.Catalogo;

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

public class getCatalogos extends AsyncTask<Void, Void, Void> {
    Context context;
    ArrayList<Catalogo> listaCatalogos = new ArrayList<>();
    private ProgressDialog progressDialog;
    boolean mostrar;

    public getCatalogos(Context ctx, boolean show)
    {
        context = ctx;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setCancelable(false);
        mostrar = show;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mostrar) {
            progressDialog.setTitle("Obteniendo Catalogos");
            progressDialog.setMessage("Descargando datos...");
            progressDialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        String Stringurl =  Constantes.URL + "catalogos";
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

            String jsonOk = "{ catalogos:" + respuesta + "}";

            JSONObject json = new JSONObject(jsonOk);
            JSONArray array = json.getJSONArray("catalogos");
            for(int x = 0; x<array.length(); x++) {
                JSONObject jsonunitario = array.getJSONObject(x);
                Catalogo catalogo = new Catalogo();
                catalogo.setIDREG(jsonunitario.getString("_id"));
                catalogo.setNombre(jsonunitario.getString("catalogo"));
                listaCatalogos.add(catalogo);
            }
            Log.v("Registro en servidor: ", respuesta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent i = new Intent("ListaCatalogos");
        i.putExtra("ListaDeCatalogos", listaCatalogos);
        context.sendBroadcast(i);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mostrar) {
            progressDialog.cancel();
        }
    }
}


