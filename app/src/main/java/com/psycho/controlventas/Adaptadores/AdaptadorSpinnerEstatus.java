package com.psycho.controlventas.Adaptadores;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.psycho.controlventas.Modelos.Estatus;
import com.psycho.controlventas.R;

import java.util.ArrayList;

/**
 * Created by luis.perez on 29/09/2016.
 */

public class AdaptadorSpinnerEstatus extends ArrayAdapter<Estatus> {

    private LayoutInflater inflater;
    private ArrayList<Estatus> filteredData = null;
    private Typeface font = Typeface.createFromAsset(getContext().getAssets(), "gloriahallelujah.ttf");
    TextView text1;

    public AdaptadorSpinnerEstatus(Context context, ArrayList<Estatus> data) {
        super(context, -1, data);
        this.filteredData = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

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
        text1.setTypeface(font);
        text1.setText(filteredData.get(position).getEstatus());

        return layout;
    }

    public void setColor(int Color) {
        text1.setTextColor(Color);
    }
}
