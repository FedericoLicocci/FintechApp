//Package dedicato alla validazione delle informazioni
package com.example.demo.dto;

//Import librerie spring
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

//Import librerie Java
import java.math.BigDecimal;
import java.time.LocalDate;

//Dichiarazione classe pubblica
public class BonificoRequest {

    @NotBlank(message = "L'IBAN non può essere vuoto\n")
    @Pattern(
            regexp = "^IT\\d{2}[A-Z0-9]{23}$",
            message = "L'IBAN deve rispettare il formato italiano"
    )
    private String IBAN;

    @NotBlank(message = "Il nome completo non può essere vuoto\n")
    @Size(min = 2, max = 50, message = "Il nome completo deve essere tra 2 e 50 caratteri")
    @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s']+$",
            message = "Il nome completo può contenere solo lettere e spazi"
    )
    private String nomeCompleto;

    @NotNull(message = "L'importo è obbligatorio")
    @DecimalMin(value = "1.00", message = "L'importo minimo è 1 euro")
    @Digits(integer = 15, fraction = 2, message = "L'importo deve avere massimo 15 cifre intere e 2 decimali")
    private BigDecimal importo;

    @NotBlank(message = "La causale non può essere vuota")
    @Size(min = 5, max = 140, message = "La causale deve essere tra 5 e 140 caratteri")
    private String causale;

    @FutureOrPresent(message = "La data di esecuzione non può essere nel passato")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEsecuzione;

    private boolean instant; // toggle switch (true = immediato, false = ordinario)

    // Getters e Setters per le variabili dichiarate
    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public String getCausale() {
        return causale;
    }

    public void setCausale(String causale) {
        this.causale = causale;
    }

    public LocalDate getDataEsecuzione() {
        return dataEsecuzione;
    }

    public void setDataEsecuzione(LocalDate dataEsecuzione) {
        this.dataEsecuzione = dataEsecuzione;
    }

    public boolean isInstant() { return instant; }

    public void setInstant(boolean instant) { this.instant = instant ; }
}



