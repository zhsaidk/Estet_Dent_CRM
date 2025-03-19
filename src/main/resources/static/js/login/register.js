// Form handling
const form = document.getElementById('registrationForm');
const errorMessage = document.getElementById('error-message');

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Clear previous errors
    errorMessage.textContent = '';
    errorMessage.classList.remove('visible', 'success');

    try {
        const response = await fetch('/register', {
            method: 'POST',
            body: new FormData(form)
        });

        if (response.redirected) {
            window.location.href = response.url;
            return;
        }

        const result = await response.json();

        if (!response.ok) {
            if (result.errors) {
                // Display validation errors
                const errorMessages = result.errors.map(error => error.defaultMessage).join('<br>');
                errorMessage.innerHTML = errorMessages;
            } else if (result.error) {
                // Display login taken error
                errorMessage.textContent = result.error;
            } else {
                throw new Error('Произошла ошибка при регистрации');
            }
            errorMessage.classList.add('visible');
        } else if (result.message) {
            // Display success message
            errorMessage.textContent = result.message;
            errorMessage.classList.add('visible', 'success');
            setTimeout(() => {
                window.location.href = '/login';
            }, 2000);
        }

    } catch (error) {
        errorMessage.textContent = error.message || 'Произошла ошибка при регистрации';
        errorMessage.classList.add('visible');
    }
});

// Basic form validation
form.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', () => {
        const formGroup = input.closest('.form-group');
        if (input.validity.valid) {
            formGroup.classList.remove('error');
        } else {
            formGroup.classList.add('error');
        }
    });
});