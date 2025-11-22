package org.example.tmsserver.controller;

import org.example.tmsserver.dto.AlertResponseDTO;
import org.example.tmsserver.dto.ResolveAlertRequest;
import org.example.tmsserver.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<List<AlertResponseDTO>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @PostMapping("/resolve")
    public ResponseEntity<AlertResponseDTO> resolveAlert(@RequestBody ResolveAlertRequest request) {
        AlertResponseDTO resolvedAlert = alertService.resolveAlert(
                request.getMessageId(),
                request.getOccurrenceType()
        );
        return ResponseEntity.ok(resolvedAlert);
    }
}
