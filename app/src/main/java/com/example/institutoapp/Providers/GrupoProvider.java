package com.example.institutoapp.Providers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GrupoProvider {
    DatabaseReference mDatabase;

    public GrupoProvider(){
        mDatabase =
                FirebaseDatabase.getInstance().getReference().child("Grupos");
    }

    public DatabaseReference GetGrupos(String Escolaridad){
        if (Escolaridad==null || Escolaridad==""){

        }
        return mDatabase.child(""+Escolaridad+"");

    }
}
