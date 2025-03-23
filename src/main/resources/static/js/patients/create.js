// Initialize phone number mask
const phoneInput = document.getElementById('phone');
const maskOptions = {
    mask: '+7 (000) 000-00-00'
};
const phoneMask = IMask(phoneInput, maskOptions);

// Form handling
const form = document.getElementById('patientForm');
const messageElement = document.getElementById('message');

// Add basic form validation
const inputs = form.querySelectorAll('input');
inputs.forEach(input => {
    input.addEventListener('input', () => {
        if (input.validity.valid) {
            input.style.borderColor = '#ddd';
        } else {
            input.style.borderColor = '#d32f2f';
        }
    });
});

form.addEventListener('submit', (e) => {
    const phoneInput = document.getElementById('phone');

    // Добавляем + перед значением номера
    phoneInput.value = '+' + phoneMask.unmaskedValue;

    // Форма отправляется дальше
});
