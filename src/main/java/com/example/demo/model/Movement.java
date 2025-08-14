package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movements")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionid;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderid", nullable = false)
    private Utente sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverid", nullable = false)
    private Utente receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private Utente utente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.completed;

    @Column(name = "causale", nullable = false)
    private String causale;

    public enum Status {
        pending,
        completed,
        failed
    }

    public Movement() {}

    // Getters and Setters

    public int getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(int transactionid) {
        this.transactionid = transactionid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Utente getSender() {
        return sender;
    }

    public void setSender(Utente sender) {
        this.sender = sender;
    }

    public Utente getReceiver() {
        return receiver;
    }

    public void setReceiver(Utente receiver) {
        this.receiver = receiver;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCausale(String causale) {
        this.causale = causale;
    }

    public String getCausale() {
        return causale;
    }
}
