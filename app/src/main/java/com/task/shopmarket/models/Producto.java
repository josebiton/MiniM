package com.task.shopmarket.models;

import java.util.List;

public class Producto {
    private int producto_id;
    private String producto_nombre;
    private String descripcion;
    private String marca_nombre;
    private String slug;
    private String empresa_nombre;
    private String categoria_nombre;
    private String unidad_nombre;
    private String precio;
   // private String precioConMoneda;
    private String stock;
    private List<String> imagenes;

   /* public Producto(int producto_id, String producto_nombre, String descripcion, String marca_nombre, String slug, String empresa_nombre, String categoria_nombre, String unidad_nombre, double precio, String stock, List<String> imagenes) {
        this.producto_id = producto_id;
        this.producto_nombre = producto_nombre;
        this.descripcion = descripcion;
        this.marca_nombre = marca_nombre;
        this.slug = slug;
        this.empresa_nombre = empresa_nombre;
        this.categoria_nombre = categoria_nombre;
        this.unidad_nombre = unidad_nombre;
        this.precio = precio;
       // this.precioConMoneda = "S/. "+ precio;
        this.stock = stock;
        this.imagenes = imagenes;
    }*/

    public void setProducto_id(int producto_id) {
        this.producto_id = producto_id;
    }

    public void setProducto_nombre(String producto_nombre) {
        this.producto_nombre = producto_nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setMarca_nombre(String marca_nombre) {
        this.marca_nombre = marca_nombre;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setEmpresa_nombre(String empresa_nombre) {
        this.empresa_nombre = empresa_nombre;
    }

    public void setCategoria_nombre(String categoria_nombre) {
        this.categoria_nombre = categoria_nombre;
    }

    public void setUnidad_nombre(String unidad_nombre) {
        this.unidad_nombre = unidad_nombre;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public int getId() {
        return producto_id;
    }
    public String getNombre() {
        return producto_nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getMarca() {
        return marca_nombre;
    }
    public String getSlug() {
        return slug;
    }
    public String getEmpresa() {
        return empresa_nombre;
    }
    public String getCategoria() {
        return categoria_nombre;
    }
    public String getUnidad() {
        return unidad_nombre;
    }
    public String getPrecio() {
        return precio;
    }
    public String getStock() {
        return stock;
    }
    public List<String> getImagenes() {
        return imagenes;
    }

}

