package com.example.institutoapp.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.institutoapp.Models.AlumnoModelo;
import com.example.institutoapp.R;
import com.example.institutoapp.activity_hijo_detalle;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterHijosPersonalizado extends RecyclerView.Adapter<RecyclerViewAdapterHijosPersonalizado.ViewHolderAlumnos> {
    ArrayList<AlumnoModelo> alumnList;
    public Context mContext;
    AlumnoModelo a = new AlumnoModelo();

    public RecyclerViewAdapterHijosPersonalizado(ArrayList<AlumnoModelo> alumnList,Context mContext) {
        this.alumnList = alumnList;
        this.mContext = mContext;
    }

    public class ViewHolderAlumnos extends RecyclerView.ViewHolder {
        private ImageView imgAlumnoItemAlumnoP;
        private TextView txtAlumnoItemAlumnoP, txtGrupoItemALumnoP;
        private CircleImageView btnDetallesItemALumnoP;
        private CardView cardViewItemAlumnoP;


        public ViewHolderAlumnos(@NonNull View itemView) {
            super(itemView);
            imgAlumnoItemAlumnoP = itemView.findViewById(R.id.imgAlumnoItemAlumnoPersonalizado);
            txtAlumnoItemAlumnoP = itemView.findViewById(R.id.txtNombreAlumnoItemAlumnoPersonalizado);
            txtGrupoItemALumnoP  = itemView.findViewById(R.id.txtGrupoItemAlumnoPersonalizado);
            cardViewItemAlumnoP  = itemView.findViewById(R.id.cardViewItemAlumnoPersonalizado);
            btnDetallesItemALumnoP =  itemView.findViewById(R.id.btnDetallesItemAlumnoPersonalizado);


        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapterHijosPersonalizado.ViewHolderAlumnos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alumno_personalizado, null, false);
        return new ViewHolderAlumnos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterHijosPersonalizado.ViewHolderAlumnos holder, int position) {
        holder.txtGrupoItemALumnoP.setText(alumnList.get(position).getGrupo());
        holder.txtAlumnoItemAlumnoP.setText(alumnList.get(position).getNombre());
        AlumnoModelo hijo = new AlumnoModelo();
        hijo = alumnList.get(position);
        String url = alumnList.get(position).getImg();

        Glide.with(mContext)
                .load(url)
                .error(R.drawable.ic_user)
                .into(holder.imgAlumnoItemAlumnoP);

        AlumnoModelo finalHijo = hijo;
        holder.cardViewItemAlumnoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               detailALumn(finalHijo);
            }
        });
        holder.btnDetallesItemALumnoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailALumn(finalHijo);
            }
        });


    }

    @Override
    public int getItemCount() {
        return alumnList.size();
    }

    public void detailALumn(AlumnoModelo a){
        Intent i = new Intent(mContext, activity_hijo_detalle.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("hijoExtra",a);
        System.out.println("------------------------------------------");
        System.out.println("Desde el recycler "+a.toString());
        System.out.println("------------------------------------------");
        mContext.startActivity(i);
    }
}
