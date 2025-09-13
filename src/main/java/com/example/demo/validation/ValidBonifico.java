package com.example.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

//Annotazione personalizzata per validare i dati di un bonifico
@Target({ElementType.TYPE}) // si applica a livello di classe (DTO)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BonificoValidator.class)
@Documented
public @interface ValidBonifico {

    //Messaggio di errore di default
    String message() default "Dati bonifico non validi";

    //Permette di raggruppare più validazioni
    Class<?>[] groups() default {};

    //Permette l'aggiunta di metadati per scenari di validazione avanzati
    Class<? extends Payload>[] payload() default {};
}
