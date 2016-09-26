package models;

/**
 * Created by Administrator on 08/09/2016.
 */

import com.google.gson.annotations.SerializedName;



public class Login {

    @SerializedName("usuario")
    private String usuario;
    @SerializedName("contrasena")
    private String contrasena;
    @SerializedName("Login")
    private Integer login;

    /**
     *
     * @return
     * The usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     *
     * @param usuario
     * The usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     *
     * @return
     * The contrasena
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     *
     * @param contrasena
     * The contrasena
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     *
     * @return
     * The login
     */
    public Integer getLogin() {
        return login;
    }

    /**
     *
     * @param login
     * The Login
     */
    public void setLogin(Integer login) {
        this.login = login;
    }

}