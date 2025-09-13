//Package dedicato alla validazione
package com.example.demo.dto;

//Import delle librerie Spring e di validazione
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

//Import librerie Java
import java.time.LocalDate;

public class RegisterRequest {

    @NotBlank(message = "Nome obbligatorio")
    @Size(min = 2, max = 50, message = "Il nome deve avere tra 2 e 50 caratteri")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Il nome può contenere solo lettere")
    private String nome;

    @NotBlank(message = "Cognome obbligatorio")
    @Size(min = 2, max = 50, message = "Il cognome deve avere tra 2 e 50 caratteri")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Il cognome può contenere solo lettere")
    private String cognome;

    @NotBlank(message = "Username obbligatorio")
    @Size(min = 3, max = 50, message = "Lo username deve avere tra 3 e 50 caratteri")
    private String username;

    @NotBlank(message = "Telefono obbligatorio")
    @Pattern(regexp = "^[0-9]{8,20}$", message = "Il telefono deve contenere solo numeri e avere tra 8 e 20 cifre")
    private String telefono;

    @Email(message = "Email non valida")
    private String email;

    @Pattern(regexp = "^[A-Z0-9]{16}$", message = "Il codice fiscale deve essere lungo 16 caratteri alfanumerici in maiuscolo")
    private String codiceFiscale;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascita;

    @NotBlank(message = "Password obbligatoria")
    @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "La password deve contenere almeno una maiuscola, una minuscola, un numero e un carattere speciale"
    )
    private String password;

    // getter e setter

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
