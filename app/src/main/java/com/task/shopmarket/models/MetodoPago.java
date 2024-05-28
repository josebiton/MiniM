package com.task.shopmarket.models;

public class MetodoPago {
    private int id;
    private String nombre;
    private String logo;

    public MetodoPago(int id, String nombre, String logo) {
        this.id = id;
        this.nombre = nombre;
        this.logo = logo;
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLogo() {
        return logo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


}
