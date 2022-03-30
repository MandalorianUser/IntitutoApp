package com.example.institutoapp.RecyclerViews;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.institutoapp.Models.AlumnoModelo;
import com.example.institutoapp.R;
import com.example.institutoapp.activity_reporte;

import java.util.List;

public class RecyclerViewAdaptador extends RecyclerView.Adapter<RecyclerViewAdaptador.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView NombreAlumno,GrupoAlumno;
        ImageView imgAlumno;
        private CardView cardViewAlumno;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreAlumno=(TextView)itemView.findViewById(R.id.txtNombreAlumnoItemAlumno);
            GrupoAlumno=(TextView)itemView.findViewById(R.id.txtGrupoItemAlumno);
            imgAlumno=(ImageView)itemView.findViewById(R.id.imgAlumnoItemAlumno);
            cardViewAlumno = itemView.findViewById(R.id.cardViewItemAlumno);

        }
    }



    public List<AlumnoModelo> AlumnoLista;
    public Context mContext;
    public RecyclerViewAdaptador(List<AlumnoModelo>AlumnoLista, Context mContext){
        this.AlumnoLista =AlumnoLista;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alumno,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.NombreAlumno.setText(AlumnoLista.get(position).getNombre());
        holder.GrupoAlumno.setText(AlumnoLista.get(position).getId());
        final AlumnoModelo a = new AlumnoModelo();
        a.setId(AlumnoLista.get(position).getId());
        a.setPadre_id(AlumnoLista.get(position).getPadre_id());
        a.setGrupo_id(AlumnoLista.get(position).getGrupo_id());
        String url = AlumnoLista.get(position).getImg();
        Glide.with(mContext)
                .load(url)
                .into(holder.imgAlumno);
        holder.cardViewAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,activity_reporte.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("Alumno",  a);
                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return AlumnoLista.size();
    }
}
