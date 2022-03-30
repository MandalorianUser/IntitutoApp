package com.example.institutoapp.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.institutoapp.Models.GrupoModel;
import com.example.institutoapp.R;
import com.example.institutoapp.activity_alumnos_grupo;

import java.util.ArrayList;

public class RecyclerViewAdaptadorGrupos extends RecyclerView.Adapter<RecyclerViewAdaptadorGrupos.ViewHolder> {

    public ArrayList<GrupoModel> GrupoLista;
    Context mContext;
    String Escolaridad;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView NombreGrupo;
        ImageView imgGrupo;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreGrupo = (TextView) itemView.findViewById(R.id.lblGrupoItemGrupo);
            imgGrupo = (ImageView) itemView.findViewById(R.id.imgGrupoItemGrupo);


        }
    }




    public RecyclerViewAdaptadorGrupos(ArrayList<GrupoModel> GrupoLista, Context context, String escolaridad) {
        this.GrupoLista = GrupoLista;
        mContext = context;
        Escolaridad = escolaridad;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grupo, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.NombreGrupo.setText(GrupoLista.get(position).getNombre());
        final String nombre = GrupoLista.get(position).getNombre();
        String url = GrupoLista.get(position).getImagen();
        final String grupoId = GrupoLista.get(position).getId();
        Glide.with(mContext)
                .load(url)
                .into(holder.imgGrupo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext,activity_alumnos_grupo.class);
                i.putExtra("Escolaridad",Escolaridad);
                i.putExtra("GrupoId",grupoId);
                i.putExtra("NombreGrupo",nombre);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return GrupoLista.size();
    }
}