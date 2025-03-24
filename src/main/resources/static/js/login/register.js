document.addEventListener('DOMContentLoaded', function() {
    const errorMessage = document.getElementById('error-message');

    // Если есть сообщение об ошибке или успехе от сервера, показываем его
    if (errorMessage && errorMessage.textContent.trim() !== '') {
        errorMessage.classList.add('visible');
    }

    // Очистка сообщения при вводе в поля
    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('input', () => {
            errorMessage.classList.remove('visible');
        });
    });
});