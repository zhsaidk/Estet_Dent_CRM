document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);

    const error = urlParams.get('error');
    if (error) {
        const errorMessage = document.getElementById('error-message');
        errorMessage.textContent = decodeURIComponent(error);
        errorMessage.classList.add('show');
    }

    const message = urlParams.get('message');
    if (message) {
        const successMessage = document.getElementById('success-message');
        successMessage.textContent = decodeURIComponent(message);
        successMessage.classList.add('show');

        // Можно скрывать сообщение через 5 секунд
        setTimeout(() => {
            successMessage.classList.remove('show');
        }, 5000);
    }

    // Восстанавливаем имя пользователя при ошибке
    const username = urlParams.get('username');
    if (username) {
        document.getElementById('username').value = username;
    }
});
