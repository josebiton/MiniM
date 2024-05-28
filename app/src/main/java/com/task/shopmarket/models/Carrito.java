package com.task.shopmarket.models;

public class Carrito {
    private final int id;
    private String nombre;
    private String precio;
    private int cantidad;
    private String imagen;

    public Carrito(int id, String nombre, String precio, int cantidad, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getImagen() {
        return imagen;
    }
}
