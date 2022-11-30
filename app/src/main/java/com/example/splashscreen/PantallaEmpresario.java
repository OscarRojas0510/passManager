package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
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
                        o[0].setAdmin(key);
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
        i.putExtra("ventana", 4);
        startActivity(i);
    }

    //boton icono busca de la barra inferior
    public void empBuscar(MenuItem item)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(PantallaEmpresario.this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_busca_emp, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ImageButton b = v.findViewById(R.id.buscaEmp_cancel);
        MaterialButton btn_busca = v.findViewById(R.id.buscaEmp_ok);
        TextInputEditText busc_cuenta = v.findViewById(R.id.buscaEmp_correo);
        TextInputEditText busc_nombre = v.findViewById(R.id.buscaEmp_nombre);

        b.setOnClickListener(v1 ->
        {
            dialog.dismiss();
        });

        btn_busca.setOnClickListener(v1 ->
        {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(PantallaEmpresario.this, "datos_busqueda_emp", null, 1);
            SQLiteDatabase bdd = admin.getWritableDatabase();
            for (int i = 0; i < cardArrayList.size(); i++)
            {
                ContentValues registro = new ContentValues();
                registro.put("id", cardArrayList.get(i).getId());
                registro.put("correo", cardArrayList.get(i).getCorreo());
                registro.put("img", cardArrayList.get(i).getImg());
                registro.put("nombre", cardArrayList.get(i).getNombre());
                registro.put("indice", i);
                bdd.insert("datos_busqueda_emp", null, registro);
            }
            Cursor fila = bdd.rawQuery("select * from datos_busqueda_emp where correo like '%" + busc_cuenta.getText().toString() + "%' and nombre like'%" + busc_nombre.getText().toString() + "%'", null);
            ArrayList<SubsObject> cardArrayList_temp = new ArrayList<>();
            if (fila.moveToFirst())
            {
                do
                {
                    SubsObject card_temp = new SubsObject();
                    card_temp = cardArrayList.get(fila.getInt(4));
                    cardArrayList_temp.add(card_temp);
                } while (fila.moveToNext());
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(PantallaEmpresario.this));
            adapterCard = new AdapterEmp(PantallaEmpresario.this, cardArrayList_temp);
            recyclerView.setAdapter(adapterCard);
            recyclerView.setItemAnimator(new CustomItemAnimation());
            bdd.delete("datos_busqueda_emp", "1=1", null);
            dialog.dismiss();
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PantallaEmpresario.this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_personalizado_imagen, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
        Toast.makeText(this, "pause", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "destroy", Toast.LENGTH_LONG).show();
        finish();
    }
}