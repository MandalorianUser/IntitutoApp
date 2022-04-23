package com.example.institutoapp.RecyclerViews;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.institutoapp.Models.ReporteModelo;
import com.example.institutoapp.R;
import com.example.institutoapp.activity_detail_reporte;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ReportesHijosAdapter extends FirebaseRecyclerAdapter<ReporteModelo, ReportesHijosAdapter.ViewHolder> {
    private Context mContext;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReportesHijosAdapter(@NonNull FirebaseRecyclerOptions<ReporteModelo> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ReporteModelo reporteModelo) {

        holder.txtMaestroReporta.setText(reporteModelo.getMaestro_id());
        holder.txtDescripcion.setText(reporteModelo.getDescripcion());
        holder.txtFecha.setText(reporteModelo.getFecha());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, activity_detail_reporte.class );
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("reporteModelo",reporteModelo);
                mContext.startActivity(i);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView txtMaestroReporta;
        private TextView txtDescripcion;
        private TextView txtFecha;

        public ViewHolder(View view) {
            super(view);

            cardView = view.findViewById(R.id.item_reporte_cardView);
            txtDescripcion = view.findViewById(R.id.item_reporte_lblStatusReporte);
            txtFecha = view.findViewById(R.id.item_reporte_lblFecha);
            txtMaestroReporta = view.findViewById(R.id.item_reporte_lblIdAlumnoReportado);
        }
    }

}
