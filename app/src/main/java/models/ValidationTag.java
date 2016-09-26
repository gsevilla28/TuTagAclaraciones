package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 13/09/2016.
 */

public class ValidationTag {

    @SerializedName("validacion")

    private Integer validacion;
    @SerializedName("tabla")

    private String tabla;
    @SerializedName("tipoPago")

    private Integer tipoPago;

    /**
     *
     * @return
     * The validacion
     */
    public Integer getValidacion() {
        return validacion;
    }

    /**
     *
     * @param validacion
     * The validacion
     */
    public void setValidacion(Integer validacion) {
        this.validacion = validacion;
    }

    /**
     *
     * @return
     * The tabla
     */
    public String getTabla() {
        return tabla;
    }

    /**
     *
     * @param tabla
     * The tabla
     */
    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    /**
     *
     * @return
     * The tipoPago
     */
    public Integer getTipoPago() {
        return tipoPago;
    }

    /**
     *
     * @param tipoPago
     * The tipoPago
     */
    public void setTipoPago(Integer tipoPago) {
        this.tipoPago = tipoPago;
    }

}