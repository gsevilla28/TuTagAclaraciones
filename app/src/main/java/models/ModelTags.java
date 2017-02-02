package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 14/09/2016.
 */
public class ModelTags {

    @SerializedName("idcliente")

    private Integer idcliente;
    @SerializedName("alias")

    private String alias;
    @SerializedName("prefijo")

    private String prefijo;
    @SerializedName("numero")

    private String numero;
    @SerializedName("celular")

    private String celular;
    @SerializedName("email")

    private String email;
    @SerializedName("contrasena")

    private String contrasena;

    @SerializedName("tipopago")
    private Integer tipopago;

    /**
     *
     * @return
     * The idcliente
     */
    public Integer getIdcliente() {
        return idcliente;
    }

    /**
     *
     * @param idcliente
     * The idcliente
     */
    public void setIdcliente(Integer idcliente) {
        this.idcliente = idcliente;
    }

    /**
     *
     * @return
     * The alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     *
     * @param alias
     * The alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     *
     * @return
     * The prefijo
     */
    public String getPrefijo() {
        return prefijo;
    }

    /**
     *
     * @param prefijo
     * The prefijo
     */
    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    /**
     *
     * @return
     * The numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     *
     * @param numero
     * The numero
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     *
     * @return
     * The celular
     */
    public String getCelular() {
        return celular;
    }

    /**
     *
     * @param celular
     * The celular
     */
    public void setCelular(String celular) {
        this.celular = celular;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
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

     * The contrasena
     */
    public void setContrasena(int TipoPago) {
        this.contrasena = contrasena;
    }


    /**
     *
     * @return
     * The tipopago
     */
    public Integer getTipopago() {
        return tipopago;
    }

    /**
     *
     * @param tipopago
     * The tipopago
     */
    public void setTipopago(Integer tipopago) {
        this.tipopago = tipopago;
    }


}