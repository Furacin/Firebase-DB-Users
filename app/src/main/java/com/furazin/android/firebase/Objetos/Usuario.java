package com.furazin.android.firebase.Objetos;

/**
 * Created by manza on 09/03/2017.
 */

public class Usuario {
    String email;
    String nombre;
    String password;

    public Usuario() {
    }

    public Usuario(String email, String nombre, String password) {
        this.email = email;
        this.nombre = nombre;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}