package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity
{

    boolean cuenta_empresarial;
    String nombre;
    int pin;
    String pregunta;
    String respuesta;

    EditText pass, confirm;
    String key, correo, contrasenia;
    TextInputLayout passil, confil;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        key = getIntent().getStringExtra("key");
        correo = getIntent().getStringExtra("correo");
        contrasenia = getIntent().getStringExtra("contrasenia");
        cuenta_empresarial = getIntent().getBooleanExtra("cuenta_empresarial", false);
        nombre = getIntent().getStringExtra("nombre");
        pin = getIntent().getIntExtra("pin", 0);
        pregunta = getIntent().getStringExtra("pregunta");
        respuesta = getIntent().getStringExtra("respuesta");


        pass = findViewById(R.id.resetPass_pass);
        confirm = findViewById(R.id.resetPass_confirm_pass);
        passil = findViewById(R.id.resetPass_pass_inL);
        confil = findViewById(R.id.resetPass_confirm_pass_inL);
    }

    public void resetPassClose(View view)
    {
        abrirInicioSesion();
    }

    public void resetPassOk(View view)
    {
        String password = pass.getText().toString();
        String confirmation = confirm.getText().toString();
        if (!password.isEmpty() && !confirmation.isEmpty() && password.equals(confirmation))
        {
            passil.setError("");
            confil.setError("");
            auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(correo, EncriptarTexto.desencriptar(contrasenia)).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null)
                        {
                            user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void unused)
                                {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("contrasenia", EncriptarTexto.encriptar(password));
                                    map.put("correo", correo);
                                    map.put("cuenta_empresarial", cuenta_empresarial);
                                    map.put("nombre", nombre);
                                    map.put("pin", pin);
                                    map.put("pregunta", pregunta);
                                    map.put("respuesta", respuesta);
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("usuarios").child(key).updateChildren(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>()
                                            {
                                                @Override
                                                public void onSuccess(Void unused)
                                                {
                                                    Toast.makeText(ResetPassword.this, "Se ha modificado la contraseña", Toast.LENGTH_SHORT).show();
                                                    abrirInicioSesion();
                                                }
                                            });
                                }
                            });
                        } else
                        {
                            Toast.makeText(ResetPassword.this, "Ocurrió un error. . .Intente de nuevo", Toast.LENGTH_SHORT).show();
                        }
                    } else
                    {

                    }
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else
        {
            confil.setError("Contraseñas no coinciden");
            passil.setError("Contraseñas no coinciden");
            if (password.isEmpty())
            {
                passil.setError("Ingrese una nueva contraseña");
            }
            if (confirmation.isEmpty())
            {
                confil.setError("Campo Obligatorio");
            }
        }
    }

    private void abrirInicioSesion()
    {
        Intent i = new Intent(ResetPassword.this, inicioSesion.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        abrirInicioSesion();
    }
}