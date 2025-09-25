package org.example.tmsserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class RadarApiResponse {
    private Integer count;
    private DateRange date_range;
    private List<RadarData> data;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public DateRange getDate_range() {
        return date_range;
    }

    public void setDate_range(DateRange date_range) {
        this.date_range = date_range;
    }

    public List<RadarData> getData() {
        return data;
    }

    public void setData(List<RadarData> data) {
        this.data = data;
    }

    public static class DateRange {
        private String start;
        private String end;

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }

    public static class RadarData {
        @JsonProperty("camera_numero")
        private String cameraNumero;
        @JsonProperty("DataHoraTz")
        private String dataHoraTz;
        @JsonProperty("tipoVeiculo")
        private String tipoVeiculo;
        @JsonProperty("velocidade")
        private Integer velocidade;
        @JsonProperty("camera_latitude")
        private BigDecimal latitude;
        @JsonProperty("camera_longitude")
        private BigDecimal longitude;
        @JsonProperty("endereco")
        private String endereco;
        @JsonProperty("velocidadeRegulamentada")
        private Integer limite;

        public Integer getLimite() {
            return limite;
        }

        public void setLimite(Integer limite) {
            this.limite = limite;
        }

        public BigDecimal getLatitude() {
            return latitude;
        }

        public void setLatitude(BigDecimal latitude) {
            this.latitude = latitude;
        }

        public BigDecimal getLongitude() {
            return longitude;
        }

        public void setLongitude(BigDecimal longitude) {
            this.longitude = longitude;
        }

        public String getEndereco() {
            return endereco;
        }

        public void setEndereco(String endereco) {
            this.endereco = endereco;
        }

        public String getCameraNumero() {
            return cameraNumero;
        }

        public void setCameraNumero(String cameraNumero) {
            this.cameraNumero = cameraNumero;
        }

        public String getDataHoraTz() {
            return dataHoraTz;
        }

        public void setDataHoraTz(String dataHoraTz) {
            this.dataHoraTz = dataHoraTz;
        }

        public String getTipoVeiculo() {
            return tipoVeiculo;
        }

        public void setTipoVeiculo(String tipoVeiculo) {
            this.tipoVeiculo = tipoVeiculo;
        }

        public Integer getVelocidade() {
            return velocidade;
        }

        public void setVelocidade(Integer velocidade) {
            this.velocidade = velocidade;
        }
    }
}