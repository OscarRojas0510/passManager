package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PantallaInicio extends AppCompatActivity
{
    AdapterCard adapterCard;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;
    ArrayList<Card> cardArrayList;
    String key;
    int ventana;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantalla_inicio);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        recyclerView = findViewById(R.id.inicio_Recycler);
        cardArrayList = new ArrayList<>();
        key = getIntent().getStringExtra("key");
        ventana = 0;
        ventana = getIntent().getIntExtra("ventana", 0);
        cargarLista();


    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    private void mostrarDatos()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(PantallaInicio.this));
        adapterCard = new AdapterCard(PantallaInicio.this, cardArrayList);
        recyclerView.setAdapter(adapterCard);
        recyclerView.setItemAnimator(new CustomItemAnimation());
    }

    private void cargarLista()
    {
        final Card c[] = new Card[1];
        final PasswordObj[] o = new PasswordObj[1];
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("usuarios").child(key).child("passwords").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(PantallaInicio.this, "contrasenias", null, 1);
                    SQLiteDatabase bdd = admin.getWritableDatabase();
                    bdd.delete("contrasenias", "1=1", null);

                    cardArrayList.clear();
                    for (DataSnapshot db : snapshot.getChildren())
                    {
                        o[0] = db.getValue(PasswordObj.class);
                        String ky = db.getKey();
                        c[0] = new Card();
                        c[0].setId(ky);
                        c[0].setId_user(key);
                        c[0].setCuenta(o[0].getCuenta());
                        c[0].setFecha_creación(o[0].getFecha_load());
                        c[0].setFecha_ultimo_uso(o[0].getFecha_ultimo_uso());
                        c[0].setImagenFireBase(o[0].getImg());
                        c[0].setPassword(EncriptarTexto.desencriptar(o[0].getPass()));
                        c[0].setUser(o[0].getUser());
                        cardArrayList.add(c[0]);

                        ContentValues registro = new ContentValues();
                        registro.put("id", ky);
                        registro.put("pass", c[0].getPassword());
                        bdd.insert("contrasenias", null, registro);
                    }
                    //finish();
                    //mostrardatos
                    mostrarDatos();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }


    public void add(View view)
    {
        finish();
        Intent intent = new Intent(PantallaInicio.this, Agregar.class);
        intent.putExtra("key", key);
        startActivity(intent);
    }

    public void buscar(MenuItem item)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(PantallaInicio.this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_personalizado2, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ImageButton b = v.findViewById(R.id.busca_cancel);

        MaterialButton btn_busca = v.findViewById(R.id.busca_ok);
        TextInputEditText busc_cuenta = v.findViewById(R.id.busca_cuenta);

        b.setOnClickListener(v1 ->
        {
            dialog.dismiss();
        });
        btn_busca.setOnClickListener(v1 ->
        {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(v1.getContext(), "datos_busqueda_individual", null, 1);
            SQLiteDatabase bdd = admin.getWritableDatabase();
            for (int i = 0; i < cardArrayList.size(); i++)
            {
                ContentValues registro = new ContentValues();
                registro.put("id", cardArrayList.get(i).getId());
                registro.put("id_user", cardArrayList.get(i).getId_user());
                registro.put("cuenta", cardArrayList.get(i).getCuenta());
                registro.put("user", cardArrayList.get(i).getUser());
                registro.put("password", cardArrayList.get(i).getPassword());
                registro.put("imagenFireBase", cardArrayList.get(i).getImagenFireBase());
                registro.put("fecha_creación", cardArrayList.get(i).getFecha_creación());
                registro.put("fecha_ultimo_uso", cardArrayList.get(i).getFecha_ultimo_uso());
                registro.put("indice", i);
                bdd.insert("datos_busqueda_individual", null, registro);
            }
            Cursor fila = bdd.rawQuery("select * from datos_busqueda_individual where cuenta like '%" + busc_cuenta.getText().toString() + "%'", null);
            ArrayList<Card> cardArrayList_temp = new ArrayList<>();
            if (fila.moveToFirst())
            {
                do
                {
                    Card card_temp = new Card();
                    card_temp = cardArrayList.get(fila.getInt(8));
                    cardArrayList_temp.add(card_temp);
                } while (fila.moveToNext());
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(PantallaInicio.this));
            adapterCard = new AdapterCard(PantallaInicio.this, cardArrayList_temp);
            recyclerView.setAdapter(adapterCard);
            recyclerView.setItemAnimator(new CustomItemAnimation());

            int cant = bdd.delete("datos_busqueda_individual", "1=1", null);
            dialog.dismiss();
        });
    }

    public void logout(MenuItem item)
    {

        Intent i = new Intent(PantallaInicio.this, inicioSesion.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if (ventana == 4)
        {
            Intent i = new Intent(PantallaInicio.this, PantallaEmpresario.class);
            i.putExtra("key", key);
            startActivity(i);
        } else
        {
            Intent i = new Intent(PantallaInicio.this, inicioSesion.class);
            startActivity(i);
        }
        finish();
    }

    public void userinf(MenuItem item)
    {
        Intent i = new Intent(PantallaInicio.this, DatosUsuario.class);
        i.putExtra("key", key);
        i.putExtra("ventana", ventana);
        startActivity(i);
    }

    public void passGen(MenuItem item)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(PantallaInicio.this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_personalizado_imagen, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}