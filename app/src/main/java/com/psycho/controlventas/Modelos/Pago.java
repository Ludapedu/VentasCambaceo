package com.psycho.controlventas.Modelos;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by luis.perez on 04/10/2016.
 */

public class Pago implements Serializable {

    String Cliente;
    int IdCliente;
    Date FechaPago;
    int Monto;

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

    public Date getFechaPago() {
        return FechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        FechaPago = fechaPago;
    }

    public int getMonto() {
        return Monto;
    }

    public void setMonto(int monto) {
        Monto = monto;
    }

}
