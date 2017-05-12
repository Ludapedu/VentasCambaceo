package com.upiicsa.cambaceo.Main;

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

import com.upiicsa.cambaceo.Assets.Font;
import com.upiicsa.cambaceo.R;


public class DashBoard extends Fragment {

    public int a = 0;
    public int b = 0;
    public int c = 0;
    public int d = 0;
    Handler progressHandler = new Handler();
    ProgressBar progressVentas, progressVentasPorCobrar, progressMontoRecabado, progressMontoPendiente;
    TextView TextViewProgresoVentas, TextViewProgresoVentasPorCobrar, TextViewProgresoMontoRecabado, TextViewProgresoMontoPendiente;
    TextView txtVentas;
    TextView txtVentasPorCobrar;
    TextView txtMontoRecabado;
    TextView txtMontoPendiente;
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


        progressVentas = (ProgressBar) view.findViewById(R.id.progressBarVentas);
        progressVentasPorCobrar = (ProgressBar) view.findViewById(R.id.progressBarVentasPendientes);
        progressMontoPendiente = (ProgressBar) view.findViewById(R.id.progressBarMontoPendiente);
        progressMontoRecabado = (ProgressBar) view.findViewById(R.id.progressBarMontoAbonado);
        TextViewProgresoVentas = (TextView)view.findViewById(R.id.progresoVentas);
        TextViewProgresoVentasPorCobrar = (TextView)view.findViewById(R.id.progresoVentasPorCobrar);
        TextViewProgresoMontoRecabado = (TextView)view.findViewById(R.id.progresoMontoRecabado);
        TextViewProgresoMontoPendiente = (TextView)view.findViewById(R.id.progresoMontoPendiente);
        txtVentas = (TextView)view.findViewById(R.id.txt_Dashboard_Ventas_Completadas);
        txtVentasPorCobrar = (TextView)view.findViewById(R.id.txt_Dashboard_Ventas_PorCobrar);
        txtMontoPendiente = (TextView)view.findViewById(R.id.txt_Dashboard_Pedidos_PorCobrar);
        txtMontoRecabado = (TextView)view.findViewById(R.id.txt_Dashboard_Compras_Realizadas);

        txtVentas.setTypeface(font.setAsset(getContext()));
        txtVentasPorCobrar.setTypeface(font.setAsset(getContext()));
        txtMontoRecabado.setTypeface(font.setAsset(getContext()));
        txtMontoPendiente.setTypeface(font.setAsset(getContext()));

        progressVentas.setProgress(0);
        progressVentasPorCobrar.setProgress(0);
        progressMontoRecabado.setProgress(0);
        progressMontoPendiente.setProgress(0);

        progressVentas.setMax(50);
        progressVentasPorCobrar.setMax(50);
        progressMontoRecabado.setMax(2000);
        progressMontoPendiente.setMax(2000);

        new Thread(new Runnable() {
            public void run() {
                while (a < 50) {
                    a += 1;
                    progressHandler.post(new Runnable() {
                        public void run() {
                            progressVentas.setProgress(a);
                            TextViewProgresoVentas.setText("" + a);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (b < 10) {
                    b += 1;
                    progressHandler.post(new Runnable() {
                        public void run() {
                            progressVentasPorCobrar.setProgress(b);
                            TextViewProgresoVentasPorCobrar.setText("" + b);
                        }
                    });
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (c < 200) {
                    c += 10;
                    progressHandler.post(new Runnable() {
                        public void run() {
                            progressMontoRecabado.setProgress(c);
                            TextViewProgresoMontoRecabado.setText("$" + c);
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (d < 1800) {
                    d += 10;
                    progressHandler.post(new Runnable() {
                        public void run() {
                            progressMontoPendiente.setProgress(d);
                            TextViewProgresoMontoPendiente.setText("$" + d);
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

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
