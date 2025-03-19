// Получаем элементы
const form = document.getElementById('materialForm');
const successMessage = document.getElementById('successMessage');
const errorMessages = document.getElementById('errorMessages');
const validationErrors = document.getElementById('validationErrors');
const validationErrorsContainer = document.getElementById('validationErrorsContainer');
const addProducerBtn = document.getElementById('addProducer');
const backButton = document.getElementById('backButton');

// Проверяем наличие формы
if (!form) {
    console.error('Форма с ID "materialForm" не найдена');
} else {
    // Обработчик отправки формы (кнопка "Создать")
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        console.log('Кнопка "Создать" нажата');

        // Очищаем сообщения перед отправкой
        clearMessages();

        // Собираем данные формы
        const formData = new FormData(form);
        console.log('Данные формы:', Object.fromEntries(formData));

        try {
            const response = await fetch('/materials/create', {
                method: 'POST',
                body: formData
            });
            console.log('Ответ сервера:', response.status, 'Редирект:', response.redirected);

            if (response.redirected) {
                console.log('Перенаправление на:', response.url);
                window.location.href = response.url; // Следуем за редиректом
                return;
            }

            // Если сервер вернул JSON (например, ошибки или успех)
            const result = await response.json();
            console.log('Результат:', result);

            if (response.ok) {
                showSuccessMessage('Материал успешно создан');
                setTimeout(() => window.location.href = '/materials', 2000); // Редирект через 2 секунды
            } else {
                if (result.errors) {
                    showValidationErrors(result.errors); // Ошибки валидации
                } else if (result.error) {
                    showErrorMessage(result.error); // Пользовательская ошибка
                } else {
                    throw new Error('Неизвестная ошибка при создании материала');
                }
            }
        } catch (error) {
            console.error('Ошибка:', error);
            showErrorMessage(error.message || 'Произошла ошибка при создании материала');
        }
    });
}

// Обработчик кнопки "Добавить производителя"
if (addProducerBtn) {
    addProducerBtn.addEventListener('click', () => {
        console.log('Кнопка "Добавить производителя" нажата');
        window.location.href = '/producer';
    });
} else {
    console.error('Кнопка "addProducer" не найдена');
}

// Обработчик кнопки "Назад"
if (backButton) {
    backButton.addEventListener('click', () => {
        console.log('Кнопка "Назад" нажата');
        window.location.href = '/materials';
    });
} else {
    console.error('Кнопка "backButton" не найдена');
}

// Функции для отображения сообщений
function showSuccessMessage(message) {
    successMessage.textContent = message;
    successMessage.style.display = 'block';
    setTimeout(() => clearMessages(), 5000); // Скрыть через 5 секунд
}

function showErrorMessage(message) {
    errorMessages.textContent = message;
    errorMessages.style.display = 'block';
    setTimeout(() => clearMessages(), 5000); // Скрыть через 5 секунд
}

function showValidationErrors(errors) {
    validationErrors.innerHTML = ''; // Очищаем предыдущие ошибки
    errors.forEach(error => {
        const div = document.createElement('div');
        div.textContent = error.defaultMessage || error.message || error;
        validationErrors.appendChild(div);
    });
    validationErrorsContainer.style.display = 'block';
    setTimeout(() => clearMessages(), 5000); // Скрыть через 5 секунд
}

function clearMessages() {
    successMessage.textContent = '';
    successMessage.style.display = 'none';
    errorMessages.textContent = '';
    errorMessages.style.display = 'none';
    validationErrors.innerHTML = '';
    validationErrorsContainer.style.display = 'none';
}