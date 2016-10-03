package com.psycho.controlventas.Modelos;

import java.io.Serializable;

/**
 * Created by luis.perez on 29/09/2016.
 */

public class Estatus implements Serializable {

    public String Estatus;
    public int IdEstatus;

    public Estatus(String Estatus, int IdEstatus)
    {
        this.Estatus = Estatus;
        this.IdEstatus = IdEstatus;
    }

    public String getEstatus() {
        return Estatus;
    }

    public void setEstatus(String estatus) {
        Estatus = estatus;
    }

    public int getIdEstatus() {
        return IdEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        IdEstatus = idEstatus;
    }

}
