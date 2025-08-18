document.getElementById("BonificoForm").addEventListener("submit", function (e) {
  console.log("Sono nel JS di bonifico form");

  e.preventDefault();

  const form = e.target;

  const formData = new URLSearchParams();
  formData.append("IBAN", form.ibanDestinatario.value);
  formData.append("nomeCompleto", form.nomeDestinatario.value);
  formData.append("importo", form.importo.value);
  formData.append("causale", form.causale.value);
  formData.append("dataEsecuzione", form.dataEsecuzione.value);


  fetch("/payment", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    body: formData.toString()
  })
  .then(response => {
    return response.text().then(message => {
      if (response.ok) {
        alert("✅ Bonifico effettuato!");
        // Se vuoi, reindirizza alla pagina di login
        // window.location.href = "/login";
      } else {
        alert("❌ Errore: " + message);
      }
    });
  })
  .catch(error => {
    console.error("❌ Errore nella richiesta:", error);
    alert("❌ Errore di connessione al server.");
  });
});
