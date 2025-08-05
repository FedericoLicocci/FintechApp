package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Nome obbligatorio")
    @Size(min = 2, max = 50, message = "Il nome deve avere tra 2 e 50 caratteri")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Il nome può contenere solo lettere")
    private String nome;

    @NotBlank(message = "Password obbligatoria")
    @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "La password deve contenere almeno una maiuscola, una minuscola, un numero e un carattere speciale"
    )
    private String password;

    // Getters and Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
