package com.example.institutoapp.Providers;

import com.example.institutoapp.Models.PadreModelo;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PadreProvider {
    private final DatabaseReference mDatabase;
    private final DatabaseReference mDatabaseMatriculas;

    public PadreProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Padres");
        mDatabaseMatriculas = FirebaseDatabase.getInstance().getReference().child("Matriculas");



    }

    public Task<Void> create(PadreModelo padreModelo) {
        return mDatabase.child(padreModelo.getId_firebase()).setValue(padreModelo);
    }

    public DatabaseReference getUser(String idUser) {
        return mDatabase.child(idUser);
    }

    public Query getPadre(String idInstitucional) {

        return mDatabase.orderByChild("id_institucional").equalTo(idInstitucional);
    }

    public DatabaseReference getHijos(String idUser) {
        return mDatabase.child(idUser).child("hijos");
    }

    public Query verifyUser(String matricula){
        System.out.println( "-----------------------------------");
        System.out.println( "Matricula a buscar "+matricula);
        System.out.println( "Dentro del metodo provider ");
        System.out.println( "-----------------------------------");
        return mDatabaseMatriculas.orderByChild("id").equalTo(matricula);
    }
}
