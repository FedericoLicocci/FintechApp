package com.example.demo.model;

//Import delle librerie Java e Spring
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

//Questa entità rappresenta un movimeto bancario
@Entity
@Table(name = "movements")
public class Movement {

    //Chiave primaria autoincrementata, identifica UNIVOCAMENTE la transazione
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionid;

    //Importo della transazione (Positivo o Negativo)
    @Column(nullable = false)
    private BigDecimal amount;

    //Data di esecuzione del bonifico
    @Column(name = "execution_date", nullable = false)
    private LocalDateTime executionDate;

    //Data di creazione del movimento
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //IBAN del mittente
    @Column(name = "iban_sender", nullable = false, length = 34)
    private String ibanSender;

    //IBAN del Destinatario
    @Column(name = "iban_receiver", nullable = false, length = 34)
    private String ibanReceiver;

    //Stato del movimento
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.completed;

    @Column(name = "causale", nullable = false)
    private String causale;


    /**
     * Possibili stati di un movimento:
     * - pending: in attesa di esecuzione
     * - scheduled: programmato per una data futura
     * - completed: eseguito correttamente
     * - failed: errore durante l’esecuzione
     */
    public enum Status {
        pending,
        scheduled,
        completed,
        failed
    }

    public Movement() {}

    // Getter e Setter

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDateTime executionDate) {
        this.executionDate = executionDate;
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
