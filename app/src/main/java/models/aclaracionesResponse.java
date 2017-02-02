package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 02/11/2016.
 */

public class aclaracionesResponse {


        @SerializedName("idfolio")

        private String idfolio;
        @SerializedName("FechaAlta")

        private String fechaAlta;
        @SerializedName("FechaDictamen")

        private String fechaDictamen;
        @SerializedName("Status")

        private String status;
        @SerializedName("importeDevuelto")

        private Double importeDevuelto;
        @SerializedName("comentarioDictamen")

        private String comentarioDictamen;
        @SerializedName("fechaCruce")

        private String fechaCruce;
        @SerializedName("horaCruce")

        private String horaCruce;
        @SerializedName("importeCruce")

        private Double importeCruce;
        @SerializedName("nomCaseta")

        private String nomCaseta;

        /**
         *
         * @return
         * The idfolio
         */
        public String getIdfolio() {
            return idfolio;
        }

        /**
         *
         * @param idfolio
         * The idfolio
         */
        public void setIdfolio(String idfolio) {
            this.idfolio = idfolio;
        }

        /**
         *
         * @return
         * The fechaAlta
         */
        public String getFechaAlta() {
            return fechaAlta;
        }

        /**
         *
         * @param fechaAlta
         * The FechaAlta
         */
        public void setFechaAlta(String fechaAlta) {
            this.fechaAlta = fechaAlta;
        }

        /**
         *
         * @return
         * The fechaDictamen
         */
        public String getFechaDictamen() {
            return fechaDictamen;
        }

        /**
         *
         * @param fechaDictamen
         * The FechaDictamen
         */
        public void setFechaDictamen(String fechaDictamen) {
            this.fechaDictamen = fechaDictamen;
        }

        /**
         *
         * @return
         * The status
         */
        public String getStatus() {
            return status;
        }

        /**
         *
         * @param status
         * The Status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         *
         * @return
         * The importeDevuelto
         */
        public Double getImporteDevuelto() {
            return importeDevuelto;
        }

        /**
         *
         * @param importeDevuelto
         * The importeDevuelto
         */
        public void setImporteDevuelto(Double importeDevuelto) {
            this.importeDevuelto = importeDevuelto;
        }

        /**
         *
         * @return
         * The comentarioDictamen
         */
        public String getComentarioDictamen() {
            return comentarioDictamen;
        }

        /**
         *
         * @param comentarioDictamen
         * The comentarioDictamen
         */
        public void setComentarioDictamen(String comentarioDictamen) {
            this.comentarioDictamen = comentarioDictamen;
        }

        /**
         *
         * @return
         * The fechaCruce
         */
        public String getFechaCruce() {
            return fechaCruce;
        }

        /**
         *
         * @param fechaCruce
         * The fechaCruce
         */
        public void setFechaCruce(String fechaCruce) {
            this.fechaCruce = fechaCruce;
        }

        /**
         *
         * @return
         * The horaCruce
         */
        public String getHoraCruce() {
            return horaCruce;
        }

        /**
         *
         * @param horaCruce
         * The horaCruce
         */
        public void setHoraCruce(String horaCruce) {
            this.horaCruce = horaCruce;
        }

        /**
         *
         * @return
         * The importeCruce
         */
        public Double getImporteCruce() {
            return importeCruce;
        }

        /**
         *
         * @param importeCruce
         * The importeCruce
         */
        public void setImporteCruce(Double importeCruce) {
            this.importeCruce = importeCruce;
        }

        /**
         *
         * @return
         * The nomCaseta
         */
        public String getNomCaseta() {
            return nomCaseta;
        }

        /**
         *
         * @param nomCaseta
         * The nomCaseta
         */
        public void setNomCaseta(String nomCaseta) {
            this.nomCaseta = nomCaseta;
        }



}
