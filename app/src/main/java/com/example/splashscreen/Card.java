package com.example.splashscreen;

import java.io.Serializable;

public class Card implements Serializable
{
    String id;
    String id_user;
    String cuenta;
    String user;
    String password;
    String imagenFireBase;
    String fecha_creación;
    String fecha_ultimo_uso;

    public Card()
    {

    }

    public String getCuenta()
    {
        return cuenta;
    }

    public void setCuenta(String cuenta)
    {
        this.cuenta = cuenta;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getFecha_ultimo_uso()
    {
        return fecha_ultimo_uso;
    }

    public void setFecha_ultimo_uso(String fecha_ultimo_uso)
    {
        this.fecha_ultimo_uso = fecha_ultimo_uso;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId_user()
    {
        return id_user;
    }

    public void setId_user(String id_user)
    {
        this.id_user = id_user;
    }


    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getImagenFireBase()
    {
        return imagenFireBase;
    }

    public void setImagenFireBase(String imagenFireBase)
    {
        this.imagenFireBase = imagenFireBase;
    }

    public String getFecha_creación()
    {
        return fecha_creación;
    }

    public void setFecha_creación(String fecha_creación)
    {
        this.fecha_creación = fecha_creación;
    }

}
