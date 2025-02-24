function deleteUser(patientId) {
    if (confirm('Удалить пользователя?')) {
        fetch('/patients/' + patientId, {
            method: 'DELETE'
        }).then(() => {
            window.location.reload(); // Обновляем страницу после удаления
        });
    }
}

