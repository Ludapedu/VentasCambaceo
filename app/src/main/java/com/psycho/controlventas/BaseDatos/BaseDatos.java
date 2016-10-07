package com.psycho.controlventas.BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luis.perez on 27/09/2016.
 */

public class BaseDatos extends SQLiteOpenHelper{

    String TablaClientes = "CREATE TABLE Clientes (IDREG INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Nombre text, ApellidoPaterno text, ApellidoMaterno text, Direccion text, Telefono text, IdCliente int)";
    String TablaMarcas = "CREATE TABLE Marcas (IDMARCA INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Marca text)";
    String TablaPagos = "CREATE TABLE Pagos (IDREG INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Cliente text, IdCliente int, Fecha text, Monto int)";
    String TablaVentas = "CREATE TABLE Ventas (IDREG INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Cliente text, IdCliente int ,Catalogo text, Pagina int, Marca text, ID int, Numero float, Costo int, Precio int, Entregado int)";
    String TablaCambios = "CREATE TABLE Cambios (IDREG INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Cliente text, IdCliente int ,Catalogo text, Pagina int, Marca text, ID int, Numero float, Costo int, Precio int, IDREGVenta int)";
    String TablaCatalogos = "CREATE TABLE Catalogos (IDREG INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Catalogo text, IdCatalogo int)";

    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TablaClientes);
        db.execSQL(TablaMarcas);
        db.execSQL(TablaPagos);
        db.execSQL(TablaVentas);
        db.execSQL(TablaCambios);
        db.execSQL(TablaCatalogos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Clientes");
        db.execSQL("DROP TABLE IF EXISTS Marcas");
        db.execSQL("DROP TABLE IF EXISTS Pagos");
        db.execSQL("DROP TABLE IF EXISTS Ventas");
        db.execSQL("DROP TABLE IF EXISTS Cambios");
        db.execSQL("DROP TABLE IF EXISTS Catalogos");
    }
}
