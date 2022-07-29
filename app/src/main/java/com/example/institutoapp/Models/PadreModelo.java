package com.example.institutoapp.Models;

public class PadreModelo {
    private String id,nombre,email,id_firebase,imagen,id_institucional;

    public PadreModelo() {
    }

    public PadreModelo(String id, String nombre, String email, String id_firebase, String imagen, String id_institucional) {
        this.id = id;
        this.id_institucional = id_institucional;
        this.nombre = nombre;
        this.email = email;
        this.id_firebase = id_firebase;
        this.imagen = imagen;
    }


    public String getId_institucional() {
        return id_institucional;
    }

    public void setId_institucional(String id_institucional) {
        this.id_institucional = id_institucional;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId_firebase() {
        return id_firebase;
    }

    public void setId_firebase(String id_firebase) {
        this.id_firebase = id_firebase;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "PadreModelo{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", id_firebase='" + id_firebase + '\'' +
                ", imagen='" + imagen + '\'' +
                ", id_institucional='" + id_institucional + '\'' +
                '}';
    }
}
