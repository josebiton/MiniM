package com.task.shopmarket.models;


public class Categoria {
    private int categoria_id;
    private String nombre;
    private String imagen;


   /* public Categoria(int categoria_id, String nombre, String imagen) {
        this.categoria_id = categoria_id;
        this.nombre = nombre;
        this.imagen = imagen;
    }*/

    public int getId() {
        return categoria_id;
    }
    public void setId(int categoria_id) {
        this.categoria_id = categoria_id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public String getImagen() {
        return imagen;
    }

}
