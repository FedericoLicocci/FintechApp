package com.example.demo.controller;

import com.example.demo.model.Utente;
import com.example.demo.model.Movement;
import com.example.demo.model.conti;
import com.example.demo.repository.ContoRepository;
import com.example.demo.repository.UtenteRepository;
import com.example.demo.repository.MovementRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.service.ContoService;
import com.example.demo.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ContoService contoService;
    private final MovementService movementService;

    // Iniezione tramite costruttore (best practice con Spring)
    public HomeController(ContoService contoService, MovementService movementService) {
        this.contoService = contoService;
        this.movementService = movementService;
    }

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ContoRepository contiRepository;

    @Autowired
    private MovementRepository movementRepository;

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal CustomUserDetails CustomUserDetails) {

        String username = CustomUserDetails.getUsername();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        conti conto = contiRepository.findByUtenteId(utente.getId())
                .orElseThrow(() -> new RuntimeException("Conto non trovato"));

        String ibanUtente = conto.getNumeroConto();

        // Ultimi 5 movimenti
        List<Movement> lastPayments = movementRepository.findTop5ByIbanSenderOrderByDateDesc(ibanUtente);

        // Somma entrate e uscite basate sull'IBAN del mittente
        BigDecimal totaleEntrate = movementRepository.sumPositiveAmountByIbanSender(ibanUtente);
        BigDecimal totaleUscite = movementRepository.sumNegativeAmountByIbanSender(ibanUtente);

        if (totaleEntrate == null) totaleEntrate = BigDecimal.ZERO;
        if (totaleUscite == null) totaleUscite = BigDecimal.ZERO;

        BigDecimal saldo = contoService.getSaldoDisponibileUtenteCorrente();

        // Movimenti ultimi 30 giorni
        List<Movement> movements = movementService.getLast30DaysMovements(ibanUtente);

        // Raggruppa per giorno e somma gli importi
        Map<LocalDate, BigDecimal> dailySums = movements.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getDate().toLocalDate(), // estrai solo la data senza ora
                        TreeMap::new, // mantiene ordinati i giorni
                        Collectors.mapping(Movement::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        // Prepara liste per Chart.js
        List<String> chartLabels = dailySums.keySet().stream()
                .map(LocalDate::toString)
                .toList();

        List<BigDecimal> chartData = new ArrayList<>(dailySums.values());

        // Aggiungi attributi al modello
        model.addAttribute("saldo", saldo);
        model.addAttribute("lastPayments", lastPayments);
        model.addAttribute("entrate", totaleEntrate);
        model.addAttribute("uscite", totaleUscite);
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "home";
    }


}
