package com.upiicsa.cambaceo.Main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.upiicsa.cambaceo.Adaptadores.AdaptadorSimpleCatalogo;
import com.upiicsa.cambaceo.AsynkTask.getCatalogos;
import com.upiicsa.cambaceo.Modelos.Catalogo;
import com.upiicsa.cambaceo.R;

import java.util.ArrayList;

public class Catalogos extends Fragment implements SearchView.OnQueryTextListener {

    private final int EDIT_CATALOGO = 60;
    private final int ADD_NEW_CATALOG = 100;

    ListView ListView_Catalogos;
    ArrayList<Catalogo> listaCatalogos = new ArrayList<Catalogo>();
    Catalogo RegistroCatalogo;
    Catalogo CatalogoSeleccionado;
    private AdaptadorSimpleCatalogo catalogoAdapter;
    private BroadcastReceiver receiverCatalogos;
    private IntentFilter filtroCatalogos = new IntentFilter();
    private getCatalogos obtenerCatalogos;
    private OnFragmentInteractionListener mListener;

    public Catalogos() {
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getActivity().registerReceiver(receiverCatalogos, filtroCatalogos);
            obtenerCatalogos = new getCatalogos(getActivity());
            obtenerCatalogos.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Catalogos newInstance(String param1, String param2) {
        Catalogos fragment = new Catalogos();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalogos, container, false);
        getActivity().setTitle("Catalogos");

        BroadCastReceiverCatalogos();
        getActivity().registerReceiver(receiverCatalogos, filtroCatalogos);
        obtenerCatalogos = new getCatalogos(getActivity());
        obtenerCatalogos.execute();

        ListView_Catalogos = (ListView) view.findViewById(R.id.Lista_Catalogos);
        ActualizarListView();

        ListView_Catalogos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CatalogoSeleccionado = catalogoAdapter.getItem(i);
                Intent intent = new Intent(getActivity(),DetalleCatalogo.class);
                intent.putExtra("Catalogo", CatalogoSeleccionado);
                startActivityForResult(intent, EDIT_CATALOGO);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.agregar_catalogo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AgregarCatalogo.class);
                startActivityForResult(i,ADD_NEW_CATALOG);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_CATALOGO) {
            if (resultCode == Activity.RESULT_OK) {
                ActualizarListView();
            }
        }
        if (requestCode == ADD_NEW_CATALOG) {
            if (resultCode == Activity.RESULT_OK) {
                ActualizarListView();
            }
        }
    }

    private void BroadCastReceiverCatalogos() {
        filtroCatalogos.addAction("ListaCatalogos");

        receiverCatalogos = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ListaCatalogos")) {
                    listaCatalogos.clear();
                    listaCatalogos = (ArrayList<Catalogo>) intent.getExtras().get("ListaDeCatalogos");
                    if (getActivity() != null) {
                        catalogoAdapter = new AdaptadorSimpleCatalogo(getActivity(), listaCatalogos);
                        ListView_Catalogos.setAdapter(catalogoAdapter);
                    }
                }
            }
        };
    }

    private void ActualizarListView() {
        obtenerCatalogos = new getCatalogos(getActivity());
        obtenerCatalogos.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        SearchView searchView = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());

        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        catalogoAdapter.getFilter().filter(newText);
        return false;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public static Catalogos newInstance(String param1, String param2, Context context) {
        Catalogos fragment = new Catalogos();
        return fragment;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            getActivity().unregisterReceiver(receiverCatalogos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
