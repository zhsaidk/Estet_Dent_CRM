document.addEventListener('DOMContentLoaded', function() {
    // Check for error message in URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    const errorMessage = urlParams.get('error_message');

    // Display error message if present
    if (errorMessage) {
        const errorDiv = document.getElementById('error-message');
        errorDiv.textContent = "Неверные учетные данные";
        errorDiv.classList.add('visible');
    }

    // Optional: Clear error message when user starts typing
    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('input', () => {
            const errorDiv = document.getElementById('error-message');
            errorDiv.classList.remove('visible');
        });
    });
});