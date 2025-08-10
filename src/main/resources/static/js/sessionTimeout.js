let logoutTimer;
let warningTimer;

// Configura tempi
const TIMEOUT = 15 * 60 * 1000; // 15 minuti
const WARNING = 14 * 30 * 1000; // Avviso al minuto 14

function resetTimer() {
clearTimeout(logoutTimer);
clearTimeout(warningTimer);

// Mostra avviso 1 minuto prima
warningTimer = setTimeout(() => {
alert("La sessione scadrà tra 1 minuto per inattività.");
}, WARNING);

// Esegue logout alla scadenza
logoutTimer = setTimeout(() => {
window.location.href = '/logout';
}, TIMEOUT);
}

// Eventi che resettano il timer
window.onload = resetTimer;
document.onmousemove = resetTimer;
document.onkeydown = resetTimer;
document.onscroll = resetTimer;
document.onclick = resetTimer;
