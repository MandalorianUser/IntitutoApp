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

import com.example.institutoapp.Models.GrupoModel;
import com.example.institutoapp.Models.ReporteModelo;
import com.example.institutoapp.R;
import com.example.institutoapp.RecyclerViews.RecyclerViewsFirebase;
import com.example.institutoapp.activity_alumnos_grupo;
import com.example.institutoapp.maestros.activity_PrincipalMaestro;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class RecyclerViewAdaptadorFirebaseReportes  extends FirebaseRecyclerAdapter<ReporteModelo, RecyclerViewsFirebase.ViewHolder>{
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     * @param activity_principalMaestro
     */
    public RecyclerViewAdaptadorFirebaseReportes(@NonNull FirebaseRecyclerOptions<ReporteModelo> options, activity_PrincipalMaestro activity_principalMaestro) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull com.example.institutoapp.RecyclerViews.RecyclerViewsFirebase.ViewHolder holder, int position, @NonNull ReporteModelo model) {

    }

    @NonNull
    @Override
    public com.example.institutoapp.RecyclerViews.RecyclerViewsFirebase.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class RecyclerViewsFirebase  extends FirebaseRecyclerAdapter<GrupoModel, com.example.institutoapp.RecyclerViews.RecyclerViewsFirebase.ViewHolder> {
        private Context mContext;
        private String Escolaridad;

        public RecyclerViewsFirebase(@NonNull FirebaseRecyclerOptions<GrupoModel> options, Context context, String Escolaridad) {
            super(options);
            mContext=context;
            this.Escolaridad =  Escolaridad;
        }

        @Override
        protected void onBindViewHolder(@NonNull com.example.institutoapp.RecyclerViews.RecyclerViewsFirebase.ViewHolder holder, int position, @NonNull final GrupoModel model) {
            try{

                // holder.lblGrupoNombre.setText(model.getNombre());
                //  Picasso.with(mContext).load(model.getImagen()).into(holder.imgGrupo);
                final String[] datos = new String[3];
                datos[0]=Escolaridad;
                datos[1]=model.getId();
                datos[2]=model.getNombre();
                //   holder.cardViewGrupo.setOnClickListener(new View.OnClickListener() {
             /*       @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, activity_alumnos_grupo.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        System.out.println("----------------------");
                        System.out.println("Datos a enviar:"+datos);
                        System.out.println("----------------------");
                        i.putExtra("Escolaridad",Escolaridad);
                        i.putExtra("GrupoId",model.getId());
                        i.putExtra("NombreGrupo",model.getNombre());
                        mContext.startActivity(i);


                    }
                });*/

            }catch (Exception e){
                System.out.println("Error en adapter "+e);
            }

        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView lblGrupoNombre;
            private ImageView imgGrupo;
            private CardView cardViewGrupo;

            public ViewHolder(View view){
                super(view);
                lblGrupoNombre = view.findViewById(R.id.lblGrupoItemGrupo);
                imgGrupo = view.findViewById(R.id.imgGrupoItemGrupo);
                cardViewGrupo  = view.findViewById(R.id.card_viewItemGrupo);



            }
        }


        @NonNull
        @Override
        public com.example.institutoapp.RecyclerViews.RecyclerViewsFirebase.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grupo,parent,false);
            //  return new com.example.login.RecyclerViewsFirebase.ViewHolder(view);
            return null;
        }


    }

}
