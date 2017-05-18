package com.upiicsa.cambaceo.Modelos;

import java.io.Serializable;

/**
 * Created by C on 07/05/2017.
 */

public class Marca implements Serializable {
    public String IDREG;
    public String Marca;

    public String getIDREG() {
        return IDREG;
    }

    public void setIDREG(String IDREG) {
        this.IDREG = IDREG;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }
}
