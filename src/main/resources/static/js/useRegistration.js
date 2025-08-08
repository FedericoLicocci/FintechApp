document.getElementById("registrationForm").addEventListener("submit", function (e) {

  console.log("Sono nel JS di register form");

  e.preventDefault();

  const form = e.target;

  const formData = new URLSearchParams();
  formData.append("nome", form.firstName.value);
  formData.append("cognome", form.lastName.value);
  formData.append("dataNascita", form.dob.value);
  formData.append("codiceFiscale", form.fiscalCode.value.toUpperCase()); // Normalizzazione ok
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
    if (response.redirected) {
      // Spring potrebbe fare redirect lato server dopo la registrazione
      window.location.href = response.url;
    } else if (response.ok) {
      alert("Registrazione completata!");
    } else {
      return response.text().then(msg => {
        alert("Errore: " + msg);
      });
    }
  })
  .catch(error => {
    console.error("Errore nella richiesta:", error);
    alert("Errore di connessione.");
  });
});
