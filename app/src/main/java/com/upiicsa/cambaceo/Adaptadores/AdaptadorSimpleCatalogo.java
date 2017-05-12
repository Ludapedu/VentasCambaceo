package com.upiicsa.cambaceo.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.Modelos.Catalogo;
import com.upiicsa.cambaceo.R;
import java.util.ArrayList;
import java.util.List;

public class AdaptadorSimpleCatalogo extends ArrayAdapter<Catalogo> implements Filterable {
    List<Catalogo> originalData = null;
    List<Catalogo> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();
    LayoutInflater mInflater;
    Font font = new Font();

    public AdaptadorSimpleCatalogo(Context context, ArrayList<Catalogo> data) {
        super(context, -1, data);
        originalData = data;
        filteredData = data;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return filteredData.size();
    }

    public Catalogo getItem(int position)
    {
        return filteredData.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_list_views, null);

            holder = new ViewHolder();
            holder.texto = (TextView) convertView.findViewById(R.id.TextoListViews);
            convertView.setTag(holder);
        } else {
            holder = (AdaptadorSimpleCatalogo.ViewHolder) convertView.getTag();
        }

        holder.texto.setTypeface(font.setAsset(getContext()));
        holder.texto.setText(filteredData.get(position).getNombre());
        return convertView;
    }

    static class ViewHolder {
        TextView texto;
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

            final List<Catalogo> list = originalData;

            int count = list.size();
            final ArrayList<Catalogo> nlist = new ArrayList<Catalogo>(count);

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
            filteredData = (ArrayList<Catalogo>) results.values;
            notifyDataSetChanged();
        }

    }
}
