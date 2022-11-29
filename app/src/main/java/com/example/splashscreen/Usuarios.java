package com.example.splashscreen;

public class Usuarios
{
    String id, correo, contrasenia, nombre, pregunta, respuesta, img, ult_login;
    int pin;
    boolean cuenta_empresarial;

    public Usuarios()
    {

    }

    public Usuarios(String id, String correo, String password, String user, String quest, String res, int pin, boolean cuenta_empresarial, String img)
    {
        this.id = id;
        this.correo = correo;
        this.contrasenia = password;
        this.respuesta = res;
        this.nombre = user;
        this.pregunta = quest;
        this.pin = pin;
        this.cuenta_empresarial = cuenta_empresarial;
        this.img = img;
    }

    public String getUlt_login()
    {
        return ult_login;
    }

    public void setUlt_login(String ult_login)
    {
        this.ult_login = ult_login;
    }

    public String getId()
    {
        return id;
    }

    public String getCorreo()
    {
        return correo;
    }

    public String getContrasenia()
    {
        return contrasenia;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getPregunta()
    {
        return pregunta;
    }

    public String getRespuesta()
    {
        return respuesta;
    }

    public int getPin()
    {
        return pin;
    }


    public boolean getCuenta_empresarial()
    {
        return cuenta_empresarial;
    }

    public String getImg()
    {
        return img;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setCorreo(String correo)
    {
        this.correo = correo;
    }

    public void setContrasenia(String contrasenia)
    {
        this.contrasenia = contrasenia;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public void setPregunta(String pregunta)
    {
        this.pregunta = pregunta;
    }

    public void setRespuesta(String respuesta)
    {
        this.respuesta = respuesta;
    }

    public void setPin(int pin)
    {
        this.pin = pin;
    }

    public void setCuenta_empresarial(boolean cuenta_empresarial)
    {
        this.cuenta_empresarial = cuenta_empresarial;
    }

    public void setImg(String img)
    {
        this.img = img;
    }
}
