// Конфигурация
const config = {
    apiBaseUrl: '/api'
};

// Элементы DOM
const historyForm = document.getElementById('historyForm');
const errorsContainer = document.getElementById('errors');
const cancelButton = document.getElementById('cancelButton');
const patientInput = document.getElementById('patient'); // Добавляем скрытое поле

// Инициализация формы
function initForm() {
    checkForErrors();
}

// Проверка ошибок в URL
function checkForErrors() {
    const urlParams = new URLSearchParams(window.location.search);
    const errors = urlParams.getAll('error');

    if (errors.length > 0) {
        errors.forEach(error => {
            const errorElement = document.createElement('div');
            errorElement.classList.add('error-message');
            errorElement.textContent = error;
            errorsContainer.appendChild(errorElement);
        });
    }
}

// Обработка отправки формы
historyForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const patientId = patientInput.value; // Берем ID из скрытого поля
    const formData = new FormData(historyForm);

    try {
        const response = await fetch(`/history/create/${patientId}`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            alert('Запись успешно создана!');
            window.location.href = `/patients/${patientId}`;
        } else {
            const errorData = await response.json();
            if (errorData.errors && errorData.errors.length > 0) {
                errorData.errors.forEach(error => {
                    const errorElement = document.createElement('div');
                    errorElement.classList.add('error-message');
                    errorElement.textContent = error.defaultMessage || 'Ошибка валидации';
                    errorsContainer.appendChild(errorElement);
                });
            } else {
                throw new Error('Ошибка при создании записи');
            }
        }
    } catch (error) {
        console.error('Ошибка при отправке формы:', error);
        const errorElement = document.createElement('div');
        errorElement.classList.add('error-message');
        errorElement.textContent = 'Произошла ошибка при создании записи. Пожалуйста, попробуйте снова.';
        errorsContainer.appendChild(errorElement);
    }
});

// Обработчик кнопки "Отмена"
cancelButton.addEventListener('click', () => {
    const patientId = patientInput.value; // Используем значение из скрытого поля
    window.location.href = `/patients/${patientId}`;
});

// Инициализация формы при загрузке страницы
document.addEventListener('DOMContentLoaded', initForm);