package org.example.tmsserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RadarApiResponse {
    private int count;
    private DateRange date_range;
    private List<RadarData> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
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
        private int velocidade;

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

        public int getVelocidade() {
            return velocidade;
        }

        public void setVelocidade(int velocidade) {
            this.velocidade = velocidade;
        }
    }
}