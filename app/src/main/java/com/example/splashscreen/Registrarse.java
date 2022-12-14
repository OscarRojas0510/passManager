package com.example.splashscreen;

import static com.example.splashscreen.EncriptarTexto.encriptar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Registrarse extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener
{
    private Button registrar, cancelar;
    AutoCompleteTextView autoCompleteTextView;
    FirebaseAuth auth;
    EditText correo, pass;
    TextInputLayout confPass, confEmail;
    boolean image;
    static String URLfoto = "";
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;
    String imgurl;
    ImageFilterView userImage;
    ImageFilterButton captImage;
    Usuarios userNew;
    Uri cam_uri;

    EditText correoUser, user, password, confPassword, res, pin, quest;

    private StorageReference mstorageRef;
    private static byte bb[];

    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        correo = findViewById(R.id.regCorreo);
        pass = findViewById(R.id.regPass);
        componentes();
        auth = FirebaseAuth.getInstance();

        String type[] =
                {
                        "Cu??l es el primer apellido de tu madre?",
                        "Cu??l es el nombre de tu mascota?",
                        "Cu??l es tu apodo?",
                        "Cu??l es el nombre de tu escuela primaria?",
                        "Cu??l es el nombre de tu escuela secundaria?",
                        "Cu??l es el nombre de tu escuela preparatoria?",
                        "Cu??l es el nombre de tu universidad?",
                        "Cu??l es el nombre de tu artista favorito?"
                };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                type
        );
        autoCompleteTextView = findViewById(R.id.regPregunta);
        autoCompleteTextView.setAdapter(adapter);
        userImage = findViewById(R.id.userImage);
        BitmapDrawable drawable = (BitmapDrawable) userImage.getDrawable();
        Bitmap thumbnail = drawable.getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        bb=bytes.toByteArray();
    }

    public void addPassTakePhoto(View view)
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startCamera.launch(i);
    }

    ActivityResultLauncher<Intent> startCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        onCaptureResult(data);
                    }
                }
            });

    private void componentes()
    {
        iniciaFirebase();
        BotonesComponentes();
        EditTextComponentes();
    }

    /*public void preguntaClick(AdapterView<?> parent, View view, int position, long id)
    {
        Toast.makeText(this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
    }*/

    public void registro() {
        if (bb != null) {
            String corr = correo.getText().toString().trim();
            String passw = pass.getText().toString().trim();
            auth.createUserWithEmailAndPassword(corr, passw).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    String tostcuenta = user.getText().toString().substring(0, 1) + user.getText().toString().substring(user.getText().toString().length() - 1);
                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String nomarch = tostcuenta + user.getText().hashCode() + timeStamp;
                    StorageReference sr = mstorageRef.child("imagesPass/" + nomarch);
                    sr.putBytes(bb).addOnSuccessListener(taskSnapshot -> sr.getDownloadUrl().addOnSuccessListener(uri ->
                    {
                        imgurl = String.valueOf(uri);
                        image = true;

                        if (image) {
                            userNew.setImg(imgurl);
                            databaseReference.child("usuarios").push().setValue(userNew).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    cancelar.performClick();
                                    Toast.makeText(Registrarse.this, "Registro existoso", Toast.LENGTH_SHORT).show();
                                    abrirInicioSesion();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Registrarse.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }).addOnFailureListener(e ->
                    {
                        image = false;
                        Toast.makeText(Registrarse.this, "fallo en imagen . . ." + e.getMessage(), Toast.LENGTH_LONG).show();
                    }));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    limpiar();
                    Toast.makeText(Registrarse.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void BotonesComponentes()
    {
        registrar = this.findViewById(R.id.mainBtnLogin);
        cancelar = this.findViewById(R.id.mainBtnSalir);
        captImage=findViewById(R.id.upUserImage);
        registrar.setOnClickListener(this);
        cancelar.setOnClickListener(this);
        captImage.setOnClickListener(this);
    }

    public void limpiar()
    {
        correoUser.setText("");
        user.setText("");
        password.setText("");
        confPassword.setText("");
        pin.setText("");
        quest.setText("");
        res.setText("");
    }

    private void iniciaFirebase()
    {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
        mstorageRef = FirebaseStorage.getInstance().getReference();
    }

    private void EditTextComponentes()
    {
        correoUser = this.findViewById(R.id.regCorreo);
        user = this.findViewById(R.id.regUser);
        password = this.findViewById(R.id.regPass);
        confPassword = this.findViewById(R.id.confPass);
        pin = this.findViewById(R.id.refPin);
        quest = this.findViewById(R.id.regPregunta);
        res = this.findViewById(R.id.regRespPregunta);
        confPassword.setOnFocusChangeListener(this::onFocusChange);
        confPass = this.findViewById(R.id.confPasword);
        confEmail = this.findViewById(R.id.correo);
        correoUser.setOnFocusChangeListener(this::onFocusChange);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.mainBtnSalir:
                abrirInicioSesion();
                break;
            case R.id.mainBtnLogin:
                if (
                        correoUser.getText().toString().equals("") ||
                                user.getText().toString().equals("") ||
                                password.getText().toString().equals("") ||
                                pin.getText().toString().equals("") ||
                                res.getText().toString().equals("")
                )
                {
                    Toast.makeText(v.getContext(), "Complete los campos", Toast.LENGTH_LONG).show();

                } else
                {
                    int auto = (int) (Math.random() * 10000);
                    String idAdd = ("" + auto);
                    String correoAdd = correoUser.getText().toString();
                    String userAdd = user.getText().toString();
                    String passwordAdd = encriptar(password.getText().toString());
                    String resAdd = res.getText().toString();
                    String questAdd = quest.getText().toString();
                    int pinAdd = Integer.parseInt(pin.getText().toString());
                    if (validaCorreo() && validaPassword())
                    {
                        userNew = new Usuarios(idAdd, correoAdd, passwordAdd, userAdd, questAdd, resAdd, pinAdd, false,imgurl);
                        registro();
                    } else
                    {
                        Toast.makeText(v.getContext(), "Complete los campos", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.upUserImage:
                Toast.makeText(v.getContext(),"Camera",Toast.LENGTH_SHORT);
                addPassTakePhoto(v);
                break;
        }

    }

    @SuppressLint("ResourceAsColor")
    public void onFocusChange(@NonNull View v, boolean hasFocus)
    {
        switch (v.getId())
        {
            case R.id.regCorreo:
                if (!hasFocus)
                {
                    validaCorreo();
                }
                break;
            case R.id.confPass:
                if (!hasFocus)
                {
                    validaPassword();
                }
                break;
        }
    }

    private void abrirInicioSesion()
    {
        Intent i = new Intent(Registrarse.this, inicioSesion.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        abrirInicioSesion();
    }

    public boolean validaCorreo()
    {
        if (correoUser.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correoUser.getText().toString().trim()).matches())
        {
            confEmail.setHelperTextEnabled(true);
            confEmail.setHelperText("Correo no valido");
            return false;
        } else
        {
            confEmail.setHelperText("");
            confEmail.setHelperTextEnabled(false);
            return true;
        }
    }

    public boolean validaPassword()
    {
        if (!password.getText().toString().equals(confPassword.getText().toString()))
        {
            password.setTextColor(this.getResources().getColor(R.color.Rojo, null));
            confPassword.setTextColor(this.getResources().getColor(R.color.Rojo, null));
            confPass.setHelperTextEnabled(true);
            confPass.setHelperText("La contrase??a no corresponde");
            return false;
        } else
        {
            password.setTextColor(this.getResources().getColor(R.color.white, null));
            confPassword.setTextColor(this.getResources().getColor(R.color.white, null));
            confPass.setHelperText("");
            confPass.setHelperTextEnabled(false);
            return true;
        }
    }

    private void onCaptureResult(Intent data)
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        bb = bytes.toByteArray();
        //String file = Base64.encodeToString(bb, Base64.DEFAULT);
        userImage.setImageBitmap(thumbnail);
    }

    //Se debe colocar dentro del registra debido al jodido Firebase, que no permite llamadas masivas. o eso creo
    //solo s?? que si la separo, esto falla
    public void upUserImage(){
        String tostcuenta = user.getText().toString().substring(0, 1) + user.getText().toString().substring(user.getText().toString().length() - 1);
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nomarch = tostcuenta + user.getText().hashCode() + timeStamp;
        StorageReference sr = mstorageRef.child("imagesPass/" + nomarch);
        sr.putBytes(bb).addOnSuccessListener(taskSnapshot -> sr.getDownloadUrl().addOnSuccessListener(uri ->
        {
            imgurl = String.valueOf(uri);
            image=true;
        }).addOnFailureListener(e ->
        {
            image=false;
            Toast.makeText(this, "fallo en imagen . . ." + e.getMessage(), Toast.LENGTH_LONG).show();
        }));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}