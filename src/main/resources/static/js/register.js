document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('test-reg');
    console.log("Sono in test-reg");
    if (!form) {
        console.error("Form di login non trovato (ID 'register-form')");
        return;
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // Impedisce l'invio standard del form

        //const username = form.username.value;
       // const password = form.password.value;

        // Validazione semplice (opzionale)
        //if (!username || !password) {
        //    alert("Inserisci username e password.");
        //    return;
        //}

        // Invia credenziali al backend
        fetch('/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
            //body: new URLSearchParams({
            //    username: username,
            //    password: password
            })
        })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else if (response.ok) {
                alert('Login effettuato con successo!');
                form.reset();
            } else {
                alert('Credenziali errate o errore nel login.');
            }
        })
        .catch(error => {
            console.error('Errore nella richiesta:', error);
            alert('Errore di comunicazione con il server.');
        });
    });
});
