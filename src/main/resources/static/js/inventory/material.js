document.addEventListener('DOMContentLoaded', function() {
    const filterToggle = document.querySelector('.filter-toggle');
    const filterContent = document.querySelector('.filter-content');

    filterToggle.addEventListener('click', function() {
        const isVisible = filterContent.style.display === 'block';
        filterContent.style.display = isVisible ? 'none' : 'block';
        filterToggle.textContent = isVisible ? 'Фильтровать ▼' : 'Фильтровать ▲';
    });
});

