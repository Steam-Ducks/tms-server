package org.example.tmsserver.dto;

public class AlertResponseDTO {
    private Integer id;
    private Boolean status;
    private String ocorrencia;
    private String location;
    private String date;
    private String destinatario;

    public AlertResponseDTO() {
    }

    public AlertResponseDTO(Integer id, Boolean status, String ocorrencia, String location, String date, String destinatario) {
        this.id = id;
        this.status = status;
        this.ocorrencia = ocorrencia;
        this.location = capitalizeWords(location);
        this.date = date;
        this.destinatario = destinatario;
    }

    private String capitalizeWords(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }

        // Special case mapping
        String normalizedInput = input.toLowerCase().trim();
        if (normalizedInput.equals("centro")) {
            return "Zona Central";
        }

        String[] words = normalizedInput.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }

            String word = words[i];
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1));
            }
        }

        return result.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(String ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
}
