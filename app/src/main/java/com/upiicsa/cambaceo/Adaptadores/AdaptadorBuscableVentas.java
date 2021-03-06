package com.upiicsa.cambaceo.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.Modelos.Venta;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luis.perez on 29/09/2016.
 */

public class AdaptadorBuscableVentas extends ArrayAdapter<Venta> implements Filterable {

    private List<Venta> originalData = null;
    private List<Venta> filteredData = null;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();
    Font font = new Font();

    public AdaptadorBuscableVentas(Context context, ArrayList<Venta> data) {
        super(context, -1, data);
        this.filteredData = data;
        this.originalData = data;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return filteredData.size();
    }

    public Venta getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_list_pedidos, null);

            holder = new ViewHolder();
            holder.Marca = (TextView) convertView.findViewById(R.id.list_pedidos_marca);
            holder.ID = (TextView) convertView.findViewById(R.id.list_pedidos_ID);
            holder.Numero = (TextView) convertView.findViewById(R.id.list_pedidos_numero);
            holder.NombreCliente = (TextView) convertView.findViewById(R.id.list_pedidos_Cliente);
            holder.lblNumero = (TextView) convertView.findViewById(R.id.list_pedidos_lblNumero);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // If weren't re-ordering this you could rely on what you set last time
        holder.Marca.setTypeface(font.setAsset(getContext()));
        holder.ID.setTypeface(font.setAsset(getContext()));
        holder.Numero.setTypeface(font.setAsset(getContext()));
        holder.NombreCliente.setTypeface(font.setAsset(getContext()));
        holder.lblNumero.setTypeface(font.setAsset(getContext()));
        holder.Marca.setText(filteredData.get(position).getIdMarca());
        holder.ID.setText(String.valueOf(filteredData.get(position).getID()));
        holder.Numero.setText(String.valueOf(filteredData.get(position).getNumero()));
        holder.NombreCliente.setText(filteredData.get(position).getIdCliente());
        return convertView;
    }

    static class ViewHolder {
        //LinearLayout fondo;
        TextView Marca;
        TextView ID;
        TextView Numero;
        TextView NombreCliente;
        TextView lblNumero;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Venta> list = originalData;

            int count = list.size();
            final ArrayList<Venta> nlist = new ArrayList<Venta>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getCliente();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Venta>) results.values;
            notifyDataSetChanged();
        }
    }
}
