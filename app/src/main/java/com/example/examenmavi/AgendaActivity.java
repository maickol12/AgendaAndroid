package com.example.examenmavi;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.examenmavi.Fragments.AgendaListFragment;

public class AgendaActivity extends AppCompatActivity {
    private FloatingActionButton fButton;
    private AgendaListFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_activity);

    }


}
