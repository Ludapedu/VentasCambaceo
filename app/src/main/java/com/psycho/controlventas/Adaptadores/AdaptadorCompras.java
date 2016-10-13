package com.psycho.controlventas.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.psycho.controlventas.Assets.Font;
import com.psycho.controlventas.Modelos.Venta;
import com.psycho.controlventas.R;

import java.util.ArrayList;

/**
 * Created by luis.perez on 13/10/2016.
 */

public class AdaptadorCompras extends ArrayAdapter<Venta> {
    ArrayList<Venta> ventas = null;
    LayoutInflater mInflater;
    Font font = new Font();

    public AdaptadorCompras(Context context, ArrayList<Venta> data) {
        super(context, -1, data);
        ventas = data;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_list_compras, null);

            holder = new ViewHolder();
            holder.Marca = (TextView) convertView.findViewById(R.id.list_compras_marca);
            holder.Cliente = (TextView) convertView.findViewById(R.id.list_compras_Cliente);
            holder.ID = (TextView) convertView.findViewById(R.id.list_compras_ID);
            holder.lblNumero = (TextView) convertView.findViewById(R.id.list_compras_lblNumero);
            holder.Numero = (TextView) convertView.findViewById(R.id.list_compras_numero);
            holder.lblUbicacion = (TextView) convertView.findViewById(R.id.list_compras_lblubicacion);
            holder.Ubicacion = (TextView) convertView.findViewById(R.id.list_compras_ubicacion);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // If weren't re-ordering this you could rely on what you set last time
        holder.Marca.setTypeface(font.setAsset(getContext()));
        holder.Cliente.setTypeface(font.setAsset(getContext()));
        holder.ID.setTypeface(font.setAsset(getContext()));
        holder.lblNumero.setTypeface(font.setAsset(getContext()));
        holder.Numero.setTypeface(font.setAsset(getContext()));
        holder.lblUbicacion.setTypeface(font.setAsset(getContext()));
        holder.Ubicacion.setTypeface(font.setAsset(getContext()));

        holder.Marca.setText(ventas.get(position).getMarca());
        holder.Cliente.setText(ventas.get(position).getCliente());
        holder.ID.setText("" + ventas.get(position).getID());
        holder.Numero.setText("" + ventas.get(position).getNumero());
        holder.Ubicacion.setText(ventas.get(position).getUbicacion());

        return convertView;
    }

    static class ViewHolder {
        TextView Marca;
        TextView Cliente;
        TextView ID;
        TextView lblNumero;
        TextView Numero;
        TextView lblUbicacion;
        TextView Ubicacion;
    }
}
