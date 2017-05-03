package com.upiicsa.cambaceo.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.Modelos.Cliente;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luis.perez on 27/09/2016.
 */

public class AdaptadorBuscableCliente extends ArrayAdapter<Cliente> implements Filterable {

    private List<Cliente> originalData = null;
    private List<Cliente> filteredData = null;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();
    Font font = new Font();

    public AdaptadorBuscableCliente(Context context, ArrayList<Cliente> data) {
        super(context,-1,data);
        this.filteredData = data;
        this.originalData = data;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return filteredData.size();
    }

    public Cliente getItem(int position)
    {
        return filteredData.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_list_clientes, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text1 = (TextView) convertView.findViewById(R.id.TextoListCliente);
            holder.text2 = (TextView) convertView.findViewById(R.id.TextoListApellidos);

            // Bind the data efficiently with the holder.

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // If weren't re-ordering this you could rely on what you set last time
        holder.text1.setTypeface(font.setAsset(getContext()));
        holder.text2.setTypeface(font.setAsset(getContext()));
        holder.text1.setText(filteredData.get(position).getNombre());
        holder.text2.setText(filteredData.get(position).getApellidoPaterno() + " " + filteredData.get(position).getApellidoMaterno());

        return convertView;
    }

    static class ViewHolder {
        TextView text1;
        TextView text2;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Cliente> list = originalData;

            int count = list.size();
            final ArrayList<Cliente> nlist = new ArrayList<Cliente>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getNombre();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Cliente>) results.values;
            notifyDataSetChanged();
        }

    }
}
