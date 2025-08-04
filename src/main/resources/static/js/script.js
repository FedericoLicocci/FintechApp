document.getElementById('paymentForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent default form submission

    const sender = event.target.sender.value;
    const amount = event.target.amount.value;
    const receiver = event.target.receiver.value;

    // Rimuovo l'alert e faccio la chiamata fetch al backend
    fetch('/make-payment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            sender: sender,
            amount: amount,
            receiver: receiver
        })
    })
    .then(response => {
        if (response.redirected) {
            window.location.href = response.url; // Segui il redirect (es. a index.html)
        } else if (response.ok) {
            alert('Pagamento effettuato con successo!');
            event.target.reset(); // Pulisce il form
        } else {
            alert('Errore durante il pagamento.');
        }
    })
    .catch(error => {
        console.error('Errore nella richiesta:', error);
        alert('Errore nella comunicazione col server.');
    });
});
