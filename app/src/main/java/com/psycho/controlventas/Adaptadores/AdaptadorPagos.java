package com.psycho.controlventas.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.psycho.controlventas.Modelos.Pago;

import java.util.ArrayList;

/**
 * Created by luis.perez on 04/10/2016.
 */

public class AdaptadorPagos extends ArrayAdapter<Pago> {

    ArrayList<Pago> pagos = null;
    LayoutInflater mInflater;

    public AdaptadorPagos(Context context, ArrayList<Pago> data) {
        super(context, -1, data);
        pagos = data;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
