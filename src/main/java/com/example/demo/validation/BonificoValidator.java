package com.example.demo.validation;


import com.example.demo.dto.BonificoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


//Implementa la validazione personalizzata, applicata su oggetti BonificoRequest
public class BonificoValidator implements ConstraintValidator<ValidBonifico, BonificoRequest> {

    @Override
    public boolean isValid(BonificoRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true; // niente da validare
        }

        // Caso 1: Bonifico istantaneo, la dataEsecuzione deve essere NULL
        if (request.isInstant()) {
            if (request.getDataEsecuzione() != null) {
                //Disabilita il messaggio di default definito in @ValidBonifico
                context.disableDefaultConstraintViolation();
                //Genera un messaggio specifico per il campo "dataEsecuzione"
                context.buildConstraintViolationWithTemplate("La data non deve essere impostata per bonifici istantanei")
                        .addPropertyNode("dataEsecuzione")
                        .addConstraintViolation();
                return false;
            }
        }
        // Caso 2: Bonifico ordinario, la dataEsecuzione obbligatoria
        else {
            if (request.getDataEsecuzione() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("La data di esecuzione è obbligatoria per bonifici ordinari")
                        .addPropertyNode("dataEsecuzione")
                        .addConstraintViolation();
                return false;
            }
        }

        //Se nessuna condizione di errore è stata rilevata il bonifico risulta valido
        return true;
    }
}
