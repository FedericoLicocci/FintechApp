//package com.example.demo.model;
//
////Import delle librerie Java e Spring
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
////Questa entità rappresenta un pagamento
//@Entity
//public class Payment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String receiver;
//
//    private Double amount;
//
//    private LocalDateTime date;
//
//    @ManyToOne
//    @JoinColumn(name = "utente_id", nullable = false)
//    private Utente utente; // 👈 Associazione con l'utente loggato
//
//    // Costruttori
//    public Payment() {}
//
//    public Payment(String receiver, Double amount, LocalDateTime date, Utente utente) {
//        this.receiver = receiver;
//        this.amount = amount;
//        this.date = date;
//        this.utente = utente;
//    }
//
//    // Getter e Setter
//    public Long getId() {
//        return id;
//    }
//
//    public String getReceiver() {
//        return receiver;
//    }
//
//    public void setReceiver(String receiver) {
//        this.receiver = receiver;
//    }
//
//    public Double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(Double amount) {
//        this.amount = amount;
//    }
//
//    public LocalDateTime getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDateTime date) {
//        this.date = date;
//    }
//
//    public Utente getUtente() {
//        return utente;
//    }
//
//    public void setUtente(Utente utente) {
//        this.utente = utente;
//    }
//}
