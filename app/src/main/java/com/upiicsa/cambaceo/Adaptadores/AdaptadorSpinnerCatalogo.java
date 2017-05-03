package com.upiicsa.cambaceo.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.Modelos.Catalogo;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;

/**
 * Created by luis.perez on 28/09/2016.
 */

public class AdaptadorSpinnerCatalogo extends ArrayAdapter<Catalogo> {

    private LayoutInflater inflater;
    private ArrayList<Catalogo> filteredData = null;
    Font font = new Font();
    TextView text1;

    public AdaptadorSpinnerCatalogo(Context context, ArrayList<Catalogo> data) {
        super(context, -1, data);
        this.filteredData = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    public View getCustomView(int position, View convertView, ViewGroup parent) {


        View layout = inflater.inflate(R.layout.layout_spinner, null);
        text1 = (TextView) layout.findViewById(R.id.text_spinner);
        text1.setTypeface(font.setAsset(getContext()));
        text1.setText(filteredData.get(position).getNombre());

        return layout;
    }

    public void setColor(int Color) {
        text1.setTextColor(Color);
    }
}
