document.getElementById("BonificoForm").addEventListener("submit", function (e) {
  console.log("Sono nel JS di bonifico form");

  e.preventDefault();

  const form = e.target;
  const instantToggle = document.getElementById("instant");
  const dataField = document.getElementById("dataEsecuzione");

  function toggleDateField() {
    if (instantToggle.checked) {
      dataField.disabled = true;
      dataField.value = ""; // resetto il valore se era stato messo
      dataField.removeAttribute("required");
    } else {
      dataField.disabled = false;
      dataField.setAttribute("required", "required");
    }
  }

  // inizializzo lo stato all'apertura pagina
  toggleDateField();

  // cambio comportamento quando l’utente tocca lo switch
  instantToggle.addEventListener("change", toggleDateField);

  const formData = new URLSearchParams();
  formData.append("IBAN", form.ibanDestinatario.value);
  formData.append("nomeCompleto", form.nomeDestinatario.value);
  formData.append("importo", form.importo.value);
  formData.append("causale", form.causale.value);

  // aggiungo il valore del toggle
  formData.append("instant", instantToggle.checked);

  // aggiungo la data SOLO se ordinario
  if (!instantToggle.checked) {
    formData.append("dataEsecuzione", form.dataEsecuzione.value);
  }

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
        // window.location.href = "/home"; // se vuoi redirect
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
