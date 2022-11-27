package com.example.splashscreen;

import static com.example.splashscreen.EncriptarTexto.desencriptar;
import static com.example.splashscreen.EncriptarTexto.encriptar;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DatosUsuario extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener
{

    private String id,oldpass;
    AutoCompleteTextView autoCompleteTextView;
    FirebaseAuth auth;
    private Button cancel, save, back, edit;

    EditText correoUser, user, password, confPassword, res, pin, quest;
    String correoUserDB, userDB, passwordDB, resDB, pinDB, questDB, nivelDB;
    TextInputLayout confPass, confEmail;
    LinearLayout editor, navAct;

    static String URLfoto = "";
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    private StorageReference mstorage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);
        componentes();
        id = getIntent().getStringExtra("key");
        getUserDB();
    }

    private void componentes()
    {
        BotonesComponentes();
        iniciaFirebase();
        EditTextComponentes();
    }

    private void BotonesComponentes()
    {
        save = this.findViewById(R.id.save);
        back = this.findViewById(R.id.backAct);
        edit = this.findViewById(R.id.edit);
        cancel=this.findViewById(R.id.cancel);
        edit.setOnClickListener(this);
        cancel.setOnClickListener(this);
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


        confPass = this.findViewById(R.id.confPasswordUserBox);
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
                            passwordDB = desencriptar(userTemp[0].getContrasenia());
                            userDB = userTemp[0].getNombre();
                            questDB = userTemp[0].getPregunta();
                            resDB = userTemp[0].getRespuesta();
                            pinDB = userTemp[0].getPin() + "";
                            oldpass=passwordDB;
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

    public void onClick(@NonNull View v)
    {
        switch (v.getId())
        {
            case R.id.backAct:
                Intent i = new Intent(DatosUsuario.this, PantallaInicio.class);
                i.putExtra("key",id);
                startActivity(i);
                finish();
                break;
            case R.id.cancel:
                editBox(false);
                break;
            case R.id.edit:
                editBox(true);
                break;
            case R.id.save:
                changeDataUser(v);
                break;
        }
    }

    public void editBox(boolean show){
        editor = findViewById(R.id.editor);
        navAct = findViewById(R.id.navAct);
        if (show) {
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
            autoCompleteTextView = findViewById(R.id.preguntaUser);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    preguntaClick(parent, view, position, id);
                }
            });
            user.setEnabled(true);
            password.setEnabled(true);
            pin.setEnabled(true);
            quest.setEnabled(true);
            res.setEnabled(true);
            confPass.setVisibility(View.VISIBLE);
            navAct.setVisibility(View.GONE);
            editor.setVisibility(View.VISIBLE);
        }
        else {
            getUserDB();
            user.setEnabled(false);
            password.setEnabled(false);
            pin.setEnabled(false);
            quest.setEnabled(false);
            res.setEnabled(false);
            confPass.setVisibility(View.GONE);
            editor.setVisibility(View.GONE);
            navAct.setVisibility(View.VISIBLE);
        }
    }

    public void preguntaClick(AdapterView<?> parent, View view, int position, long id){
        Toast.makeText(this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    private void iniciaFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
        mstorage = FirebaseStorage.getInstance().getReference();
    }

    private void changeDataUser(View v){
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
            auth = FirebaseAuth.getInstance();
            String idAdd = id;
            String correoAdd = correoUser.getText().toString();
            String userAdd = user.getText().toString();
            String passwordAdd = encriptar(password.getText().toString());
            String resAdd = res.getText().toString();
            String questAdd = quest.getText().toString();
            int pinAdd = Integer.parseInt(pin.getText().toString());
            if (validaPassword())
            {
                AuthCredential credential = EmailAuthProvider.getCredential(correoAdd,oldpass);
                auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            auth.getCurrentUser().updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        databaseReference.child("usuarios").child(id).child("nombre").setValue(userAdd);
                                        databaseReference.child("usuarios").child(id).child("contrasenia").setValue(passwordAdd);
                                        databaseReference.child("usuarios").child(id).child("pin").setValue(pinAdd);
                                        databaseReference.child("usuarios").child(id).child("pregunta").setValue(questAdd);
                                        databaseReference.child("usuarios").child(id).child("respuesta").setValue(resAdd);
                                        Toast.makeText(DatosUsuario.this, "Cambios realizados con exito", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DatosUsuario.this, "Algo malo paso", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
                cancel.performClick();
            } else
            {
                Toast.makeText(v.getContext(), "Complete los campos", Toast.LENGTH_LONG).show();
            }
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