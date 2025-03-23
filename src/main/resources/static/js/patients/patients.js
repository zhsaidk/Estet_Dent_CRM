document.addEventListener('DOMContentLoaded', function() {
    // Add hover effect to nav items
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('mouseenter', function() {
            if (!this.classList.contains('active')) {
                this.style.backgroundColor = 'var(--secondary-color)';
            }
        });

        item.addEventListener('mouseleave', function() {
            if (!this.classList.contains('active')) {
                this.style.backgroundColor = '';
            }
        });
    });

    // Search functionality
    document.getElementById('searchInput').addEventListener('input', function(e) {
        const searchText = e.target.value.toLowerCase();
        const rows = document.querySelectorAll('.patients-table tbody tr');

        rows.forEach(row => {
            const text = row.textContent.toLowerCase();
            row.style.display = text.includes(searchText) ? '' : 'none';
        });
    });

    // Delete user function with confirmation
    window.deleteUser = function(id) {
        if (confirm('Вы уверены, что хотите удалить этого пациента?')) {
            fetch(`/patients/${id}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        document.getElementById('message').textContent = 'Ошибка при удалении пациента';
                    }
                })
                .catch(error => {
                    document.getElementById('message').textContent = 'Ошибка при удалении пациента';
                });
        }
    }

    // Add filter dropdown functionality
    const filterToggle = document.querySelector('.filter-toggle');
    const filterContent = document.querySelector('.filter-content');

    filterToggle.addEventListener('click', function() {
        const isVisible = filterContent.style.display === 'block';
        filterContent.style.display = isVisible ? 'none' : 'block';
        filterToggle.textContent = isVisible ? 'Фильтровать ▼' : 'Фильтровать ▲';
    });

    // Close filter dropdown when clicking outside
    document.addEventListener('click', function(event) {
        if (!event.target.closest('.filter-dropdown') && filterContent.style.display === 'block') {
            filterContent.style.display = 'none';
            filterToggle.textContent = 'Фильтровать ▼';
        }
    });

    // Add hover effect for create button
    document.querySelector('.create-btn').addEventListener('mouseenter', function() {
        this.style.transform = 'translateY(-1px)';
        this.style.boxShadow = '0 2px 4px rgba(0,0,0,0.1)';
    });

    document.querySelector('.create-btn').addEventListener('mouseleave', function() {
        this.style.transform = '';
        this.style.boxShadow = '';
    });
});