// Patient management system JavaScript code

// Configuration
import config from './patientСonfig.js';

document.addEventListener('DOMContentLoaded', () => {
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

    // Tab switching functionality
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            // Remove active class from all tabs
            tabs.forEach(t => t.classList.remove('active'));

            // Add active class to clicked tab
            tab.classList.add('active');

            // Hide all tab content
            document.querySelectorAll('.tab-content-item').forEach(content => {
                content.classList.remove('active');
            });

            // Show content for selected tab
            const tabId = tab.getAttribute('data-tab');
            document.getElementById(tabId + '-content').classList.add('active');
        });
    });

    // History item expansion/collapse
    const historyItems = document.querySelectorAll('.history-item');
    historyItems.forEach(item => {
        const toggleBtn = item.querySelector('.toggle-btn');
        if (toggleBtn) {
            toggleBtn.addEventListener('click', () => {
                item.classList.toggle('expanded');
                toggleBtn.classList.toggle('up');
                toggleBtn.classList.toggle('down');
            });
        }
    });
});