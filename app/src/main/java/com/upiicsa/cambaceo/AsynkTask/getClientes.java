package com.upiicsa.cambaceo.AsynkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.upiicsa.cambaceo.Constantes.Constantes;
import com.upiicsa.cambaceo.Modelos.Cliente;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by C on 04/05/2017.
 */

public class getClientes extends AsyncTask<Void, Void, Void> {
    Context context;
    ArrayList<Cliente> listaClientes = new ArrayList<>();
    private ProgressDialog progressDialog;
    boolean mostrar;

    public getClientes(Context ctx, boolean show)
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
            progressDialog.setTitle("Obteniendo Clientes");
            progressDialog.setMessage("Descargando datos...");
            progressDialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        String Stringurl =  Constantes.URL + "clientes";
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

            String jsonOk = "{ clientes:" + respuesta + "}";

            JSONObject json = new JSONObject(jsonOk);
            JSONArray array = json.getJSONArray("clientes");
            for(int x = 0; x<array.length(); x++) {
                JSONObject jsonunitario = array.getJSONObject(x);
                Cliente cli = new Cliente();
                cli.setIdCliente(jsonunitario.getString("_id"));
                cli.setNombre(jsonunitario.getString("nombre"));
                if(jsonunitario.getString("apellidoPaterno").equals("vacio")) {
                    cli.setApellidoPaterno("");
                }else {
                    cli.setApellidoPaterno(jsonunitario.getString("apellidoPaterno"));
                }
                if(jsonunitario.getString("apellidoMaterno").equals("vacio")) {
                    cli.setApellidoMaterno("");
                }else {
                    cli.setApellidoMaterno(jsonunitario.getString("apellidoMaterno"));
                }
                if(jsonunitario.getString("direccion").equals("vacio")) {
                    cli.setDireccion("");
                }else {
                    cli.setDireccion(jsonunitario.getString("direccion"));
                }
                if(jsonunitario.getString("telefono").equals("vacio")) {
                    cli.setTelefono("");
                }else {
                    cli.setTelefono(jsonunitario.getString("telefono"));
                }
                listaClientes.add(cli);
            }

            Log.v("Registro en servidor: ", respuesta);
        } catch (Exception e) {
            Intent i = new Intent("Error");
            i.putExtra("Exception", e.getMessage());
            context.sendBroadcast(i);
            e.printStackTrace();
        }
        Intent i = new Intent("ListaClientes");
        i.putExtra("ListaDeClientes", listaClientes);
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

