package com.example.splashscreen;

import static com.example.splashscreen.EncriptarTexto.desencriptar;
import static com.example.splashscreen.EncriptarTexto.encriptar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatosUsuario extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener
{

    private String id, oldpass;
    AutoCompleteTextView autoCompleteTextView;
    FirebaseAuth auth;
    private Button cancel, save, back, edit;
    int ventana = 0;
    String keya = null;

    EditText correoUser, user, password, confPassword, res, pin, quest, ubicacion;
    String correoUserDB, userDB, passwordDB, resDB, pinDB, questDB, nivelDB, imageDB;
    TextInputLayout confPass, confEmail;
    LinearLayout editor, navAct;
    ImageFilterView userImage;
    ImageFilterButton captImage, upUserNivel;
    static String URLfoto = "";
    String imgurl;
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;
    private StorageReference mstorage;

    DataSnapshot objSnapshotNew;

    private StorageReference mstorageRef;
    private static byte bb[];
    DataSnapshot snapshotNew;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);
        componentes();
        id = getIntent().getStringExtra("key");
        keya = getIntent().getStringExtra("keya");
        ventana = getIntent().getIntExtra("ventana", 0);
        getUserDB();
        userImage = findViewById(R.id.userImageAct);
        BitmapDrawable drawable = (BitmapDrawable) userImage.getDrawable();
        Bitmap thumbnail = drawable.getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        bb = bytes.toByteArray();
        mstorageRef = FirebaseStorage.getInstance().getReference();
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
        cancel = this.findViewById(R.id.cancel);
        captImage = this.findViewById(R.id.upNewUserImage);
        upUserNivel = this.findViewById(R.id.upNewUserAdmin);
        edit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        captImage.setOnClickListener(this);
        upUserNivel.setOnClickListener(this);
    }

    private void EditTextComponentes()
    {
        ubicacion = findViewById(R.id.datos_user_ubicacion);
        correoUser = findViewById(R.id.datos_users_correoUser);
        user = findViewById(R.id.nameUser);
        password = findViewById(R.id.passUser);
        confPassword = this.findViewById(R.id.confPassUser);
        pin = this.findViewById(R.id.pinUser);
        quest = this.findViewById(R.id.preguntaUser);
        res = this.findViewById(R.id.respPreguntaUser);
        userImage = findViewById(R.id.userImage);
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
                snapshotNew = snapshot;
                int cont = 0;
                final Usuarios[] userTemp = new Usuarios[1];
                for (DataSnapshot objSnapshot : snapshot.getChildren())
                {
                    objSnapshotNew = objSnapshot;
                    if (snapshot.exists())
                    {
                        userTemp[0] = objSnapshot.getValue(Usuarios.class);
                        userTemp[0].setId(objSnapshot.getKey());
                        if (userTemp[0].getId().equals(id))
                        {
                            Toast.makeText(DatosUsuario.this, id, Toast.LENGTH_SHORT);
                            correoUserDB = userTemp[0].getCorreo();
                            passwordDB = desencriptar(userTemp[0].getContrasenia());
                            userDB = userTemp[0].getNombre();
                            questDB = userTemp[0].getPregunta();
                            resDB = userTemp[0].getRespuesta();
                            pinDB = userTemp[0].getPin() + "";
                            boolean admin = userTemp[0].getCuenta_empresarial();
                            if (admin)
                            {
                                upUserNivel.setVisibility(View.GONE);
                            }
                            oldpass = passwordDB;
                            imageDB = userTemp[0].getImg();
                            ubicacion.setText(userTemp[0].getUlt_login());
                            //nivelDB = "" + userTemp[0].isCuenta_empresarial();
                            // img = cursor.getString(9);
                            correoUser.setText(correoUserDB);
                            user.setText(userDB);
                            password.setText(passwordDB);
                            confPassword.setText(passwordDB);
                            pin.setText(pinDB);
                            quest.setText(questDB);
                            res.setText(resDB);
                            Picasso.with(DatosUsuario.this).load(imageDB).into(userImage);
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
                if (ventana == 5)
                {
                    Intent i = new Intent(DatosUsuario.this, PantallaEmpresario.class);
                    i.putExtra("key", keya);
                    startActivity(i);
                } else
                {
                    if (ventana == 4)
                    {
                        Intent i = new Intent(DatosUsuario.this, PantallaEmpresario.class);
                        i.putExtra("key", id);
                        startActivity(i);
                    } else
                    {
                        Intent i = new Intent(DatosUsuario.this, PantallaInicio.class);
                        i.putExtra("key", id);
                        startActivity(i);
                    }
                }
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
            case R.id.upNewUserImage:
                addPassTakePhoto(v);
                break;
            case R.id.upNewUserAdmin:
                upAdmin();
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if (ventana == 5)
        {
            Intent i = new Intent(DatosUsuario.this, PantallaEmpresario.class);
            i.putExtra("key", keya);
            startActivity(i);
        } else
        {
            if (ventana == 4)
            {
                Intent i = new Intent(DatosUsuario.this, PantallaEmpresario.class);
                i.putExtra("key", id);
                startActivity(i);
            } else
            {
                Intent i = new Intent(DatosUsuario.this, PantallaInicio.class);
                i.putExtra("key", id);
                startActivity(i);
            }
        }
        finish();
    }

    public void editBox(boolean show)
    {
        editor = findViewById(R.id.editor);
        navAct = findViewById(R.id.navAct);
        if (show)
        {
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
            captImage.setVisibility(View.VISIBLE);
            navAct.setVisibility(View.GONE);
            editor.setVisibility(View.VISIBLE);
        } else
        {
            getUserDB();
            user.setEnabled(false);
            password.setEnabled(false);
            pin.setEnabled(false);
            quest.setEnabled(false);
            res.setEnabled(false);
            confPass.setVisibility(View.GONE);
            editor.setVisibility(View.GONE);
            captImage.setVisibility(View.GONE);
            navAct.setVisibility(View.VISIBLE);
        }
    }

    public void preguntaClick(AdapterView<?> parent, View view, int position, long id)
    {
        Toast.makeText(this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    private void iniciaFirebase()
    {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDataBase.getReference();
        mstorage = FirebaseStorage.getInstance().getReference();
    }

    private void changeDataUser(View v)
    {
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
                AuthCredential credential = EmailAuthProvider.getCredential(correoAdd, oldpass);
                auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            auth.getCurrentUser().updatePassword(password.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void unused)
                                {
                                    final Usuarios[] userTemp = new Usuarios[1];
                                    if (task.isSuccessful())
                                    {
                                        for (DataSnapshot objSnapshot : snapshotNew.getChildren())
                                        {
                                            if (snapshotNew.exists())
                                            {
                                                userTemp[0] = objSnapshot.getValue(Usuarios.class);
                                                userTemp[0].setId(objSnapshot.getKey());
                                                if (userTemp[0].getId().equals(id))
                                                {
                                                    String tostcuenta = user.getText().toString().substring(0, 1) + user.getText().toString().substring(user.getText().toString().length() - 1);
                                                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                                    String nomarch = tostcuenta + user.getText().hashCode() + timeStamp;
                                                    StorageReference sr = mstorageRef.child("imagesPass/" + nomarch);

                                                    BitmapDrawable drawable = (BitmapDrawable) userImage.getDrawable();
                                                    Bitmap thumbnail = drawable.getBitmap();
                                                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                                                    bb = bytes.toByteArray();

                                                    sr.putBytes(bb).addOnSuccessListener(taskSnapshot -> sr.getDownloadUrl().addOnSuccessListener(uri ->
                                                    {
                                                        imgurl = String.valueOf(uri);

                                                        databaseReference.child("usuarios").child(id).child("nombre").setValue(userAdd);
                                                        databaseReference.child("usuarios").child(id).child("contrasenia").setValue(passwordAdd);
                                                        databaseReference.child("usuarios").child(id).child("pin").setValue(pinAdd);
                                                        databaseReference.child("usuarios").child(id).child("pregunta").setValue(questAdd);
                                                        databaseReference.child("usuarios").child(id).child("respuesta").setValue(resAdd);
                                                        databaseReference.child("usuarios").child(id).child("img").setValue(imgurl);
                                                        Toast.makeText(DatosUsuario.this, "Cambios realizados con exito", Toast.LENGTH_SHORT).show();
                                                        editBox(false);
                                                    }).addOnFailureListener(e ->
                                                    {
                                                        Toast.makeText(DatosUsuario.this, "fallo en imagen . . ." + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }));
                                                }

                                            }
                                        }
                                    } else
                                    {
                                        Toast.makeText(DatosUsuario.this, "Algo malo paso", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(DatosUsuario.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
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
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        onCaptureResult(data);
                    }
                }
            });

    private void onCaptureResult(Intent data)
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        bb = bytes.toByteArray();
        //String file = Base64.encodeToString(bb, Base64.DEFAULT);
        userImage.setImageBitmap(thumbnail);
    }

    public void upAdmin()
    {
        final Usuarios[] userTemp = new Usuarios[1];
        for (DataSnapshot objSnapshot : snapshotNew.getChildren())
        {
            if (snapshotNew.exists())
            {
                userTemp[0] = objSnapshot.getValue(Usuarios.class);
                userTemp[0].setId(objSnapshot.getKey());
                if (userTemp[0].getId().equals(id))
                {
                    databaseReference.child("usuarios").child(id).child("cuenta_empresarial").setValue(true);
                    upUserNivel.setVisibility(View.GONE);
                    Toast.makeText(DatosUsuario.this, "Cambios realizados con exito", Toast.LENGTH_SHORT).show();
                }

            }
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