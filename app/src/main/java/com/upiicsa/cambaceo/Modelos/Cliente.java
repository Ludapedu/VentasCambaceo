package com.upiicsa.cambaceo.Modelos;

import java.io.Serializable;

public class Cliente implements Serializable{
    public  String nombre;
    public  String apellidopaterno;
    public  String apellidomaterno;
    public  String direccion;
    public  String telefono;
    public  String IdCliente;

    public String getNombre()
    {
        return nombre;
    }
    public String getApellidoPaterno()
    {
        return apellidopaterno;
    }
    public String getApellidoMaterno()
    {
        return apellidomaterno;
    }
    public String getDireccion()
    {
        return direccion;
    }public String getTelefono()
    {
        return telefono;
    }
    public String getIdCliente()
    {
        return IdCliente;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidopaterno)
    {
        this.apellidopaterno = apellidopaterno;
    }

    public void setApellidoMaterno(String apellidomaterno)
    {
        this.apellidomaterno = apellidomaterno;
    }

    public void setDireccion(String direccion)
    {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public void setIdCliente(String IdCliente)
    {
        this.IdCliente = IdCliente;
    }
}