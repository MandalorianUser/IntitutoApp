package com.example.institutoapp.Providers;

import com.example.institutoapp.Models.ReporteModelo;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ReporteProvider {
    DatabaseReference mDatabase;
    public ReporteProvider(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Reportes");

    }

    public Task<Void> create(ReporteModelo reporteModelo){

        return mDatabase.child(reporteModelo.getIdReporte()).setValue(reporteModelo);
    }

    public DatabaseReference getReportes(String maestroId){
        return mDatabase.child(maestroId);
    }

    public DatabaseReference getReporte(String reporteId){return  mDatabase.child(reporteId);}
    public DatabaseReference getReportesAlumno(String idAlumno){return  mDatabase.child(idAlumno);}

    public Task<Void> updateStatus(String idReporte,String status){
        if (status.isEmpty()) return null;
        Map<String,Object> map = new HashMap<>();
        map.put("status",status);
        return  mDatabase.child(idReporte).updateChildren(map);
    }
}
