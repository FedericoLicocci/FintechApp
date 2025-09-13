//Package dedicato alla gestione dei controller
package com.example.demo.controller;

// Import di classi definite all'interno del progetto
// (model, repository, security e servizi personalizzati)
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

// Import di librerie fornite da Spring Framework
// (annotazioni, gestione sicurezza, controller e rendering di pagine con Thymeleaf)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Import di classi della libreria standard Java
// (gestione numeri decimali, utenti loggati, date e collezioni)
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//Definizione del controller che gestisce le richieste HTTP
@Controller
public class HomeController {

    // Servizi iniettati da Spring
    // ContoService -> logica per la gestione dei conti
    // MovementService -> logica per i movimenti e transazioni
    private final ContoService contoService;
    private final MovementService movementService;

    // Iniezione tramite costruttore
    public HomeController(ContoService contoService, MovementService movementService) {
        this.contoService = contoService;
        this.movementService = movementService;
    }

    // Repository per interagire direttamente con il database.
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ContoRepository contiRepository;

    @Autowired
    private MovementRepository movementRepository;


    // Il metodo seguente gestisce la rotta GET "/home"
    // Nel momento in cui un utente viene autenticato viene caricata la dashboard.
    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal CustomUserDetails CustomUserDetails) {

        // Recupero delle informazioni dell’utente loggato
        String username = CustomUserDetails.getUsername();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Recupero del conto associato all’utente
        conti conto = contiRepository.findByUtenteId(utente.getId())
                .orElseThrow(() -> new RuntimeException("Conto non trovato"));
        String ibanUtente = conto.getNumeroConto();

        // Ultimi 5 movimenti sul conto
        List<Movement> lastPayments = movementRepository.findTop5ByIbanSenderOrderByExecutionDateDesc(ibanUtente);

        // Somma entrate e uscite basate sull'IBAN del mittente
        BigDecimal totaleEntrate = movementRepository.sumPositiveAmountByIbanSender(ibanUtente);
        BigDecimal totaleUscite = movementRepository.sumNegativeAmountByIbanSender(ibanUtente);

        // Gestione del caso in cui non esistano movimenti, per evitare valori nulli
        if (totaleEntrate == null) totaleEntrate = BigDecimal.ZERO;
        if (totaleUscite == null) totaleUscite = BigDecimal.ZERO;

        BigDecimal saldo = contoService.getSaldoDisponibileUtenteCorrente();

        // Movimenti ultimi 30 giorni
        List<Movement> movements = movementService.getLast30DaysMovements(ibanUtente);

        // Raggruppa per giorno e somma gli importi
        Map<LocalDate, BigDecimal> dailySums = movements.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getExecutionDate().toLocalDate(), // estrai solo la data senza ora
                        TreeMap::new, // mantiene ordinati i giorni
                        Collectors.mapping(Movement::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        // Prepara liste per Chart.js
        List<String> chartLabels = dailySums.keySet().stream()
                .map(LocalDate::toString)
                .toList();

        List<BigDecimal> chartData = new ArrayList<>(dailySums.values());

        // Passa gli attributi al modello per renderli disponibili a Thymeleaf
        model.addAttribute("saldo", saldo);
        model.addAttribute("lastPayments", lastPayments);
        model.addAttribute("entrate", totaleEntrate);
        model.addAttribute("uscite", totaleUscite);
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "home";
    }


}
