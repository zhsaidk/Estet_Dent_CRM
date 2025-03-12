document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registrationForm');
    const errorContainer = document.getElementById('errorContainer');
    const successContainer = document.getElementById('successContainer');

    // Показываем сообщение из URL (если есть)
    const urlParams = new URLSearchParams(window.location.search);

    const message = urlParams.get('message');
    if (message) {
        successContainer.textContent = decodeURIComponent(message);
        successContainer.style.display = 'block';
    }

    const error = urlParams.get('error');
    if (error) {
        const errorElement = document.createElement('div');
        errorElement.className = 'error';
        errorElement.textContent = decodeURIComponent(error);
        errorContainer.appendChild(errorElement);
    }

    // Валидация и отправка формы
    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        // Очистка сообщений
        errorContainer.innerHTML = '';
        successContainer.style.display = 'none';

        const errors = [];

        // Считываем данные
        const doctorName = document.getElementById('doctorName').value.trim();
        const email = document.getElementById('email').value.trim();
        const login = document.getElementById('login').value.trim();
        const password = document.getElementById('password').value.trim();
        const confirmPassword = document.getElementById('confirmPassword').value.trim();

        // Проверки на клиенте
        if (doctorName.length < 2) {
            errors.push('Имя должно содержать не менее 2 символов');
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            errors.push('Пожалуйста, введите корректный email адрес');
        }

        if (login.length < 3) {
            errors.push('Логин должен содержать не менее 3 символов');
        }

        if (password.length < 6) {
            errors.push('Пароль должен содержать не менее 6 символов');
        }

        if (password !== confirmPassword) {
            errors.push('Пароли не совпадают');
        }

        // Показываем ошибки, если есть
        if (errors.length > 0) {
            errors.forEach(error => {
                const errorElement = document.createElement('div');
                errorElement.className = 'error';
                errorElement.textContent = error;
                errorContainer.appendChild(errorElement);
            });
            return;
        }

        // Отправляем форму на сервер
        try {
            const response = await fetch('/register', {
                method: 'POST',
                body: new FormData(form),
            });

            if (response.redirected) {
                window.location.href = response.url;
            }
        } catch (error) {
            const errorElement = document.createElement('div');
            errorElement.className = 'error';
            errorElement.textContent = 'Произошла ошибка при отправке формы';
            errorContainer.appendChild(errorElement);
        }
    });
});
