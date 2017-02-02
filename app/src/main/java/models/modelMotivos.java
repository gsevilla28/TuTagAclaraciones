package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 07/11/2016.
 */

public class modelMotivos {

    @SerializedName("IdMotivo")

    private Integer idMotivo;
    @SerializedName("Descripcion")

    private String descripcion;

    /**
     *
     * @return
     * The idMotivo
     */
    public Integer getIdMotivo() {
        return idMotivo;
    }

    /**
     *
     * @param idMotivo
     * The IdMotivo
     */
    public void setIdMotivo(Integer idMotivo) {
        this.idMotivo = idMotivo;
    }

    /**
     *
     * @return
     * The descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     *
     * @param descripcion
     * The Descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}