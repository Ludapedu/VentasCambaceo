package com.upiicsa.cambaceo.Modelos;

import java.io.Serializable;

/**
 * Created by C on 07/05/2017.
 */

public class Marca implements Serializable {
    public int IDREG;
    public String Marca;

    public int getIDREG() {
        return IDREG;
    }

    public void setIDREG(int IDREG) {
        this.IDREG = IDREG;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }
}
