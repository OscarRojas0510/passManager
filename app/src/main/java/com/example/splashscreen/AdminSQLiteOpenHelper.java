package com.example.splashscreen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper
{

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase BaseDeDatos)
    {
        BaseDeDatos.execSQL("create table datos_almacenados(codigo int primary key, email text, password text)");

        BaseDeDatos.execSQL("create table contrasenias(id text primary key, pass text)");


        BaseDeDatos.execSQL("create table datos_busqueda_individual(" +
                "id text primary key, " +
                "id_user text, " +
                "cuenta text," +
                "user text," +
                "password text," +
                "imagenFireBase text," +
                "fecha_creación text," +
                "fecha_ultimo_uso text," +
                "indice int)");
        BaseDeDatos.execSQL("create table datos_busqueda_emp(" +
                "id text primary key, " +
                "correo text," +
                "img text," +
                "nombre text," +
                "indice int)");

        BaseDeDatos.execSQL("create table datos_gps(ubicacion text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
