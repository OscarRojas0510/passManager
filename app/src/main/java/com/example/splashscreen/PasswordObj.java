package com.example.splashscreen;

public class PasswordObj
{
    String cuenta, fecha_load, fecha_ultimo_uso, img, pass, user;

    public PasswordObj(String cuenta, String fecha_load, String fecha_ultimo_uso, String img, String pass, String user)
    {
        this.cuenta = cuenta;
        this.fecha_load = fecha_load;
        this.fecha_ultimo_uso = fecha_ultimo_uso;
        this.img = img;
        this.pass = pass;
        this.user = user;
    }

    public PasswordObj()
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

    public String getFecha_load()
    {
        return fecha_load;
    }

    public void setFecha_load(String fecha_load)
    {
        this.fecha_load = fecha_load;
    }

    public String getFecha_ultimo_uso()
    {
        return fecha_ultimo_uso;
    }

    public void setFecha_ultimo_uso(String fecha_ultimo_uso)
    {
        this.fecha_ultimo_uso = fecha_ultimo_uso;
    }

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public String getPass()
    {
        return pass;
    }

    public void setPass(String pass)
    {
        this.pass = pass;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }
}
