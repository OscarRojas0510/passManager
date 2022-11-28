package com.example.splashscreen;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
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
        holder.editbtn.setOnClickListener(v ->
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

        }
    }
}
