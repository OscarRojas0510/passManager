package com.example.splashscreen;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterEmp extends RecyclerView.Adapter<AdapterEmp.ViewHolder>
{
    LayoutInflater inflater;
    ArrayList<SubsObject> model;
    SubsObject card;

    public AdapterEmp(Context c, ArrayList<SubsObject> model)
    {
        this.inflater = LayoutInflater.from(c);
        this.model = model;
    }

    //asd
    public void setModel(ArrayList<SubsObject> model)
    {
        this.model = model;
    }

    @NonNull
    @Override
    public AdapterEmp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = inflater.inflate(R.layout.card_emp, parent, false);
        return new AdapterEmp.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEmp.ViewHolder holder, int position)
    {
        card = model.get(position);
        holder.c = card;
        Picasso.with(inflater.getContext())
                .load(card.getImg())
                .resize(400, 500)
                .into(holder.imageView);
        holder.correo.setText(card.getCorreo());
        holder.nombre.setText(card.getNombre());
        holder.verbtn.setOnClickListener(v ->
        {

        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull AdapterEmp.ViewHolder holder)
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
        SubsObject c;
        EditText correo, nombre;
        MaterialButton editbtn, verbtn;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardEmpFoto);
            correo = itemView.findViewById(R.id.cardEmpCorreo);
            nombre = itemView.findViewById(R.id.cardEmpPass);
            editbtn = itemView.findViewById(R.id.cardEmp_edit);
            verbtn = itemView.findViewById(R.id.cardEmp_ver);
            c = card;
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
                        .load(c.getImg())
                        .resize(800, 1000)
                        .into(i);
                dialog.show();
            });
            editbtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(v.getContext(), DatosUsuario.class);
                    i.putExtra("key", c.getId());
                    i.putExtra("ventana", 5);
                    i.putExtra("keya", c.getAdmin());
                    v.getContext().startActivity(i);
                }
            });
            verbtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });
        }
    }
}
