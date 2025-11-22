package org.example.tmsserver.service;

import org.example.tmsserver.dto.AlertResponseDTO;
import org.example.tmsserver.entity.Alert;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<AlertResponseDTO> getAllAlerts() {
        List<Alert> alerts = alertRepository.findAllByOrderByDateDesc();

        return alerts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AlertResponseDTO resolveAlert(Integer messageId, String occurrenceType) {
        Alert alert = alertRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + messageId));

        alert.setStatus(true);
        alert.setOcorrencia(occurrenceType);

        Alert savedAlert = alertRepository.save(alert);

        return convertToDTO(savedAlert);
    }

    public Alert createAlert(Region region, User user) {
        Alert alert = new Alert();
        alert.setStatus(false);  // Always false initially
        alert.setOcorrencia(null);  // Always null initially
        alert.setRegion(region);
        alert.setUser(user);
        alert.setDate(OffsetDateTime.now());

        return alertRepository.save(alert);
    }

    private AlertResponseDTO convertToDTO(Alert alert) {
        return new AlertResponseDTO(
                alert.getId(),
                alert.getStatus(),
                alert.getOcorrencia(),
                alert.getRegion().getName(),
                alert.getDate().format(FORMATTER),
                alert.getUser().getUsername()
        );
    }
}
