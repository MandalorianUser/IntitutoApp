package com.example.institutoapp.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.institutoapp.Models.ReporteModelo;
import com.example.institutoapp.Providers.AlumnoProvider;
import com.example.institutoapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RecylerViewFirebasePrincipalMaestro extends FirebaseRecyclerAdapter<ReporteModelo, RecylerViewFirebasePrincipalMaestro.ViewHolder> {

    private AlumnoProvider mAlumnoProvider;
    private Context mContext;

    public RecylerViewFirebasePrincipalMaestro(FirebaseRecyclerOptions<ReporteModelo> options, Context context){

        super(options);
        mContext = context;
        mAlumnoProvider =  new AlumnoProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ReporteModelo model) {
        holder.lblAlumnoReportado.setText("Matricula: "+model.getAlumno_id());
        holder.lblFechaReporte.setText(model.getFecha());
        holder.lblstatusReporte.setText("Estatus: "+model.getStatus());
        mAlumnoProvider.getUser(model.getAlumno_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    // String imgAlumno = snapshot.child("imagen").getValue().toString();
                    // Picasso.with(mContext).load(imgAlumno).into(holder.imageViewAlumnoreportado);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte,parent,false);

            return new ViewHolder(view);
        }catch (Exception e){
            System.out.println("Excepcion we" +e);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte,parent,false);

            return new ViewHolder(view);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView lblAlumnoReportado,lblFechaReporte,lblstatusReporte;
        private ImageView imageViewAlumnoreportado;
        private CardView cardViewAlumno;

        public ViewHolder(View view){
            super(view);
            try {
                lblAlumnoReportado = view.findViewById(R.id.item_reporte_lblIdAlumnoReportado);
                lblFechaReporte = view.findViewById(R.id.item_reporte_lblFecha);
                lblstatusReporte = view.findViewById(R.id.item_reporte_lblStatusReporte);
                imageViewAlumnoreportado = view.findViewById(R.id.item_reporte_imgAlumnoReportado);
                cardViewAlumno = view.findViewById(R.id.item_reporte_cardView);
            }catch (Exception e){
                System.out.println("Excepcion "+e);
            }

        }
    }
}
