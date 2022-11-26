package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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

public class Registrarse extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener
{

    private Button registrar, cancelar;

    AutoCompleteTextView autoCompleteTextView;
    FirebaseAuth auth;
    EditText correo, pass;
    TextInputLayout confPass, confEmail;

    static String URLfoto = "";
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    EditText correoUser, user, password, confPassword, res, pin, quest;

    private StorageReference mstorage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        correo = findViewById(R.id.regCorreo);
        pass = findViewById(R.id.regPass);

        auth = FirebaseAuth.getInstance();

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

        autoCompleteTextView = findViewById(R.id.regPregunta);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                preguntaClick(parent, view, position, id);
            }
        });
        componentes();
    }

    private void componentes()
    {
        BotonesComponentes();
        iniciaFirebase();
        EditTextComponentes();
    }

    public void preguntaClick(AdapterView<?> parent, View view, int position, long id)
    {
        Toast.makeText(this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    public void registro()
    {
        String corr = correo.getText().toString().trim();
        String passw = pass.getText().toString().trim();
        auth.createUserWithEmailAndPassword(corr, passw).addOnSuccessListener(new OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess(AuthResult authResult)
            {
                Toast.makeText(Registrarse.this, "Registro existoso", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(Registrarse.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void BotonesComponentes()
    {
        registrar = this.findViewById(R.id.mainBtnLogin);
        cancelar = this.findViewById(R.id.mainBtnSalir);
        registrar.setOnClickListener(this);
        cancelar.setOnClickListener(this);
    }

    public void limpiar()
    {
        correoUser.setText("");
        user.setText("");
        password.setText("");
        pin.setText("");
        res.setText("");
    }

    private void iniciaFirebase()
    {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
        mstorage = FirebaseStorage.getInstance().getReference();
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
                Intent i = new Intent(Registrarse.this, inicioSesion.class);
                startActivity(i);
                //finish();
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
                    String passwordAdd = password.getText().toString();
                    String resAdd = res.getText().toString();
                    String questAdd = quest.getText().toString();
                    int pinAdd = Integer.parseInt(pin.getText().toString());
                    if (validaCorreo() && validaPassword())
                    {
                        Usuarios userNew = new Usuarios(idAdd, correoAdd, passwordAdd, userAdd, questAdd, resAdd, pinAdd, false);
                        databaseReference.child("usuarios").child(userNew.getId()).setValue(userNew);
                        registro();
                        limpiar();
                        Toast.makeText(this, "Usuario ha sido agregado con exito", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        Toast.makeText(v.getContext(), "Complete los campos", Toast.LENGTH_LONG).show();
                    }
                }
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
            confPass.setHelperText("La contraseña no corresponde");
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