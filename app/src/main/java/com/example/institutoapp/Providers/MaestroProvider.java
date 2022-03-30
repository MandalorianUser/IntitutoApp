package com.example.institutoapp.Providers;

import com.example.institutoapp.Models.MaestroModelo;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MaestroProvider {
    DatabaseReference mDatabase,mDatabaseMatriculas;

    public MaestroProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Maestro");
        mDatabaseMatriculas = FirebaseDatabase.getInstance().getReference().child("Matriculas");

    }

    public Task<Void> create(MaestroModelo maestroModelo){
        return mDatabase.child(maestroModelo.getId_firebase()).setValue(maestroModelo);
    }
    public DatabaseReference getUser(String idUser){return mDatabase.child(idUser);}

    public Query verifyUser(String matricula){
        System.out.println( "-----------------------------------");
        System.out.println( "Matricula a buscar "+matricula);
        System.out.println( "Dentro del metodo provider ");
        System.out.println( "-----------------------------------");
        return mDatabaseMatriculas.orderByChild("id").equalTo(matricula);
    }
}


