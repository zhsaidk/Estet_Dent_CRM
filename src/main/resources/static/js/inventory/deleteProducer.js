// Получаем элементы
const form = document.getElementById('deleteForm');
const messageElement = document.getElementById('message');
const errorMessageElement = document.getElementById('errorMessage');
const producerSelect = document.getElementById('producerId');

// Функция отображения сообщений
function showMessage(message, isError = false) {
    const targetElement = isError ? errorMessageElement : messageElement;
    const otherElement = isError ? messageElement : errorMessageElement;

    targetElement.textContent = message;
    targetElement.style.display = 'block';
    otherElement.style.display = 'none';

    // Скрываем сообщение через 5 секунд
    setTimeout(() => {
        targetElement.style.display = 'none';
    }, 5000);
}

// Обработчик отправки формы
function handleDelete(event) {
    event.preventDefault();
    console.log('Кнопка "Удалить" нажата');

    const producerId = producerSelect.value;

    if (!producerId) {
        showMessage('Пожалуйста, выберите производителя', true);
        return;
    }

    // Собираем данные формы
    const formData = new FormData();
    formData.append('producerId', producerId);

    // Отправляем POST-запрос
    fetch('/producer/delete', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            console.log('Статус ответа:', response.status, 'Редирект:', response.redirected);

            if (response.redirected) {
                // После редиректа Thymeleaf отобразит flash attributes
                console.log('Перенаправление на:', response.url);
                window.location.href = response.url;

                // Проверяем, что удаление прошло успешно (по тексту сообщения)
                const url = new URL(response.url);
                if (url.pathname === '/materials/producer/delete') {
                    // Если это страница удаления, обновляем список только при успехе
                    setTimeout(() => {
                        const messageText = messageElement.textContent;
                        if (messageText === 'Успешно удалился') {
                            producerSelect.querySelector(`option[value="${producerId}"]`).remove();
                            if (producerSelect.options.length === 1) {
                                setTimeout(() => window.location.href = '/producer', 2000);
                            }
                        }
                    }, 100); // Небольшая задержка для загрузки страницы
                }
                return;
            }

            // Если сервер вернул ошибку без редиректа (маловероятно)
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Ошибка при удалении производителя');
                });
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            showMessage(error.message, true);
        });
}

// Инициализация страницы
document.addEventListener('DOMContentLoaded', () => {
    if (!form) {
        console.error('Форма с ID "deleteForm" не найдена');
        return;
    }
    if (!producerSelect) {
        console.error('Элемент select с ID "producerId" не найден');
        return;
    }

    console.log('Страница загружена, привязываем обработчик');
    form.addEventListener('submit', handleDelete);

    // Показываем flash attributes при загрузке страницы
    if (messageElement.textContent) {
        messageElement.style.display = 'block';
        setTimeout(() => messageElement.style.display = 'none', 5000);
    }
    if (errorMessageElement.textContent) {
        errorMessageElement.style.display = 'block';
        setTimeout(() => errorMessageElement.style.display = 'none', 5000);
    }
});