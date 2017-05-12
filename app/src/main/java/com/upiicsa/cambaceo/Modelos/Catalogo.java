package com.upiicsa.cambaceo.Modelos;

import java.io.Serializable;

/**
 * Created by luis.perez on 28/09/2016.
 */

public class Catalogo implements Serializable {

    public String Nombre;
    public int IDREG;

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getIDREG() {
        return IDREG;
    }

    public void setIDREG(int IDREG) {
        this.IDREG = IDREG;
    }
}
