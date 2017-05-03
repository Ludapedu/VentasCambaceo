package com.upiicsa.cambaceo.Main;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.upiicsa.cambaceo.ImageSaver.ImageSaver;
import com.upiicsa.cambaceo.InternetConnection.InternetConection;
import com.upiicsa.cambaceo.R;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        Pedidos.OnFragmentInteractionListener,
        Compras.OnFragmentInteractionListener,
        Ventas.OnFragmentInteractionListener,
        Pagos.OnFragmentInteractionListener,
        Cambios.OnFragmentInteractionListener,
        Clientes.OnFragmentInteractionListener,
        DashBoard.OnFragmentInteractionListener,
        Entregas.OnFragmentInteractionListener {

    InternetConection internet = new InternetConection(this);
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editSharedPreferences;
    ImageSaver imgen = new ImageSaver();

    public final int RC_SIGN_IN = 500;

    public GoogleApiClient mGoogleApiCliente;
    public GoogleSignInOptions gso;

    Bitmap im = null;
    String url;

    TextView NombreGoogle;
    TextView CorreoGoogle;
    TextView LeyendaControlVentas;
    RoundedImageView FotoGoogle;
    SignInButton signinButton;
    ProgressBar progreslogin;
    ImageButton logout;

    public IInAppBillingService mService;
    ServiceConnection mServiceConn;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = DashBoard.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        mGoogleApiCliente = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        NombreGoogle = (TextView) headerLayout.findViewById(R.id.Nombre_Google);
        CorreoGoogle = (TextView) headerLayout.findViewById(R.id.Correo_Google);
        FotoGoogle = (RoundedImageView) headerLayout.findViewById(R.id.imageView_Google);
        LeyendaControlVentas = (TextView) headerLayout.findViewById(R.id.Menu_ControlDeVentas);
        progreslogin = (ProgressBar) headerLayout.findViewById(R.id.progres_login);
        logout = (ImageButton) headerLayout.findViewById(R.id.logout);

        FotoGoogle.setVisibility(View.GONE);
        LeyendaControlVentas.setVisibility(View.GONE);
        progreslogin.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
        signinButton = (SignInButton) headerLayout.findViewById(R.id.sign_in_button);
        signinButton.setSize(SignInButton.SIZE_STANDARD);
        signinButton.setScopes(gso.getScopeArray());
        signinButton.setVisibility(View.INVISIBLE);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet.IsConnected()) {
                    signIn();
                    signinButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Se requiere una conexión a internet para acceder", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }
        };

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        VerificarAccesoGoogle();

    }

    private void VerificarAccesoGoogle() {
        sharedPreferences = getSharedPreferences("Google", Context.MODE_PRIVATE);
        String IdGoogle = sharedPreferences.getString("IdGoogle", "");
        if (IdGoogle.isEmpty()) {
            signinButton.setVisibility(View.VISIBLE);
        } else {
            logout.setVisibility(View.VISIBLE);
            signinButton.setVisibility(View.GONE);
            progreslogin.setVisibility(View.GONE);
            LeyendaControlVentas.setVisibility(View.VISIBLE);
            NombreGoogle.setText(sharedPreferences.getString("NombreGoogle", ""));
            CorreoGoogle.setText(sharedPreferences.getString("CorreoGoogle", ""));
            String RutaImagen = sharedPreferences.getString("RutaImagenGoogle", "");
            if (RutaImagen.isEmpty()) {
                url = sharedPreferences.getString("PhotoURL", "");
                if (!url.isEmpty())
                    ObtenerImagenGoogle();
            }
            Bitmap bit = imgen.ObtenerImagen(RutaImagen);
            if (bit != null) {
                FotoGoogle.setImageBitmap(bit);
                FotoGoogle.setVisibility(View.VISIBLE);
            } else {
                FotoGoogle.setImageDrawable(getDrawable(R.drawable.ic_account_circle_white_24dp));
                FotoGoogle.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_delete) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiCliente);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progreslogin.setVisibility(View.VISIBLE);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiCliente).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("¿Deseas cerrar sesión de Google?")
                                .setTitle("Confirmar")
                                .setCancelable(false)
                                .setNegativeButton("Cancelar",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                .setPositiveButton("Confirmar",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sharedPreferences = getSharedPreferences("Google", Context.MODE_PRIVATE);
                                                editSharedPreferences = sharedPreferences.edit();
                                                editSharedPreferences.clear();
                                                editSharedPreferences.commit();

                                                logout.setVisibility(View.GONE);
                                                LeyendaControlVentas.setVisibility(View.GONE);
                                                NombreGoogle.setText("Menú principal");
                                                CorreoGoogle.setText("Bienvenido a tu control de ventas");
                                                signinButton.setVisibility(View.VISIBLE);
                                                FotoGoogle.setVisibility(View.GONE);
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {


            logout.setVisibility(View.VISIBLE);
            progreslogin.setVisibility(View.GONE);
            FotoGoogle.setVisibility(View.VISIBLE);
            LeyendaControlVentas.setVisibility(View.VISIBLE);

            GoogleSignInAccount acct = result.getSignInAccount();

            if (acct.getPhotoUrl() != null) {
                url = acct.getPhotoUrl().toString();
                ObtenerImagenGoogle();
            } else {
                FotoGoogle.setImageDrawable(getDrawable(R.drawable.ic_account_circle_white_24dp));
            }
            NombreGoogle.setText(acct.getDisplayName());
            CorreoGoogle.setText(acct.getEmail());

            sharedPreferences = getSharedPreferences("Google", Context.MODE_PRIVATE);
            editSharedPreferences = sharedPreferences.edit();
            editSharedPreferences.putString("PhotoURL", url);
            editSharedPreferences.putString("NombreGoogle", acct.getDisplayName());
            editSharedPreferences.putString("CorreoGoogle", acct.getEmail());
            editSharedPreferences.putString("TokenGoogle", acct.getIdToken());
            editSharedPreferences.putString("IdGoogle", acct.getId());
            editSharedPreferences.commit();
        } else {
            signinButton.setVisibility(View.VISIBLE);
        }
    }

    private void ObtenerImagenGoogle() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    im = new GetImageFromUrl().execute(url).get();
                    if (im != null) {
                        FotoGoogle.setImageBitmap(im);
                        FotoGoogle.setVisibility(View.VISIBLE);
                        String rutaimagen = imgen.Guardar(im, "GooglePhoto");
                        editSharedPreferences = sharedPreferences.edit();
                        editSharedPreferences.putString("RutaImagenGoogle", rutaimagen);
                        editSharedPreferences.commit();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        View contentView = this.findViewById(android.R.id.content).getRootView();
        if (contentView.isInEditMode()) {
            return true;
        }

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.pedidos) {
            fragmentClass = Pedidos.class;
        } else if (id == R.id.compras) {
            fragmentClass = Compras.class;
        } else if (id == R.id.ventas) {
            fragmentClass = Ventas.class;
        } else if (id == R.id.pagos) {
            fragmentClass = Pagos.class;
        } else if (id == R.id.cambios) {
            fragmentClass = Cambios.class;
        } else if (id == R.id.clientes) {
            fragmentClass = Clientes.class;
        } else if (id == R.id.inicio) {
            fragmentClass = DashBoard.class;
        } else if (id == R.id.entregas) {
            fragmentClass = Entregas.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
