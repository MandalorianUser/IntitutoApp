package com.example.institutoapp.Models;

import java.io.Serializable;

public class ReporteModelo implements Serializable {
    private String id,alumno_id,maestro_id,asignatura_id,tipo,descripcion,fecha,gravedad,status,idReporte,idPadre;

    public ReporteModelo() {
    }

    public ReporteModelo(String id, String alumno_id, String maestro_id, String asignatura_id, String tipo, String descripcion, String fecha, String gravedad,String status,String idReporte,String idPadre) {
        this.id = id;
        this.idReporte=idReporte;
        this.idPadre = idPadre;
        this.status=status;
        this.alumno_id = alumno_id;
        this.maestro_id = maestro_id;
        this.asignatura_id = asignatura_id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.gravedad = gravedad;
    }

    public String getTipo() {
        return tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlumno_id() {
        return alumno_id;
    }

    public void setAlumno_id(String alumno_id) {
        this.alumno_id = alumno_id;
    }

    public String getMaestro_id() {
        return maestro_id;
    }

    public void setMaestro_id(String maestro_id) {
        this.maestro_id = maestro_id;
    }

    public String getAsignatura_id() {
        return asignatura_id;
    }

    public void setAsignatura_id(String asignatura_id) {
        this.asignatura_id = asignatura_id;
    }

    public String getTipo(String tipoReporte) {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


    public String getGravedad() {
        return gravedad;
    }

    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }

    public String getIdReporte() {
        return idReporte;
    }

    public String getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(String idPadre) {
        this.idPadre = idPadre;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }


    @Override
    public String toString() {
        return "ReporteModelo{" +
                "id='" + id + '\'' +
                ", alumno_id='" + alumno_id + '\'' +
                ", maestro_id='" + maestro_id + '\'' +
                ", asignatura_id='" + asignatura_id + '\'' +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", gravedad='" + gravedad + '\'' +
                ", status='" + status + '\'' +
                ", idReporte='" + idReporte + '\'' +
                ", idPadre='" + idPadre + '\'' +
                '}';
    }
}
