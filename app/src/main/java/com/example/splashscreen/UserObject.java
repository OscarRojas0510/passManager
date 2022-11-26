package com.example.splashscreen;

public class UserObject
{
    private String contrasenia;
    private String correo;
    private boolean cuenta_empresarial;
    private String nombre;
    private int pin;
    private String pregunta;
    private String respuesta;

    public UserObject()
    {

    }

    public UserObject(String contrasenia, String correo, boolean cuenta_empresarial, String nombre, int pin, String pregunta, String respuesta)
    {
        this.contrasenia = contrasenia;
        this.correo = correo;
        this.cuenta_empresarial = cuenta_empresarial;
        this.nombre = nombre;
        this.pin = pin;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public String getContrasenia()
    {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia)
    {
        this.contrasenia = contrasenia;
    }

    public String getCorreo()
    {
        return correo;
    }

    public void setCorreo(String correo)
    {
        this.correo = correo;
    }

    public boolean isCuenta_empresarial()
    {
        return cuenta_empresarial;
    }

    public void setCuenta_empresarial(boolean cuenta_empresarial)
    {
        this.cuenta_empresarial = cuenta_empresarial;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public int getPin()
    {
        return pin;
    }

    public void setPin(int pin)
    {
        this.pin = pin;
    }

    public String getPregunta()
    {
        return pregunta;
    }

    public void setPregunta(String preguna)
    {
        this.pregunta = preguna;
    }

    public String getRespuesta()
    {
        return respuesta;
    }

    public void setRespuesta(String respuesta)
    {
        this.respuesta = respuesta;
    }
}
