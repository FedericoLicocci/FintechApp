package com.example.demo.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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
    @Min(value = 1, message = "L'importo minimo è 1 euro")
    @Digits(integer = 15, fraction = 2, message = "L'importo deve avere massimo 15 cifre intere e 2 decimali")
    private Integer importo;

    @NotBlank(message = "La causale non può essere vuota")
    @Size(min = 5, max = 140, message = "La causale deve essere tra 5 e 140 caratteri")
    private String causale;

    @FutureOrPresent(message = "La data di esecuzione non può essere nel passato")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEsecuzione;

    // Getters & Setters
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

    public Integer getImporto() {
        return importo;
    }

    public void setImporto(Integer importo) {
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
}
