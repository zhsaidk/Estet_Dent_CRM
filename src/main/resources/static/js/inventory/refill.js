document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('refillForm');
    const errorToast = document.getElementById('error-toast');
    const successToast = document.getElementById('success-toast');
    const countInput = document.getElementById('count');
    const totalAmountSpan = document.getElementById('totalAmount');

    // Get current amount from the page
    const currentAmount = parseInt(document.querySelector('.current-amount span').textContent) || 0;

    // Update total amount preview when input changes
    countInput.addEventListener('input', () => {
        const inputValue = parseInt(countInput.value) || 0;
        if (inputValue > 0) {
            const newTotal = currentAmount + inputValue;
            totalAmountSpan.textContent = newTotal;
        } else {
            totalAmountSpan.textContent = '-';
        }
    });

    // Check for Thymeleaf messages on page load
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('message')) {
        showSuccess(decodeURIComponent(urlParams.get('message')));
    }
    if (urlParams.has('error')) {
        showError(decodeURIComponent(urlParams.get('error')));
    }

    form.addEventListener('submit', async (e) => {
        const inputValue = parseInt(countInput.value);

        if (inputValue <= 0) {
            e.preventDefault();
            showError('Количество должно быть больше 0');
            return;
        }
    });

    function showToast(element, message) {
        element.textContent = message;
        element.style.display = 'block';

        setTimeout(() => {
            element.style.display = 'none';
        }, 5000);
    }

    function showError(message) {
        showToast(errorToast, message);
    }

    function showSuccess(message) {
        showToast(successToast, message);
    }
});