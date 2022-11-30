package com.example.splashscreen;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.ViewHolder>
{
    LayoutInflater inflater;
    ArrayList<Card> model;
    Card card;

    public AdapterCard(Context c, ArrayList<Card> model)
    {
        this.inflater = LayoutInflater.from(c);
        this.model = model;
    }

    public void setModel(ArrayList<Card> model)
    {
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = inflater.inflate(R.layout.card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        String cuenta = model.get(position).getCuenta();
        String pass = model.get(position).getPassword();

        String sugerencias = "";
        card = model.get(position);
        int alarma = 0;

        holder.nom.setText(cuenta);
        holder.c = card;

        holder.pass.setText(pass);
        boolean campanaFlag = false;

        if (pass.length() < 8)
        {
            sugerencias += "-La contraseña es muy corta\n\n";
            alarma++;
        }
        int cantmin = 0;
        int cantmay = 0;
        int cantnum = 0;
        int cantsim = 0;

        for (int i = 0; i < pass.length(); i++)
        {
            char c = pass.substring(i, i + 1).charAt(0);
            if (c >= 48 && c <= 57)
            {
                cantnum++;
            } else
            {
                if (c >= 97 && c <= 122)
                {
                    cantmin++;
                } else
                {
                    if (c >= 65 && c <= 90)
                    {
                        cantmay++;
                    } else
                    {
                        cantsim++;
                    }
                }
            }
        }
        if (cantmin < 3)
        {
            campanaFlag = true;
            sugerencias += "-Se recomienda añadir más minúsculas\n\n";
            alarma++;
        }
        if (cantmay < 3)
        {
            campanaFlag = true;
            sugerencias += "-Se recomienda añadir más mayúsculas\n\n";
            alarma++;
        }
        if (cantnum < 3)
        {
            campanaFlag = true;
            sugerencias += "-Se recomienda añadir más numeros\n\n";
            alarma++;
        }
        if (cantsim < 3)
        {
            campanaFlag = true;
            sugerencias += "-Se recomienda añadir más simbolos\n\n";
            alarma++;
        }
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(inflater.getContext(), "contrasenias", null, 1);
        SQLiteDatabase bdd = admin.getWritableDatabase();
        Cursor fila = bdd.rawQuery("select count(*) from (select pass from contrasenias where pass like '%" + pass + "%')", null);
        fila.moveToFirst();
        int coincidencias = fila.getInt(0) - 1;
        if (coincidencias > 0)
        {
            campanaFlag = true;
            sugerencias += "-Existen " + coincidencias + " contraseña(s) iguales o parecidas\n\n";
            alarma++;
        }
        holder.sugerencias = sugerencias;
        if (campanaFlag)
        {
            switch (alarma)
            {
                case 1:
                    holder.notify.setColorFilter(inflater.getContext().getResources().getColor(R.color.alarma_1));
                    break;
                case 2:
                    holder.notify.setColorFilter(inflater.getContext().getResources().getColor(R.color.alarma_2));
                    break;
                case 3:
                    holder.notify.setColorFilter(inflater.getContext().getResources().getColor(R.color.alarma_3));
                    break;
                case 4:
                    holder.notify.setColorFilter(inflater.getContext().getResources().getColor(R.color.alarma_4));
                    break;
                case 5:
                    holder.notify.setColorFilter(inflater.getContext().getResources().getColor(R.color.alarma_5));
                    break;
                case 6:
                    holder.notify.setColorFilter(inflater.getContext().getResources().getColor(R.color.Rojo));
                    break;
            }
        }


        int diag1 = card.getFecha_creación().indexOf("/");
        int diag2 = card.getFecha_creación().substring(diag1 + 1).indexOf("/") + diag1 + 1;
        String fechacard = card.getFecha_creación();
        String fechaReg = fechacard.substring(diag2 + 1, fechacard.length()) + "-" + fechacard.substring(diag1 + 1, diag2) + "-" + fechacard.substring(0, diag1);

        fila = bdd.rawQuery("select cast((julianday('now') - julianday(date('" + fechaReg + "'))) as integer)", null);
        fila.moveToFirst();


        int antiguedad = fila.getInt(0) - 1;
        if (antiguedad < 0)
        {
            antiguedad = 0;
        }

        holder.antiguedad.setText("Registro hace : " + antiguedad + " dia(s)");
        Picasso.with(inflater.getContext())
                .load(card.getImagenFireBase())
                .resize(400, 500)
                .into(holder.imageView);

    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder)
    {
        super.onViewAttachedToWindow(holder);
        animateCircularReveal(holder.itemView);
    }

    public void animateCircularReveal(View v)
    {
        int centerX = 0;
        int centerY = 0;
        int startRad = 0;
        int endRad = Math.max(v.getWidth(), v.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(v, centerX, centerY, startRad, endRad);
        v.setVisibility(View.VISIBLE);
        animation.start();
    }

    @Override
    public int getItemCount()
    {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        Card c;
        EditText nom, pass;
        TextView antiguedad;
        MaterialButton editbtn;
        ImageView imageView, notify;
        String sugerencias;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardFoto);
            nom = itemView.findViewById(R.id.cardCorreo);
            pass = itemView.findViewById(R.id.cardPass);
            antiguedad = itemView.findViewById(R.id.cardAntiguedad);
            notify = itemView.findViewById(R.id.cardNotify);
            editbtn = itemView.findViewById(R.id.card_edit);
            c = card;
            editbtn.setOnClickListener(v ->
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater1 = inflater;
                View v1 = inflater.inflate(R.layout.dialog_sugerencias, null);
                builder.setView(v1);
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                //Intent intent = new Intent(v.getContext(), DetalleCard.class);
                //v.getContext().startActivity(intent);
                //Toast.makeText(v.getContext(), c.getCuenta(), Toast.LENGTH_SHORT).show();
            });
            notify.setOnClickListener(v ->
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater1 = inflater;
                View v1 = inflater.inflate(R.layout.dialog_sugerencias, null);
                builder.setView(v1);
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                ImageButton b = v1.findViewById(R.id.sugerencias_cancel);
                TextView tvPass, tvSugerencias;
                tvPass = v1.findViewById(R.id.sugerencias_pass);
                tvSugerencias = v1.findViewById(R.id.sugerencias_textview);
                tvPass.setText(pass.getText());
                if (sugerencias.length() > 0)
                {
                    tvSugerencias.setText(sugerencias);
                } else
                {
                    tvSugerencias.setText("Tu contraseña es segura");
                }

                b.setOnClickListener(v2 ->
                {
                    dialog.dismiss();
                });
            });
            imageView.setOnClickListener(v ->
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater1 = inflater;
                View v1 = inflater.inflate(R.layout.dialog_personalizado_imagen, null);
                ImageView i = v1.findViewById(R.id.imagenC);

                builder.setView(v1);
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Picasso.with(inflater.getContext())
                        .load(c.getImagenFireBase())
                        .resize(800, 1000)
                        .into(i);
                dialog.show();
            });

        }
    }
}
