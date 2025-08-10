package com.example.demo.controller;

import com.example.demo.model.Movement;
import com.example.demo.model.Utente;
import com.example.demo.repository.MovementRepository;
import com.example.demo.repository.UtenteRepository;
import com.example.demo.service.MovementExportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MovementController {

    private final MovementRepository movementRepository;
    private final UtenteRepository utenteRepository;
    private final MovementExportService movementExportService;

    @Autowired
    public MovementController(MovementRepository movementRepository,
                              UtenteRepository utenteRepository,
                              MovementExportService movementExportService) {
        this.movementRepository = movementRepository;
        this.utenteRepository = utenteRepository;
        this.movementExportService = movementExportService;
    }

    @GetMapping("/movements")
    public List<Movement> getLastFiveMovements(Principal principal) {
        Optional<Utente> userOptional = utenteRepository.findByUsername(principal.getName());
        if (userOptional.isEmpty()) {
            return List.of();
        }
        Utente user = userOptional.get();
        return movementRepository.findTop5BySenderIdOrderByDateDesc(user.getId());
    }

    @GetMapping("/movements/export/pdf")
    public ResponseEntity<byte[]> exportMovementsToPdf(Principal principal) throws Exception {
        Optional<Utente> userOptional = utenteRepository.findByUsername(principal.getName());
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Utente user = userOptional.get();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        movementExportService.exportToPdf(user.getId(), baos);

        byte[] pdfBytes = baos.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movements.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/movements/export/excel")
    public ResponseEntity<byte[]> exportMovementsToExcel(Principal principal) throws Exception {
        Optional<Utente> userOptional = utenteRepository.findByUsername(principal.getName());
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Utente user = userOptional.get();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        movementExportService.exportToExcel(user.getId(), baos);

        byte[] excelBytes = baos.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movements.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
