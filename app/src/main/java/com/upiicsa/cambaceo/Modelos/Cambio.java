package com.upiicsa.cambaceo.Modelos;

import java.io.Serializable;

/**
 * Created by luis.perez on 07/10/2016.
 */

public class Cambio implements Serializable {

    public  String Catalogo;
    public  String Marca;
    public  int IDREG;
    public  int ID;
    public  int Pagina;
    public  float Numero;
    public  int Costo;
    public  int Precio;
    public  int IdCliente;
    public  String Cliente;
    public int IDREGVenta;

    public int getIDREGVenta() {
        return IDREGVenta;
    }

    public void setIDREGVenta(int IDREGVenta) {
        this.IDREGVenta = IDREGVenta;
    }






    public int getIDREG() {
        return IDREG;
    }

    public void setIDREG(int IDREG) {
        this.IDREG = IDREG;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String nombreCliente) {
        Cliente = nombreCliente;
    }

    public String getCatalogo() {
        return Catalogo;
    }

    public void setCatalogo(String catalogo) {
        Catalogo = catalogo;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPagina() {
        return Pagina;
    }

    public void setPagina(int pagina) {
        Pagina = pagina;
    }

    public float getNumero() {
        return Numero;
    }

    public void setNumero(float numero) {
        Numero = numero;
    }

    public int getCosto() {
        return Costo;
    }

    public void setCosto(int costo) {
        Costo = costo;
    }

    public int getPrecio() {
        return Precio;
    }

    public void setPrecio(int precio) {
        Precio = precio;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
    }
}

