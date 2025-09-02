package com.example.demo.controller;

import com.example.demo.model.Movement;
import com.example.demo.model.Utente;
import com.example.demo.model.conti;
import com.example.demo.repository.ContoRepository;
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

    @Autowired
    private ContoRepository contiRepository;

    @GetMapping("/movements")
    public List<Movement> getLastFiveMovements(Principal principal) {
        Optional<Utente> userOptional = utenteRepository.findByUsername(principal.getName());
        if (userOptional.isEmpty()) {
            return List.of();
        }
        Utente user = userOptional.get();

        // Recupera il conto dell'utente per prendere l'IBAN
        conti conto = contiRepository.findByUtenteId(user.getId())
                .orElseThrow(() -> new RuntimeException("Conto non trovato"));

        String ibanUtente = conto.getNumeroConto();

        // Restituisce gli ultimi 5 movimenti filtrati per IBAN
        return movementRepository.findTop5ByIbanSenderOrderByDateDesc(ibanUtente);
    }


    @GetMapping("/movements/export/pdf")
    public ResponseEntity<byte[]> exportMovementsToPdf(Principal principal) throws Exception {
        Optional<Utente> userOptional = utenteRepository.findByUsername(principal.getName());
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Utente user = userOptional.get();

        // Recupera il conto dell'utente per ottenere l'IBAN
        conti conto = contiRepository.findByUtenteId(user.getId())
                .orElseThrow(() -> new RuntimeException("Conto non trovato"));

        String ibanUtente = conto.getNumeroConto();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        movementExportService.exportToPdf(ibanUtente, baos);

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

        // Recupera il conto dell'utente per ottenere l'IBAN
        conti conto = contiRepository.findByUtenteId(user.getId())
                .orElseThrow(() -> new RuntimeException("Conto non trovato"));

        String ibanUtente = conto.getNumeroConto();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        movementExportService.exportToExcel(ibanUtente, baos);

        byte[] excelBytes = baos.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movements.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

}
