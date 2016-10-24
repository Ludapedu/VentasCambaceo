package com.psycho.controlventas.Modelos;

import java.io.Serializable;

/**
 * Created by luis.perez on 04/10/2016.
 */

public class Pago implements Serializable {

    int IDREG;
    String Cliente;
    int IdCliente;
    String FechaPago;
    int Monto;
    int Dia;
    int Mes;
    int Anio;

    public int getDia() {
        return Dia;
    }

    public void setDia(int dia) {
        Dia = dia;
    }

    public int getMes() {
        return Mes;
    }

    public void setMes(int mes) {
        Mes = mes;
    }

    public int getAnio() {
        return Anio;
    }

    public void setAnio(int anio) {
        Anio = anio;
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

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
    }

    public String getFechaPago() {
        return FechaPago;
    }

    public void setFechaPago(String fechaPago) {
        FechaPago = fechaPago;
    }

    public int getMonto() {
        return Monto;
    }

    public void setMonto(int monto) {
        Monto = monto;
    }

}
