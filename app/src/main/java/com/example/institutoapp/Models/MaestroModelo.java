package com.example.institutoapp.Models;

public class MaestroModelo {
    private String id_institucional;
    private String nombre;
    private String id_firebase;
    private String email;
    private String imagen;



    public MaestroModelo() {
    }
    public MaestroModelo(String id_institucional, String nombre, String id_firebase, String email,String imagen) {
        this.id_institucional = id_institucional;
        this.nombre = nombre;
        this.email=email;
        this.id_firebase=id_firebase;
        this.imagen=imagen;
    }

    public String getId_institucional() {
        return id_institucional;
    }

    public void setId_institucional(String id_institucional) {
        this.id_institucional = id_institucional;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }





    public String getId_firebase() {
        return id_firebase;
    }

    public void setId_firebase(String id_firebase) {
        this.id_firebase = id_firebase;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
