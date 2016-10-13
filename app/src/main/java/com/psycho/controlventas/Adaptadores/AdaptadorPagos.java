package com.psycho.controlventas.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.psycho.controlventas.Assets.Font;
import com.psycho.controlventas.Modelos.Pago;
import com.psycho.controlventas.R;

import java.util.ArrayList;

/**
 * Created by luis.perez on 04/10/2016.
 */

public class AdaptadorPagos extends ArrayAdapter<Pago> {

    ArrayList<Pago> pagos = null;
    LayoutInflater mInflater;
    Font font = new Font();

    public AdaptadorPagos(Context context, ArrayList<Pago> data) {
        super(context, -1, data);
        pagos = data;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_list_pagos, null);

            holder = new ViewHolder();
            holder.Fecha = (TextView) convertView.findViewById(R.id.list_pago_fecha);
            holder.Monto = (TextView) convertView.findViewById(R.id.list_pago_monto);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // If weren't re-ordering this you could rely on what you set last time
        holder.Fecha.setTypeface(font.setAsset(getContext()));
        holder.Monto.setTypeface(font.setAsset(getContext()));

        holder.Fecha.setText(pagos.get(position).getFechaPago());
        holder.Monto.setText("$ "+ pagos.get(position).getMonto());

        return convertView;
    }

    static class ViewHolder {
        TextView Fecha;
        TextView Monto;
    }
}
