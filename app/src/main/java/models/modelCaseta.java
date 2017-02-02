package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 14/11/2016.
 */

public class modelCaseta {
    @SerializedName("nombreCaseta")
    private String nombreCaseta;

    /**
     *
     * @return
     * The nombreCaseta
     */
    public String getNombreCaseta() {
        return nombreCaseta;
    }

    /**
     *
     * @param nombreCaseta
     * The nombreCaseta
     */
    public void setNombreCaseta(String nombreCaseta) {
        this.nombreCaseta = nombreCaseta;
    }



}