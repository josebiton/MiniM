package com.task.shopmarket.models;

public class Direccion {
    private int idDireccion;
    private String direccion;
    private String referencia;

    public Direccion(int idDireccion, String direccion, String referencia) {
        this.idDireccion = idDireccion;
        this.direccion = direccion;
        this.referencia = referencia;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    @Override
    public String toString() {
        return direccion + " - " + referencia;
    }
}

