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

    @Column(name = "iban_sender", nullable = false, length = 34)
    private String ibanSender;

    @Column(name = "iban_receiver", nullable = false, length = 34)
    private String ibanReceiver;

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

    public String getIbanSender() {
        return ibanSender;
    }

    public void setIbanSender(String ibanSender) {
        this.ibanSender = ibanSender;
    }

    public String getIbanReceiver() {
        return ibanReceiver;
    }

    public void setIbanReceiver(String ibanReceiver) {
        this.ibanReceiver = ibanReceiver;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCausale() {
        return causale;
    }

    public void setCausale(String causale) {
        this.causale = causale;
    }
}
