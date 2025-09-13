//Questo package si occupa della gestione dei controller
package com.example.demo.controller;

//Import delle classi del progetto
import com.example.demo.dto.BonificoRequest;
import com.example.demo.model.Movement;
import com.example.demo.model.Utente;
import com.example.demo.model.conti;
import com.example.demo.repository.MovementRepository;
import com.example.demo.repository.UtenteRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.MovementService;
import com.example.demo.service.ContoService;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.repository.ContoRepository;

//Import di librerie di Spring
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//Import librerie Java
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Optional;

//Questo controller gestisce la logica dei pagamenti (Bonifici e Movimenti)
@Controller
public class PaymentController {

    //Le principali dipendenze per la gestione dei movimenti
    private final MovementService movementService;
    private final UtenteRepository utenteRepository;
    private final MovementRepository movementRepository;

    //Iniezione tramite costruttore
    @Autowired
    public PaymentController(MovementService movementService,
                             UtenteRepository utenteRepository,
                             MovementRepository movementRepository) {
        this.movementService = movementService;
        this.utenteRepository = utenteRepository;
        this.movementRepository = movementRepository;
    }

//    @GetMapping("/payment")
//    public String paymentPage(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        model.addAttribute("username", username);
//
//        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
//        model.addAttribute("utente", userDetails.getUsername());
//
//        return "payment";
//    }

    @Autowired
    private ContoRepository contiRepository;

    //Rotta per la gestione dei bonifici (Istantanei e Ordinari)
    @PostMapping("/payment")
    @ResponseBody
    public ResponseEntity<String> Payment(
            @Valid @ModelAttribute("bonificoRequest") BonificoRequest request,
            BindingResult result
    ) {

        //System.out.println("Sono in payment...");

        //Controlla se ci sono errori
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Errore di validazione:\n");
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.badRequest().body(errorMessage.toString().trim());
        }

        //Recupero delle informazioni sull'utente autenticato
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        //Recupero il conto dell’utente
        conti conto = contiRepository.findByUtenteId(utente.getId())
                .orElseThrow(() -> new RuntimeException("Conto non trovato"));


        BigDecimal saldoDisponibile = conto.getSaldoDisponibile();
        BigDecimal importo = request.getImporto();

        //System.out.println("Il saldo è: " + saldoDisponibile + " e l'importo da scalare è di: " + importo);

        //Controlla se il saldo è sufficiente per eseguire l'operazione
        if (saldoDisponibile.compareTo(importo) < 0) {
            return ResponseEntity.badRequest().body("Saldo insufficiente!");
        }

        //Creazione dei dati del movimento bancario
        Movement movimento = new Movement();
        movimento.setAmount(importo.negate());
        movimento.setIbanSender(conto.getNumeroConto());
        movimento.setIbanReceiver(request.getIBAN());
        movimento.setCausale(request.getCausale());

        //Verifica se il bonifico è istantaneo oppure no
        if (request.isInstant()) {
            //Caso 1: Bonifico istantaneo, viene eseguito subito
            conto.setSaldoDisponibile(saldoDisponibile.subtract(importo));
            contiRepository.save(conto);

            //vengono generati gli ultimi dati inerenti al movimento bancario
            movimento.setCreatedAt(LocalDateTime.now());
            movimento.setExecutionDate(LocalDateTime.now());
            movimento.setStatus(Movement.Status.completed);

            System.out.println("Bonifico istantaneo eseguito subito. Nuovo saldo: " + conto.getSaldoDisponibile());
        } else if (request.getDataEsecuzione().isEqual(ChronoLocalDate.from(LocalDateTime.now()))) {
        //Caso 2: Bonifico ordinario con data odierna ed esecuzione al giorno successivo alle 16
        // vengono generati gli ultimi dati inerenti al movimento bancario
        movimento.setCreatedAt(LocalDateTime.now());
        movimento.setExecutionDate(request.getDataEsecuzione().plusDays(1).atTime(16, 0));
        movimento.setStatus(Movement.Status.scheduled);

        System.out.println("Bonifico ordinario programmato per il " + movimento.getExecutionDate());
    } else {
        // Caso 3: Bonifico ordinario con data futura, esecuzione nel giorno scelto alle 16
        //vengono generati gli ultimi dati inerenti al movimento bancario
        movimento.setCreatedAt(LocalDateTime.now());
        movimento.setExecutionDate(request.getDataEsecuzione().atTime(16, 0));
        movimento.setStatus(Movement.Status.scheduled);

        //System.out.println("Bonifico ordinario programmato per il " + movimento.getExecutionDate());
    }

        //Salvataggio del movimento sul DB
        movementRepository.save(movimento);
        System.out.println("Movimento registrato con ID: " + movimento.getTransactionid());

        //Messaggi di output nel caso di Successo o Fallimento
        return ResponseEntity.ok(
                request.isInstant()
                        ? "✅ Bonifico istantaneo eseguito con successo!"
                        : "📅 Bonifico ordinario programmato per " + request.getDataEsecuzione()
        );
    }

    //Rotta per gli ultimi movimenti bancari eseguiti dall'utente
    @GetMapping("/lastmovements")
    public String showLastMovements(Model model) {

        //Recupera informazioni sull'utente corrente
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Recupera il conto dell'utente per ottenere l'IBAN
        conti conto = contiRepository.findByUtenteId(utente.getId())
                .orElseThrow(() -> new RuntimeException("Conto non trovato"));

        String ibanUtente = conto.getNumeroConto();

        // Recupera gli ultimi 5 movimenti usando l'IBAN
        List<Movement> lastPayments = movementRepository.findTop5ByIbanSenderOrderByExecutionDateDesc(ibanUtente);

        model.addAttribute("lastPayments", lastPayments);

        return "lastmovements.html";
    }

    //Pagina dedicata a tutte le modalità di pagamento
    @GetMapping("/pagamenti")
    public String paytest(Model model) {
        System.out.println("Sono in paytest...");
        return "pagamenti";
    }

    //pagina con il form per il bonifico
    @GetMapping("/bonifico/terzi")
    public String bonificoTerzi(Model model) {
        System.out.println("Sono in bonifico terzi...");
        return "bonifico-terzi";
    }

    //Pagina di autenticazione utente
    @GetMapping("/auth")
    public String auth(Model model) {
        System.out.println("Sono in auth...");
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth";
    }
}
