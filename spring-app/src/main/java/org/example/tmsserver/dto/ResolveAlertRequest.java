package org.example.tmsserver.dto;

public class ResolveAlertRequest {
    private Integer messageId;
    private String occurrenceType;

    public ResolveAlertRequest() {
    }

    public ResolveAlertRequest(Integer messageId, String occurrenceType) {
        this.messageId = messageId;
        this.occurrenceType = occurrenceType;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getOccurrenceType() {
        return occurrenceType;
    }

    public void setOccurrenceType(String occurrenceType) {
        this.occurrenceType = occurrenceType;
    }
}

