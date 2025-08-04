document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('login-form');

    if (!form) {
        console.error("Form di login non trovato (ID 'login-form')");
        return;
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // Impedisce l'invio standard del form

        const username = form.username.value;
        const password = form.password.value;

        // Validazione semplice (opzionale)
        if (!username || !password) {
            alert("Inserisci username e password.");
            return;
        }

        // Invia credenziali al backend
        fetch('/process-login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                username: username,
                password: password
            })
        })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else if (response.ok) {
                alert('Registrazione effettuata con successo!');
                form.reset();
            } else {
                alert('Utente già presente nel sistema.');
            }
        })
        .catch(error => {
            console.error('Errore nella richiesta:', error);
            alert('Errore di comunicazione con il server.');
        });
    });
});
