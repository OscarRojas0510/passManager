package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class EmpAgregaUser extends AppCompatActivity
{
    private static byte bb[];
    private String key, imgurl;
    private boolean image;
    private ImageFilterView userImage;
    private TextInputLayout empAgregaUser_correo, empAgregaUser_confPasword;
    private TextInputEditText empAgregaUser_regCorreo, empAgregaUser_User, empAgregaUser_Pass, empAgregaUser_confPass, empAgregaUser_Pin, empAgregaUser_RespPregunta;
    private AutoCompleteTextView empAgregaUser_Pregunta;

    private FirebaseAuth auth;
    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDataBase;

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_agrega_user);

        userImage = findViewById(R.id.empAgregaUser_Foto);
        empAgregaUser_correo = findViewById(R.id.empAgregaUser_correo);
        empAgregaUser_regCorreo = findViewById(R.id.empAgregaUser_regCorreo);
        empAgregaUser_User = findViewById(R.id.empAgregaUser_User);
        empAgregaUser_Pass = findViewById(R.id.empAgregaUser_Pass);
        empAgregaUser_confPasword = findViewById(R.id.empAgregaUser_confPasword);
        empAgregaUser_confPass = findViewById(R.id.empAgregaUser_confPass);
        empAgregaUser_Pin = findViewById(R.id.empAgregaUser_Pin);
        empAgregaUser_RespPregunta = findViewById(R.id.empAgregaUser_RespPregunta);
        empAgregaUser_Pregunta = findViewById(R.id.empAgregaUser_Pregunta);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        key = getIntent().getStringExtra("key");
        String type[] =
                {
                        "Cuál es el primer apellido de tu madre?",
                        "Cuál es el nombre de tu mascota?",
                        "Cuál es tu apodo?",
                        "Cuál es el nombre de tu escuela primaria?",
                        "Cuál es el nombre de tu escuela secundaria?",
                        "Cuál es el nombre de tu escuela preparatoria?",
                        "Cuál es el nombre de tu universidad?",
                        "Cuál es el nombre de tu artista favorito?"
                };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                type
        );
        empAgregaUser_Pregunta.setAdapter(adapter);
        bb = null;
    }

    public void empAgregaUser_takeFoto(View view)
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
        userImage.setImageBitmap(thumbnail);
    }


    public void empAgregaUser_cancel(View view)
    {
        Intent i = new Intent(EmpAgregaUser.this, PantallaEmpresario.class);
        i.putExtra("key", key);
        startActivity(i);
        finish();
    }

    public void empAgregaUser_registrar(View view)
    {
        if (bb != null)
        {
            String corr = empAgregaUser_regCorreo.getText().toString().trim();
            String passw = empAgregaUser_Pass.getText().toString().trim();
            auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(corr, passw).addOnSuccessListener(new OnSuccessListener<AuthResult>()
            {
                @Override
                public void onSuccess(AuthResult authResult)
                {
                    String tostcuenta = empAgregaUser_User.getText().toString().substring(0, 1) + empAgregaUser_User.getText().toString().substring(empAgregaUser_User.getText().toString().length() - 1);
                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String nomarch = tostcuenta + empAgregaUser_User.getText().hashCode() + timeStamp;
                    StorageReference sr = mStorageRef.child("imagesPass/" + nomarch);
                    sr.putBytes(bb).addOnSuccessListener(taskSnapshot -> sr.getDownloadUrl().addOnSuccessListener(uri ->
                    {
                        imgurl = String.valueOf(uri);
                        image = true;

                        if (image)
                        {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("contrasenia", passw);
                            map.put("correo", corr);
                            map.put("cuenta_empresarial", false);
                            map.put("img", imgurl);
                            map.put("nombre", empAgregaUser_User.getText().toString());
                            map.put("pin", Integer.parseInt(empAgregaUser_Pin.getText().toString()));
                            map.put("pregunta", empAgregaUser_Pregunta.getText().toString());
                            map.put("respuesta", empAgregaUser_RespPregunta.getText().toString());

                            firebaseDataBase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDataBase.getReference();
                            mStorageRef = FirebaseStorage.getInstance().getReference();
                            String nkey = databaseReference.child("usuarios").push().getKey();
                            databaseReference.child("usuarios").child(nkey).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void unused)
                                {
                                    HashMap<String, Object> map1 = new HashMap<>();
                                    map1.put("correo", corr);
                                    map1.put("img", imgurl);
                                    map1.put("nombre", empAgregaUser_User.getText().toString());
                                    firebaseDataBase = FirebaseDatabase.getInstance();
                                    databaseReference = firebaseDataBase.getReference();
                                    mStorageRef = FirebaseStorage.getInstance().getReference();
                                    databaseReference.child("usuarios").child(key).child("subs").child(nkey).setValue(map1);
                                    Toast.makeText(EmpAgregaUser.this, "Registro existoso", Toast.LENGTH_SHORT).show();
                                    abrirPantallaEmp();
                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Toast.makeText(EmpAgregaUser.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }).addOnFailureListener(e ->
                    {
                        image = false;
                        Toast.makeText(EmpAgregaUser.this, "fallo en imagen . . ." + e.getMessage(), Toast.LENGTH_LONG).show();
                    }));
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(EmpAgregaUser.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else
        {
            Toast.makeText(this, "debe tomar una foto primero", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirPantallaEmp()
    {
        Intent i = new Intent(EmpAgregaUser.this, PantallaEmpresario.class);
        i.putExtra("key", key);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(EmpAgregaUser.this, PantallaEmpresario.class);
        i.putExtra("key", key);
        startActivity(i);
        finish();
    }
}