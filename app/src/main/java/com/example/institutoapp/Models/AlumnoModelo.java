package com.example.institutoapp.Models;

import java.io.Serializable;

public class AlumnoModelo implements Serializable {
    private String id, nombre, grupo_id, escolaridad_id, padre_id, curp, img, grupo;

    public AlumnoModelo() {
    }

    public AlumnoModelo(String id, String nombre, String grupo_id, String escolaridad_id, String padre_id, String curp, String img, String grupo) {
        this.id = id;
        this.nombre = nombre;
        this.grupo_id = grupo_id;
        this.escolaridad_id = escolaridad_id;
        this.padre_id = padre_id;
        this.curp = curp;
        this.img = img;
        this.grupo = grupo;
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
}

