package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 21/09/2016.
 */
public class modelCruces implements Serializable {

    @SerializedName("Carril")
    private String carril;

    @SerializedName("Caseta")
    private String caseta;

    @SerializedName("Clase")
    private Integer clase;

    @SerializedName("Ejes")
    private Integer ejes;

    @SerializedName("Fecha")
    private String fecha;

    @SerializedName("Hora")
    private String hora;

    @SerializedName("Monto")
    private Double monto;

    @SerializedName("Rodada")
    private Integer rodada;

    @SerializedName("TipoMovimiento")
    private Integer tipoMovimiento;

    @SerializedName("Tramo")
    private String tramo;

    @SerializedName("IdFolio")
    private String idFolio;

    /**
     *
     * @return
     * The carril
     */
    public String getCarril() {
        return carril;
    }

    /**
     *
     * @param carril
     * The Carril
     */
    public void setCarril(String carril) {
        this.carril = carril;
    }

    /**
     *
     * @return
     * The caseta
     */
    public String getCaseta() {
        return caseta;
    }

    /**
     *
     * @param caseta
     * The Caseta
     */
    public void setCaseta(String caseta) {
        this.caseta = caseta;
    }

    /**
     *
     * @return
     * The clase
     */
    public Integer getClase() {
        return clase;
    }

    /**
     *
     * @param clase
     * The Clase
     */
    public void setClase(Integer clase) {
        this.clase = clase;
    }

    /**
     *
     * @return
     * The ejes
     */
    public Integer getEjes() {
        return ejes;
    }

    /**
     *
     * @param ejes
     * The Ejes
     */
    public void setEjes(Integer ejes) {
        this.ejes = ejes;
    }

    /**
     *
     * @return
     * The fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     *
     * @param fecha
     * The Fecha
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     *
     * @return
     * The hora
     */
    public String getHora() {
        return hora;
    }

    /**
     *
     * @param hora
     * The Hora
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     *
     * @return
     * The monto
     */
    public Double getMonto() {
        return monto;
    }

    /**
     *
     * @param monto
     * The Monto
     */
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    /**
     *
     * @return
     * The rodada
     */
    public Integer getRodada() {
        return rodada;
    }

    /**
     *
     * @param rodada
     * The Rodada
     */
    public void setRodada(Integer rodada) {
        this.rodada = rodada;
    }

    /**
     *
     * @return
     * The tipoMovimiento
     */
    public Integer getTipoMovimiento() {
        return tipoMovimiento;
    }

    /**
     *
     * @param tipoMovimiento
     * The TipoMovimiento
     */
    public void setTipoMovimiento(Integer tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    /**
     *
     * @return
     * The tramo
     */
    public String getTramo() {
        return tramo;
    }

    /**
     *
     * @param tramo
     * The Tramo
     */
    public void setTramo(String tramo) {
        this.tramo = tramo;
    }

    /**
     *
     * @return
     * The idFolio
     */
    public String getIdFolio() {
        return idFolio;
    }

    /**
     *
     * @param idFolio
     * The IdFolio
     */
    public void setIdFolio(String idFolio) {
        this.idFolio = idFolio;
    }
}