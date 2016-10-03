package com.psycho.controlventas.Modelos;

/**
 * Created by luis.perez on 28/09/2016.
 */

public class Catalogo {

    public Catalogo(String nombre){
        this.Nombre = nombre;
    }

    public String Nombre;

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
