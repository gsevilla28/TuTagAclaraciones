package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 20/09/2016.
 */
public class ValidaPropiedadTag {

    @SerializedName("numtar")

    private String numtar;
    @SerializedName("primeros")

    private String primeros;
    @SerializedName("ultimos")

    private String ultimos;
    @SerializedName("success")

    private String success;

    /**
     *
     * @return
     * The numtar
     */
    public String getNumtar() {
        return numtar;
    }

    /**
     *
     * @param numtar
     * The numtar
     */
    public void setNumtar(String numtar) {
        this.numtar = numtar;
    }

    /**
     *
     * @return
     * The primeros
     */
    public String getPrimeros() {
        return primeros;
    }

    /**
     *
     * @param primeros
     * The primeros
     */
    public void setPrimeros(String primeros) {
        this.primeros = primeros;
    }

    /**
     *
     * @return
     * The ultimos
     */
    public String getUltimos() {
        return ultimos;
    }

    /**
     *
     * @param ultimos
     * The ultimos
     */
    public void setUltimos(String ultimos) {
        this.ultimos = ultimos;
    }

    /**
     *
     * @return
     * The success
     */
    public String getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(String success) {
        this.success = success;
    }

}