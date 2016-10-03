package com.psycho.controlventas.Adaptadores;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.psycho.controlventas.Modelos.Venta;
import com.psycho.controlventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luis.perez on 29/09/2016.
 */

public class AdaptadorBuscablePedidos extends ArrayAdapter<Venta> implements Filterable {

    private List<Venta> originalData = null;
    private List<Venta> filteredData = null;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();
    private Typeface font = Typeface.createFromAsset(getContext().getAssets(), "gloriahallelujah.ttf");

    public AdaptadorBuscablePedidos(Context context, ArrayList<Venta> data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_list_pedidos, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.Marca = (TextView) convertView.findViewById(R.id.list_pedidos_marca);
            holder.ID = (TextView) convertView.findViewById(R.id.list_pedidos_ID);
            holder.Numero = (TextView) convertView.findViewById(R.id.list_pedidos_numero);
            holder.NombreCliente = (TextView) convertView.findViewById(R.id.list_pedidos_Cliente);
            holder.Costo = (TextView) convertView.findViewById(R.id.list_pedidos_costo);
            holder.Precio = (TextView) convertView.findViewById(R.id.list_pedidos_precio);
            holder.lblNumero = (TextView) convertView.findViewById(R.id.list_pedidos_lblNumero);
            holder.lblCosto = (TextView) convertView.findViewById(R.id.list_pedidos_lblCosto);
            holder.lblPrecio = (TextView) convertView.findViewById(R.id.list_pedidos_lblPrecio);


            // Bind the data efficiently with the holder.

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // If weren't re-ordering this you could rely on what you set last time
        holder.Marca.setTypeface(font);
        holder.ID.setTypeface(font);
        holder.Numero.setTypeface(font);
        holder.NombreCliente.setTypeface(font);
        holder.Costo.setTypeface(font);
        holder.Precio.setTypeface(font);
        holder.lblNumero.setTypeface(font);
        holder.lblCosto.setTypeface(font);
        holder.lblPrecio.setTypeface(font);
        holder.Marca.setText(filteredData.get(position).getMarca());
        holder.ID.setText(String.valueOf(filteredData.get(position).getID()));
        holder.Numero.setText(String.valueOf(filteredData.get(position).getNumero()));
        holder.NombreCliente.setText(filteredData.get(position).getCliente());
        holder.Costo.setText(String.valueOf(filteredData.get(position).getCosto()));
        holder.Precio.setText(String.valueOf(filteredData.get(position).getPrecio()));

        return convertView;
    }

    static class ViewHolder {
        TextView Marca;
        TextView ID;
        TextView Numero;
        TextView NombreCliente;
        TextView Costo;
        TextView Precio;
        TextView lblNumero;
        TextView lblCosto;
        TextView lblPrecio;
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
