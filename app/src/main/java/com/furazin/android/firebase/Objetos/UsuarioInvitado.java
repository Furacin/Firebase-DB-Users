package com.furazin.android.firebase.Objetos;

/**
 * Created by manza on 10/03/2017.
 */

public class UsuarioInvitado {
    String email;
    String password;
    String user_admin;

    public UsuarioInvitado() {
    }

    public UsuarioInvitado(String email, String password, String user_admin) {
        this.email = email;
        this.password = password;
        this.user_admin = user_admin;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUser_admin() {
        return user_admin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser_admin(String user_admin) {
        this.user_admin = user_admin;
    }
}
