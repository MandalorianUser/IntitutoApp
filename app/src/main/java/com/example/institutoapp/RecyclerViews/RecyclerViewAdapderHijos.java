package com.example.institutoapp.RecyclerViews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.institutoapp.Models.AlumnoModelo;
import com.example.institutoapp.Providers.ReporteProvider;
import com.example.institutoapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class RecyclerViewAdapderHijos extends FirebaseRecyclerAdapter<AlumnoModelo, RecyclerViewAdapderHijos.ViewHolder> {

    private ReporteProvider mReporteProvider;
    private Context mContext;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecyclerViewAdapderHijos(@NonNull @NotNull FirebaseRecyclerOptions<AlumnoModelo> options, Context context) {
        super(options);
        mReporteProvider = new ReporteProvider();
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull RecyclerViewAdapderHijos.ViewHolder holder, int position, @NonNull @NotNull AlumnoModelo model) {
        holder.txtNombreAlumno.setText(model.getNombre());
        holder.txtGrupoAlumno.setText(model.getGrupo());
        mReporteProvider.getReportesAlumno(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    System.out.println("Existen reportes del alumno " + model.getNombre() + " - " + snapshot);
                    System.out.println("---------------------------------------");
                    if (snapshot.hasChild("imagen")) {
                        Log.i("ImagenHijo", "TieneImagen");
                    } else {
                        Log.w("ImagenHijo", "NoTieneImagen");

                    }
                    String imagen = snapshot.child("imagen").getValue().toString();
                    Picasso.with(mContext).load(imagen).error(R.drawable.ic_user).into(holder.imgAlumno);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                System.out.println("Error al obtener los reportes del alumno");
            }
        });

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alumno_reportado, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombreAlumno, txtGrupoAlumno, txtReportes;
        private ImageView imgAlumno;
        private CardView cardViewAlumno;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtNombreAlumno = itemView.findViewById(R.id.txtNombreAlumnoItemAlumnoReportado);
            txtGrupoAlumno = itemView.findViewById(R.id.txtGrupoItemAlumnoReportado);
            imgAlumno = itemView.findViewById(R.id.imgAlumnoItemAlumnoReportado);
            txtReportes = itemView.findViewById(R.id.txtReportesItemAlumnoReportado);

            cardViewAlumno = itemView.findViewById(R.id.cardViewItemAlumnoReportado);
        }
    }
}
