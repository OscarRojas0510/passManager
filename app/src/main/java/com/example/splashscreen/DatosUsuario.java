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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DatosUsuario extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener
{

    private String id;


    private Button salir, cancelar, save, back, edit;

    EditText correoUser, user, password, confPassword, res, pin, quest;
    String correoUserDB, userDB, passwordDB, resDB, pinDB, questDB, nivelDB;
    TextInputLayout confPass, confEmail;
    LinearLayout editor, navAct;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);
        EditTextComponentes();
        BotonesComponentes();
        id = getIntent().getStringExtra("key");
        getUserDB();
    }

    public void datosUsuario(View v)
    {
    }

    private void iniciaFirebase()
    {
    }

    private void BotonesComponentes()
    {
        save = this.findViewById(R.id.save);
        back = this.findViewById(R.id.backAct);
        edit = this.findViewById(R.id.edit);

        edit.setOnClickListener(this);
        //cancelar.setOnClickListener(this);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void EditTextComponentes()
    {
        correoUser = findViewById(R.id.datos_users_correoUser);
        user = findViewById(R.id.nameUser);
        password = findViewById(R.id.passUser);
        confPassword = this.findViewById(R.id.confPassUser);
        pin = this.findViewById(R.id.pinUser);
        quest = this.findViewById(R.id.preguntaUser);
        res = this.findViewById(R.id.respPreguntaUser);


        confPass = this.findViewById(R.id.confPaswordUser);
        confEmail = this.findViewById(R.id.correoUserBox);


        confPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    validaPassword();
                }
            }
        });
        correoUser.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    validaCorreo();
                }
            }
        });
    }

    public void getUserDB()
    {
        DatabaseReference firebaseDataBase;
        firebaseDataBase = FirebaseDatabase.getInstance().getReference().child("usuarios");
        firebaseDataBase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                int cont = 0;
                final Usuarios[] userTemp = new Usuarios[1];
                for (DataSnapshot objSnapshot : snapshot.getChildren())
                {
                    if (snapshot.exists())
                    {
                        userTemp[0] = objSnapshot.getValue(Usuarios.class);
                        userTemp[0].setId(objSnapshot.getKey());
                        if (userTemp[0].getId().equals(id))
                        {
                            correoUserDB = userTemp[0].getCorreo();
                            passwordDB = userTemp[0].getContrasenia();
                            userDB = userTemp[0].getNombre();
                            questDB = userTemp[0].getPregunta();
                            resDB = userTemp[0].getRespuesta();
                            pinDB = userTemp[0].getPin() + "";
                            //nivelDB = "" + userTemp[0].isCuenta_empresarial();

                            // img = cursor.getString(9);
                            correoUser.setText(correoUserDB);
                            user.setText(userDB);
                            password.setText(passwordDB);
                            confPassword.setText(passwordDB);
                            pin.setText(pinDB);
                            quest.setText(questDB);
                            res.setText(resDB);
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.backAct:
                Intent i = new Intent(DatosUsuario.this, inicioSesion.class);
                startActivity(i);
                //finish();
                break;
            case R.id.cancel:
                datosUsuario(v);
                break;
            case R.id.edit:
                changeDataUser(v);
                break;
            case R.id.save:
                break;
        }
    }

    public void editBox(boolean show)
    {
        editor = findViewById(R.id.editor);
        navAct = findViewById(R.id.navAct);
        if (show)
        {
            navAct.setVisibility(View.GONE);
            editor.setVisibility(View.VISIBLE);
        } else
        {
            editor.setVisibility(View.GONE);
            navAct.setVisibility(View.VISIBLE);
        }

    }

    private void changeDataUser(View v)
    {
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
            password.setTextColor(this.getResources().getColor(R.color.red, null));
            confPassword.setTextColor(this.getResources().getColor(R.color.red, null));
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