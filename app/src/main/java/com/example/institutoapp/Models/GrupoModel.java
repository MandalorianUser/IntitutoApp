package com.example.institutoapp.Models;

public class GrupoModel {
    private String nombre;
    private String imagen;
    private String id;
    private String alumnos;

    public GrupoModel(String nombre, String imagen, String id, String alumnos) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.id = id;
        this.alumnos = alumnos;
    }

    public GrupoModel(){}
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(String alumnos) {
        this.alumnos = alumnos;
    }
}
