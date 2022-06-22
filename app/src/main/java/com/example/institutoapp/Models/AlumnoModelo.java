package com.example.institutoapp.Models;

import java.io.Serializable;

public class AlumnoModelo implements Serializable {
    private String curp,escolaridad_id,grupo,grupo_id,id,img, nombre, padre_id;

    public AlumnoModelo() {
    }

    public AlumnoModelo(String curp, String escolaridad_id, String grupo, String grupo_id, String id, String img, String nombre, String padre_id) {
        this.curp = curp;
        this.escolaridad_id = escolaridad_id;
        this.grupo = grupo;
        this.grupo_id = grupo_id;
        this.id = id;
        this.img = img;
        this.nombre = nombre;
        this.padre_id = padre_id;
    }

    public AlumnoModelo(AlumnoModelo alumnoModelo) {
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

    public String getGrupo_id() {
        return grupo_id;
    }

    public void setGrupo_id(String grupo_id) {
        this.grupo_id = grupo_id;
    }

    public String getEscolaridad_id() {
        return escolaridad_id;
    }

    public void setEscolaridad_id(String escolaridad_id) {
        this.escolaridad_id = escolaridad_id;
    }

    public String getPadre_id() {
        return padre_id;
    }

    public void setPadre_id(String padre_id) {
        this.padre_id = padre_id;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    @Override
    public String toString() {
        return "AlumnoModelo{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", grupo_id='" + grupo_id + '\'' +
                ", escolaridad_id='" + escolaridad_id + '\'' +
                ", padre_id='" + padre_id + '\'' +
                ", curp='" + curp + '\'' +
                ", img='" + img + '\'' +
                ", grupo='" + grupo + '\'' +
                '}';
    }
}

