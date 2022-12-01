package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class inicioSesion extends AppCompatActivity
{
    final int CONTADOR_BTN_OLVIDO[] = new int[1];
    final int CONTADOR_BTN_LOGIN[] = new int[1];
    EditText user, pass;
    FirebaseAuth auth;
    private TextInputLayout InputUser, InputPass;
    Switch sw;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        user = findViewById(R.id.etUsuario);
        pass = findViewById(R.id.etPass);
        InputUser = findViewById(R.id.inicio_inputLay_correo);
        InputPass = findViewById(R.id.inicio_inputLay_pass);
        sw = findViewById(R.id.sRecordar);

        buscar();
        CONTADOR_BTN_OLVIDO[0] = 0;
        CONTADOR_BTN_LOGIN[0] = 0;

        auth = FirebaseAuth.getInstance();
        user.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                sw.setChecked(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                InputUser.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        pass.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                sw.setChecked(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                InputPass.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (isChecked)
                {
                    registrar();
                } else
                {
                    eliminar();
                }
            }
        });
    }

    public void registrar()
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "datos_almacenados", null, 1);
        SQLiteDatabase bdd = admin.getWritableDatabase();
        String email = user.getText().toString();
        String con = pass.getText().toString();
        if (!email.isEmpty() && !con.isEmpty())
        {
            ContentValues registro = new ContentValues();
            registro.put("codigo", 1);
            registro.put("email", email);
            registro.put("password", con);
            bdd.insert("datos_almacenados", null, registro);
            bdd.close();
        } else
        {
            validar();
            sw.setChecked(false);
        }
    }

    public void buscar()
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "datos_almacenados", null, 1);
        SQLiteDatabase bdd = admin.getWritableDatabase();
        Cursor fila = bdd.rawQuery("select email, password from datos_almacenados where codigo = 1", null);
        if (fila.moveToFirst())
        {
            user.setText(fila.getString(0));
            pass.setText(fila.getString(1));
            bdd.close();
            sw.setChecked(true);
        } else
        {
            bdd.close();
            sw.setChecked(false);
        }
    }

    //Metodo para Eliminar
    public void eliminar()
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "datos_almacenados", null, 1);
        SQLiteDatabase bdd = admin.getWritableDatabase();
        bdd.delete("datos_almacenados", "1 = 1", null);
        bdd.close();
    }

    public void registro(View view)
    {
        Intent i = new Intent(inicioSesion.this, Registrarse.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finishAffinity();
    }

    public void olvidada(View view)
    {
        CONTADOR_BTN_OLVIDO[0]++;
        if (CONTADOR_BTN_OLVIDO[0] <= 1)
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios");
            final boolean flag[] = new boolean[1];
            final UserObject[] o = new UserObject[1];
            flag[0] = false;
            databaseReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if (snapshot.exists())
                    {
                        for (DataSnapshot db : snapshot.getChildren())
                        {
                            o[0] = db.getValue(UserObject.class);
                            String temp = db.getKey();
                            if (o[0].getCorreo().equals(user.getText().toString()))
                            {
                                try
                                {
                                    InputUser.setHelperText("");
                                    flag[0] = true;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(inicioSesion.this);
                                    LayoutInflater inflater = getLayoutInflater();
                                    View v = inflater.inflate(R.layout.dialog_personalizado, null);
                                    builder.setView(v);
                                    AlertDialog dialog = builder.create();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.show();
                                    dialog.setOnDismissListener(dialog1 ->
                                    {
                                        CONTADOR_BTN_OLVIDO[0] = 0;
                                    });
                                    ImageButton b = v.findViewById(R.id.btncerrar);
                                    b.setOnClickListener(v1 ->
                                    {
                                        dialog.dismiss();
                                        CONTADOR_BTN_OLVIDO[0] = 0;
                                    });
                                    MaterialButton btnOk = v.findViewById(R.id.btnCopiar);
                                    EditText respuesta = v.findViewById(R.id.txtPass);
                                    TextInputLayout ilRespuesta = v.findViewById(R.id.dialog_olvido_textilResp);

                                    btnOk.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            if (o[0].getRespuesta().equals(respuesta.getText().toString()))
                                            {
                                                ilRespuesta.setHelperText("Respuesta correcta");
                                                ilRespuesta.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                                                dialog.dismiss();
                                                segundoDialog(temp, o[0]);
                                            } else
                                            {
                                                ilRespuesta.setHelperText("Respuesta incorrecta");
                                                ilRespuesta.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.deg)));
                                            }
                                        }
                                    });

                                    EditText pregunta;
                                    pregunta = v.findViewById(R.id.olvido_Pregunta);
                                    pregunta.setText(o[0].getPregunta());
                                } catch (Exception e)
                                {

                                }
                                break;
                            }
                        }
                        if (!flag[0])
                        {
                            CONTADOR_BTN_OLVIDO[0] = 0;
                            InputUser.setHelperText("Correo no válido");
                            InputUser.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.Rojo)));

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }
    }

    void segundoDialog(String key, UserObject o)
    {
        Intent i = new Intent(inicioSesion.this, ResetPassword.class);
        i.putExtra("key", key);
        i.putExtra("contrasenia", o.getContrasenia());
        i.putExtra("correo", o.getCorreo());
        i.putExtra("cuenta_empresarial", o.isCuenta_empresarial());
        i.putExtra("nombre", o.getNombre());
        i.putExtra("pin", o.getPin());
        i.putExtra("pregunta", o.getPregunta());
        i.putExtra("respuesta", o.getRespuesta());
        i.putExtra("img", o.getImg());
        startActivity(i);
        finish();
    }

    public void login(View view)
    {
        CONTADOR_BTN_LOGIN[0]++;
        if (CONTADOR_BTN_LOGIN[0] <= 1)
        {
            Toast.makeText(this, "encontrado... validando", Toast.LENGTH_SHORT).show();
            try
            {
                if (validar())
                {
                    auth.signInWithEmailAndPassword(user.getText().toString().trim(), pass.getText().toString().trim())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios");
                                        final UserObject[] o = new UserObject[1];
                                        databaseReference.addValueEventListener(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {
                                                for (DataSnapshot db : snapshot.getChildren())
                                                {
                                                    o[0] = db.getValue(UserObject.class);
                                                    String temp = db.getKey();
                                                    if (o[0].getCorreo().equals(user.getText().toString()))
                                                    {
                                                        String gps = "Desconocida";
                                                        AdminSQLiteOpenHelper admin;
                                                        SQLiteDatabase bdd;
                                                        admin = new AdminSQLiteOpenHelper(getApplicationContext(), "datos_gps", null, 1);
                                                        bdd = admin.getWritableDatabase();
                                                        Cursor fila = bdd.rawQuery("select ubicacion from datos_gps", null);
                                                        if (fila.moveToFirst())
                                                        {
                                                            gps = fila.getString(0);
                                                        }
                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("ult_login", gps);
                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                                        reference.child("usuarios").child(temp).updateChildren(map);

                                                        if (o[0].isCuenta_empresarial())
                                                        {
                                                            Intent i = new Intent(inicioSesion.this, PantallaEmpresario.class);
                                                            i.putExtra("key", temp);
                                                            startActivity(i);
                                                            CONTADOR_BTN_LOGIN[0] = 0;
                                                        } else
                                                        {
                                                            Intent i = new Intent(inicioSesion.this, PantallaInicio.class);
                                                            i.putExtra("key", temp);
                                                            startActivity(i);
                                                            CONTADOR_BTN_LOGIN[0] = 0;
                                                        }
                                                        break;

                                                    }
                                                }
                                                CONTADOR_BTN_LOGIN[0] = 0;
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error)
                                            {
                                                CONTADOR_BTN_LOGIN[0] = 0;
                                            }
                                        });

                                    } else
                                    {
                                        CONTADOR_BTN_LOGIN[0] = 0;
                                        Toast.makeText(getApplicationContext(), "Autenticación fallida", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else
                {
                    CONTADOR_BTN_LOGIN[0] = 0;
                }
            } catch (Exception e)
            {
                CONTADOR_BTN_LOGIN[0] = 0;
            }
        }
    }

    private boolean validar()
    {
        boolean retorno = true;
        String usuario, password;
        usuario = user.getText().toString();
        password = pass.getText().toString();
        if (usuario.isEmpty())
        {
            InputUser.setError("Ingrese un Correo Electronico");
            retorno = false;
        } else
        {
            InputUser.setErrorEnabled(false);
        }

        if (password.isEmpty())
        {
            InputPass.setError("Ingrese una Contraseña");
            retorno = false;
        } else
        {
            InputPass.setErrorEnabled(false);
        }
        return retorno;
    }
}