package com.upiicsa.cambaceo.Modelos;

import java.io.Serializable;

/**
 * Created by luis.perez on 28/09/2016.
 */

public class Venta implements Serializable {

    public  String Catalogo;
    public  String Marca;
    public  String IDREG;
    public  int ID;
    public  String IdCatalogo;
    public  int Pagina;
    public  float Numero;
    public  int Costo;
    public  int Precio;
    public  String IdCliente;
    public  String Cliente;
    public int Entregado;
    public String Ubicacion;
    public String idMarca;

    public String getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(String idMarca) {
        this.idMarca = idMarca;
    }

    public String getIdCatalogo() {
        return IdCatalogo;
    }

    public void setIdCatalogo(String idCatalogo) {
        IdCatalogo = idCatalogo;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public int getEntregado() {
        return Entregado;
    }

    public void setEntregado(int entregado) {
        Entregado = entregado;
    }


    public String getIDREG() {
        return IDREG;
    }

    public void setIDREG(String IDREG) {
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

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }
}
