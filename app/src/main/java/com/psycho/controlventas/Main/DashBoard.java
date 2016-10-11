package com.psycho.controlventas.Main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.psycho.controlventas.Assets.Font;
import com.psycho.controlventas.R;


public class DashBoard extends Fragment {

    public int i = 0;
    Handler progressHandler = new Handler();
    ProgressBar myprogressBar;
    TextView progressingTextView;
    TextView VentasCompletadas;
    TextView VentasPorCobrar;
    TextView PedidosPorComprar;
    TextView ComprasRealizadas;
    Font font = new Font();


    private OnFragmentInteractionListener mListener;

    public DashBoard() {
    }


    public static DashBoard newInstance() {
        DashBoard fragment = new DashBoard();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard, container, false);

        getActivity().setTitle("Inicio");

        myprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressingTextView = (TextView)view.findViewById(R.id.progress_circle_text);
        VentasCompletadas = (TextView)view.findViewById(R.id.txt_Dashboard_Ventas_Completadas);
        VentasPorCobrar = (TextView)view.findViewById(R.id.txt_Dashboard_Ventas_PorCobrar);
        PedidosPorComprar = (TextView)view.findViewById(R.id.txt_Dashboard_Pedidos_PorCobrar);
        ComprasRealizadas = (TextView)view.findViewById(R.id.txt_Dashboard_Compras_Realizadas);

        VentasCompletadas.setTypeface(font.setAsset(getContext()));
        VentasPorCobrar.setTypeface(font.setAsset(getContext()));
        PedidosPorComprar.setTypeface(font.setAsset(getContext()));

        myprogressBar.setProgress(20);
        progressingTextView.setText("20");

        /*new Thread(new Runnable() {
            public void run() {
                while (i < 100) {
                    i += 2;
                    progressHandler.post(new Runnable() {
                        public void run() {
                            myprogressBar.setProgress(i);
                            progressingTextView.setText("" + i);
                        }
                    });
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
