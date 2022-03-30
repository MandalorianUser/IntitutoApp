package com.example.institutoapp.Providers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlumnoProvider {
    DatabaseReference mDatabase;
    public AlumnoProvider(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Alumnos");
    }



    public DatabaseReference getUser(String idUser){return mDatabase.child(idUser);}
}
