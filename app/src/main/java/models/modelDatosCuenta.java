package models;

/**
 * Created by Administrator on 24/11/2016.
 */

public class modelDatosCuenta {

    public modelDatosCuenta(){
      /*  this.email=vemail;
        this.celular=vcelular;
        this.contrasena=vcontrasena;*/
    }

    private String celular;
    private String email;
    private String contrasena;

   public void setCelular(String vcelular){
        this.celular=vcelular;
    }

    public String getCelular(){
        return this.celular;
    }


    public void setEmail(String vemail){
        this.email=vemail;
    }

    public String getEmail(){
        return this.email;
    }

    public void setContrasena(String vcontrasena){
        this.contrasena=vcontrasena;
    }
    public String getContrasena(){
        return this.contrasena;
    }


}
