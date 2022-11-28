package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Agregar extends AppCompatActivity
{

    String key;
    StorageReference mStorageRef;
    ImageView imageView;
    EditText cuenta, user, pass;
    DatabaseReference mDataBase;
    final Calendar c = Calendar.getInstance();
    int dia;
    int mes;
    int anio;

    private static byte bb[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        key = getIntent().getStringExtra("key");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imageView = findViewById(R.id.addPass_Foto);
        cuenta = findViewById(R.id.addPass_Cuenta);
        user = findViewById(R.id.addPass_User);
        pass = findViewById(R.id.addPass_Pass);

        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH) + 1;
        anio = c.get(Calendar.YEAR);
        bb = null;
    }


    public void addPassClose(View view)
    {
        Intent i = new Intent(Agregar.this, PantallaInicio.class);
        i.putExtra("key", key);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finish();
    }

    public void addPassTakePhoto(View view)
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 101)
            {
                onCaptureResult(data);
            }
        }
    }

    private void onCaptureResult(Intent data)
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        bb = bytes.toByteArray();
        //String file = Base64.encodeToString(bb, Base64.DEFAULT);
        imageView.setImageBitmap(thumbnail);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(Agregar.this, PantallaInicio.class);
        i.putExtra("key", key);
        startActivity(i);
        finish();
    }

    private void subirAFirebase(byte[] bb)
    {
        String tostcuenta = cuenta.getText().toString().substring(0, 1) + cuenta.getText().toString().substring(cuenta.getText().toString().length() - 1);
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nomarch = tostcuenta + key + cuenta.getText().hashCode() + timeStamp;
        StorageReference sr = mStorageRef.child("imagesPass/" + nomarch);
        sr.putBytes(bb).addOnSuccessListener(taskSnapshot -> sr.getDownloadUrl().addOnSuccessListener(uri ->
        {
            String imgurl = String.valueOf(uri);
            Toast.makeText(this, "imagen subida", Toast.LENGTH_SHORT).show();

            mDataBase = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> map = new HashMap<>();
            map.put("cuenta", cuenta.getText().toString());
            if (!user.getText().toString().isEmpty())
            {
                map.put("user", user.getText().toString());
            }
            map.put("pass", EncriptarTexto.encriptar(pass.getText().toString()));
            map.put("img", imgurl);

            String fecha = dia + "/" + mes + "/" + anio;

            map.put("fecha_load", fecha);
            map.put("fecha_ultimo_uso", fecha);

            mDataBase.child("usuarios").child(key).child("passwords").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void unused)
                {
                    Toast.makeText(getApplicationContext(), "registro agregado", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), PantallaInicio.class);
                    i.putExtra("key", key);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(getApplicationContext(), "fallo en datos . . ." + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }).addOnFailureListener(e ->
        {
            Toast.makeText(this, "fallo en imagen . . ." + e.getMessage(), Toast.LENGTH_LONG).show();
        }));
    }


    public void registraPass(View view)
    {
        if (!(cuenta.getText().toString().isEmpty() && pass.getText().toString().isEmpty()) && bb != null)
        {
            subirAFirebase(bb);
        } else
        {
            Toast.makeText(this, "faltan datos", Toast.LENGTH_LONG).show();
        }
    }
}