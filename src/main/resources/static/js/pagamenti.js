// pagamenti.js - piccole migliorie di accessibilità e interazione
document.addEventListener('DOMContentLoaded', function () {
    // Rendiamo 'Enter' attivo sulle card che sono anchors (per accessibilità)
    document.querySelectorAll('.payment-card').forEach(card => {
        card.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' || e.key === ' ') {
                const link = card.closest('a') || card;
                // se è un <a>, segui il link; altrimenti ignora
                if (link && link.tagName === 'A' && link.href) {
                    window.location.href = link.href;
                }
            }
        });
    });

    // Tiny touch feedback: aggiunge una classe 'pressed' e la rimuove subito dopo
    document.querySelectorAll('.payment-card, .small-btn').forEach(el => {
        el.addEventListener('pointerdown', () => el.classList.add('pressed'));
        el.addEventListener('pointerup', () => setTimeout(() => el.classList.remove('pressed'), 150));
        el.addEventListener('pointerleave', () => el.classList.remove('pressed'));
    });
});
