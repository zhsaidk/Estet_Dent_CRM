document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('materialForm');
    const deleteBtn = document.getElementById('deleteBtn');
    const deleteForm = document.getElementById('deleteForm');

    // Form validation
    form.addEventListener('submit', function(e) {
        const name = document.getElementById('name').value;
        const count = document.getElementById('count').value;
        const price = document.getElementById('price').value;

        let isValid = true;
        const errors = [];

        if (!name.trim()) {
            errors.push('Название материала обязательно');
            isValid = false;
        }

        if (!count || count < 0) {
            errors.push('Количество должно быть положительным числом');
            isValid = false;
        }

        if (!price || price < 0) {
            errors.push('Цена должна быть положительным числом');
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
            // Update error display
            const errorContainer = document.querySelector('.error-container');
            errorContainer.innerHTML = '';
            errors.forEach(error => {
                const errorDiv = document.createElement('div');
                errorDiv.className = 'error-message';
                errorDiv.innerHTML = `<span>${error}</span>`;
                errorContainer.appendChild(errorDiv);
            });
        }
    });

    // Handle delete button
    deleteBtn.addEventListener('click', function(e) {
        e.preventDefault();
        if (confirm('Вы уверены, что хотите удалить этот материал?')) {
            deleteForm.submit();
        }
    });

    // Add input validation styling
    const inputs = form.querySelectorAll('input[type="number"]');
    inputs.forEach(input => {
        input.addEventListener('input', function() {
            if (this.value < 0) {
                this.style.borderColor = '#dc3545';
            } else {
                this.style.borderColor = '#ddd';
            }
        });
    });
});