// Finance chart - dati placeholder
document.addEventListener("DOMContentLoaded", function () {
    const ctx = document.getElementById('financeChart');

    if (ctx) {
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['3 Apr', '4 Apr', '5 Apr', '6 Apr', '7 Apr', '8 Apr'],
                datasets: [
                    {
                        label: 'Entrate',
                        data: [200, 300, 250, 400, 350, 500], // Placeholder
                        borderColor: '#3b82f6',
                        fill: false,
                        tension: 0.3
                    },
                    {
                        label: 'Uscite',
                        data: [150, 200, 300, 250, 400, 300], // Placeholder
                        borderColor: '#ef4444',
                        fill: false,
                        tension: 0.3
                    }
                ]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { display: true }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
});
