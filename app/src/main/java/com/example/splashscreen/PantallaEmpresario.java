package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PantallaEmpresario extends AppCompatActivity
{

    AdapterEmp adapterCard;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;
    ArrayList<SubsObject> cardArrayList;
    String key;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_empresario);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        bottomNavigationView = findViewById(R.id.bottomEmp_nav_view);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        recyclerView = findViewById(R.id.inicioEmp_Recycler);
        cardArrayList = new ArrayList<>();
        key = getIntent().getStringExtra("key");
        cargarLista();

    }

    private void cargarLista()
    {
        final SubsObject o[] = new SubsObject[1];
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("usuarios").child(key).child("subs").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    cardArrayList.clear();
                    for (DataSnapshot db : snapshot.getChildren())
                    {
                        o[0] = db.getValue(SubsObject.class);
                        o[0].setId(db.getKey());
                        cardArrayList.add(o[0]);
                    }
                    mostrarDatos();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void mostrarDatos()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(PantallaEmpresario.this));
        adapterCard = new AdapterEmp(PantallaEmpresario.this, cardArrayList);
        recyclerView.setAdapter(adapterCard);
        recyclerView.setItemAnimator(new CustomItemAnimation());
    }

    //boton icono usuario de la barra inferior
    public void empUserinf(MenuItem item)
    {
        Intent i = new Intent(PantallaEmpresario.this, DatosUsuario.class);
        i.putExtra("key", key);
        startActivity(i);
        Toast.makeText(this, "empUserInf", Toast.LENGTH_SHORT).show();
    }

    //boton icono busca de la barra inferior
    public void empBuscar(MenuItem item)
    {
        Toast.makeText(this, "empBuscar", Toast.LENGTH_SHORT).show();
    }

    //boton icono logout de la barra inferior
    public void empLogout(MenuItem item)
    {
        Intent i = new Intent(PantallaEmpresario.this, inicioSesion.class);
        startActivity(i);
        finish();
    }

    //boton icono llave de la barra inferior
    public void empPassGen(MenuItem item)
    {
        Toast.makeText(this, "empPassGen", Toast.LENGTH_SHORT).show();
    }

    //botón circular inferior
    public void addEmp(View view)
    {
        Intent i = new Intent(PantallaEmpresario.this, EmpAgregaUser.class);
        i.putExtra("key", key);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(PantallaEmpresario.this, inicioSesion.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finish();
    }
}