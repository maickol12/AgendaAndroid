package com.example.examenmavi.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.examenmavi.Adaptadores.rvAgendaAdapter;
import com.example.examenmavi.Model.agenda;
import com.example.examenmavi.R;
import com.example.examenmavi.db.database;
import com.example.examenmavi.eventos.ClickListener;
import com.example.examenmavi.eventos.RecyclerTouchListener;

import java.util.ArrayList;

public class AgendaListFragment extends Fragment {
    private RecyclerView rvAgenda;
    private FloatingActionButton fButton;
    private rvAgendaAdapter adapter;

    private database db;
    private int idAgenda = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.agenda_list_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvAgenda = view.findViewById(R.id.rvAgenda);
        fButton  = view.findViewById(R.id.FBAgregar);
        initList();

        initEventFloatingButton();
    }
    private ArrayList<agenda> lista;
    private void initList(){
        new asyncLoadInfo(getProgressBar("Espera","Cargando información")).execute();
    }
    private void initRecycler(){
        LinearLayoutManager lim = new LinearLayoutManager(getContext());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        rvAgenda.setLayoutManager(lim);
        adapter = new rvAgendaAdapter(getContext(),lista);
        rvAgenda.setAdapter(adapter);
    }
    private void initEvent(){
        rvAgenda.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvAgenda, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mostrarInformacionAgendaOEditar(position,false);
            }

            @Override
            public void onLongClick(View view, int position) {
                mostrarInformacionAgendaOEditar(position,true);
            }
        }));
    }


    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View viewRoot = inflater.inflate(R.layout.agregar_agenda, null);
        final EditText etNombre,etTelefono,etCumpleanios,etNota;
        Button agregarAgenda         = viewRoot.findViewById(R.id.agregarAgenda);
        agregarAgenda.setText("Agregar");
        agregarAgenda.setEnabled(true);
        etNombre                     = viewRoot.findViewById(R.id.nombre);
        etTelefono                   = viewRoot.findViewById(R.id.telefono);
        etCumpleanios                 = viewRoot.findViewById(R.id.cumpleanios);
        etNota                       = viewRoot.findViewById(R.id.nota);

        agregarAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAgendaDbLocal(etNombre.getText().toString(),etTelefono.getText().toString(),etCumpleanios.getText().toString(),etNota.getText().toString());
            }
        });

        builder.setView(viewRoot);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnCerrar             = viewRoot.findViewById(R.id.cerrar);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
    private void guardarAgendaDbLocal(String nombre,String telefono,String cumpleanios,String nota){
        ContentValues cv = new ContentValues();
        cv.put("vNombre",nombre);
        cv.put("vTelefono",telefono);
        cv.put("vCumpleanios",cumpleanios);
        cv.put("vNota",nota);
        long i = 0;
        if(idAgenda == 0){
            i = db.getWritableDatabase().insert("tblAgenda",null,cv);
        }else{
            i = db.getWritableDatabase().update("tblAgenda",cv,"idAgenda = "+idAgenda,null);
        }

        if(i > 0){
            Toast.makeText(getContext(),"Guardado con exito",Toast.LENGTH_LONG).show();
            agenda row = new agenda();
            row.setNombre(nombre);
            row.setTelefono(telefono);
            row.setCumpleanios(cumpleanios);
            row.setNota(nota);
            lista.removeAll(lista);
            obtenerRegistrosGuardados();
            adapter.notifyDataSetChanged();

        }else{
            Toast.makeText(getContext(),"Ocurrio un error al guardar",Toast.LENGTH_LONG).show();
        }
    }
    private void obtenerRegistrosGuardados(){


        SQLiteDatabase reader = db.getReadableDatabase();
        String sql = "" +
                    "SELECT " +
                        "idAgenda," +
                        "vNombre,"+
                        "vTelefono,"+
                        "vCumpleanios,"+
                        "vNota " +
                     "FROM tblAgenda";
        Cursor c = reader.rawQuery(sql,null,null);

        if(c.moveToFirst()){
            do{
                agenda row = new agenda();
                row.setIdAgenda(c.getInt(c.getColumnIndex("idAgenda")));
                row.setNombre(c.getString(c.getColumnIndex("vNombre")));
                row.setTelefono(c.getString(c.getColumnIndex("vTelefono")));
                row.setCumpleanios(c.getString(c.getColumnIndex("vCumpleanios")));
                row.setNota(c.getString(c.getColumnIndex("vNota")));
                lista.add(row);
            }while (c.moveToNext());
        }



    }
    private void mostrarInformacionAgendaOEditar(int position,boolean flag){
        agenda row = lista.get(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View viewRoot = inflater.inflate(R.layout.agregar_agenda, null);
        final EditText etNombre,etTelefono,etCumpleanios,etNota;

        Button agregarAgenda         = viewRoot.findViewById(R.id.agregarAgenda);
        if(flag){
            agregarAgenda.setEnabled(true);
            idAgenda = row.getIdAgenda();
            agregarAgenda.setText("Editar");
        }else{
            agregarAgenda.setEnabled(false);
        }

        etNombre                     = viewRoot.findViewById(R.id.nombre);
        etTelefono                   = viewRoot.findViewById(R.id.telefono);
        etCumpleanios                 = viewRoot.findViewById(R.id.cumpleanios);
        etNota                       = viewRoot.findViewById(R.id.nota);

        etNombre.setText(row.getNombre());
        etTelefono.setText(row.getTelefono());
        etCumpleanios.setText(row.getCumpleanios());
        etNota.setText(row.getNota());

        builder.setView(viewRoot);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnCerrar             = viewRoot.findViewById(R.id.cerrar);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                idAgenda = 0;
            }
        });
        agregarAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAgendaDbLocal(etNombre.getText().toString(),etTelefono.getText().toString(),etCumpleanios.getText().toString(),etNota.getText().toString());
            }
        });
    }
    public void initEventFloatingButton(){

        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private class asyncLoadInfo extends AsyncTask<Void,Void,Void>{
        private ProgressDialog progressDialog;
        public asyncLoadInfo(ProgressDialog dialog){
            this.progressDialog = dialog;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            lista = new ArrayList<>();
            if(db == null){
                db = new database(getContext(),"agenda",null,1);
            }
            obtenerRegistrosGuardados();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.progressDialog.dismiss();
            initRecycler();
            initEvent();
        }
    }
    public ProgressDialog getProgressBar(String titulo,String mensaje){
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage(mensaje);
        dialog.setTitle(titulo);
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.setCancelable(false);
        return dialog;
    }
}
