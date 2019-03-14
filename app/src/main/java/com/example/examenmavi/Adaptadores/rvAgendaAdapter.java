package com.example.examenmavi.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.examenmavi.Model.agenda;
import com.example.examenmavi.R;

import java.util.ArrayList;

public class rvAgendaAdapter extends RecyclerView.Adapter<rvAgendaAdapter.rvAgendaHolder> {
    private ArrayList<agenda> listaAgenda;
    private Context context;
    public rvAgendaAdapter(Context context,ArrayList<agenda> agenda){
        this.context        = context;
        this.listaAgenda    = agenda;
    }
    public static class rvAgendaHolder extends RecyclerView.ViewHolder{
        private TextView tvNombre,tvTelefono,tvCumpleanios,tvNota;
        public rvAgendaHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre        = itemView.findViewById(R.id.nombre);
            tvTelefono      = itemView.findViewById(R.id.telefono);
            tvCumpleanios   = itemView.findViewById(R.id.cumpleanios);
            tvNota          = itemView.findViewById(R.id.nota);
        }
    }
    @NonNull
    @Override
    public rvAgendaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_agenda,viewGroup,false);
        rvAgendaHolder holder = new rvAgendaHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull rvAgendaHolder rvAgendaHolder, int i) {
        agenda row = listaAgenda.get(i);
        rvAgendaHolder.tvNombre.setText(row.getNombre());
        rvAgendaHolder.tvTelefono.setText(row.getTelefono());
        rvAgendaHolder.tvCumpleanios.setText(row.getCumpleanios());
        rvAgendaHolder.tvNota.setText(row.getNota());
    }
    @Override
    public int getItemCount() {
        int size = 0;
        try{
            size = listaAgenda.size();
        }catch (Exception e){
            size = 0;
        }
        return size;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
