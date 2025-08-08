document.getElementById("registrationForm").addEventListener("submit", function (e) {
  console.log("Sono nel JS di register form");

  e.preventDefault();

  const form = e.target;

  const formData = new URLSearchParams();
  formData.append("nome", form.firstName.value);
  formData.append("cognome", form.lastName.value);
  formData.append("dataNascita", form.dob.value);
  formData.append("codiceFiscale", form.fiscalCode.value.toUpperCase());
  formData.append("email", form.email.value);
  formData.append("telefono", form.phone.value);
  formData.append("username", form.username.value);
  formData.append("password", form.password.value);

  fetch("/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    body: formData.toString()
  })
  .then(response => {
    return response.text().then(message => {
      if (response.ok) {
        alert("✅ Registrazione completata!");
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
