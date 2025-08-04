document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('userDropdown');

    if (!form) {
        console.error("Form di login non trovato (ID 'userDropdown')");
        return;
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // Impedisce l'invio standard del form

        // Invia credenziali al backend
        fetch('/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else if (response.ok) {
                alert('Logout effettuato con successo!');
                form.reset();
            } else {
                alert('Logout non effettuato.');
            }
        })
        .catch(error => {
            console.error('Errore nella richiesta:', error);
            alert('Errore di comunicazione con il server.');
        });
    });
});
