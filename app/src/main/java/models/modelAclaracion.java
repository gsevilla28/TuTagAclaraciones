package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 11/10/2016.
 */
public class modelAclaracion {


        @SerializedName("clatran")

        private String clatran;
        @SerializedName("numtar")

        private String numtar;
        @SerializedName("tipousr")

        private String tipousr;
        @SerializedName("importe")

        private Integer importe;
        @SerializedName("idmotivo")

        private Integer idmotivo;
        @SerializedName("fecha")

        private String fecha;
        @SerializedName("hora")

        private String hora;
        @SerializedName("ncarril")

        private String ncarril;
        @SerializedName("comentario")

        private String comentario;

        /**
         *
         * @return
         * The clatran
         */
        public String getClatran() {
            return clatran;
        }

        /**
         *
         * @param clatran
         * The clatran
         */
        public void setClatran(String clatran) {
            this.clatran = clatran;
        }

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
         * The tipousr
         */
        public String getTipousr() {
            return tipousr;
        }

        /**
         *
         * @param tipousr
         * The tipousr
         */
        public void setTipousr(String tipousr) {
            this.tipousr = tipousr;
        }

        /**
         *
         * @return
         * The importe
         */
        public Integer getImporte() {
            return importe;
        }

        /**
         *
         * @param importe
         * The importe
         */
        public void setImporte(Integer importe) {
            this.importe = importe;
        }

        /**
         *
         * @return
         * The idmotivo
         */
        public Integer getIdmotivo() {
            return idmotivo;
        }

        /**
         *
         * @param idmotivo
         * The idmotivo
         */
        public void setIdmotivo(Integer idmotivo) {
            this.idmotivo = idmotivo;
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
         * The fecha
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
         * The hora
         */
        public void setHora(String hora) {
            this.hora = hora;
        }

        /**
         *
         * @return
         * The ncarril
         */
        public String getNcarril() {
            return ncarril;
        }

        /**
         *
         * @param ncarril
         * The ncarril
         */
        public void setNcarril(String ncarril) {
            this.ncarril = ncarril;
        }

        /**
         *
         * @return
         * The comentario
         */
        public String getComentario() {
            return comentario;
        }

        /**
         *
         * @param comentario
         * The comentario
         */
        public void setComentario(String comentario) {
            this.comentario = comentario;
        }


}
