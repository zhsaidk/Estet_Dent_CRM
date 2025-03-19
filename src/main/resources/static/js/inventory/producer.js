// Form validation and enhancement
document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form');
    const nameInput = document.getElementById('name');

    form.addEventListener('submit', (e) => {
        // Basic client-side validation
        if (!nameInput.value.trim()) {
            e.preventDefault();
            alert('Пожалуйста, введите название производителя');
            return;
        }
    });

    // Clear form when success message is present
    const successMessage = document.querySelector('.success-message');
    if (successMessage) {
        nameInput.value = '';
    }
});

