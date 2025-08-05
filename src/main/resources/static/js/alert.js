document.addEventListener("DOMContentLoaded", function() {
    const errorDiv = document.getElementById("validationErrors");
    if (!errorDiv) return;

    const hasErrors = errorDiv.getAttribute("data-has-errors") === 'true';
    if (!hasErrors) return;

    const errors = [];

    const nomeError = errorDiv.getAttribute("data-nome-error");
    if (nomeError) errors.push("Nome: " + nomeError);

    const passwordError = errorDiv.getAttribute("data-password-error");
    if (passwordError) errors.push("Password: " + passwordError);

    if (errors.length > 0) {
        alert("Errore di validazione:\n" + errors.join("\n"));
    }
});
