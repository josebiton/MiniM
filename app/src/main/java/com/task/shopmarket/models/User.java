package com.task.shopmarket.models;

public class User {
    String id;
    String name;
    String email;
    String telefono;
    String password;

    public User() {
    }

    public User(String id, String name, String email, String telefono, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String mobile) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
