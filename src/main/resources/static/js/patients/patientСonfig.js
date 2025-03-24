// Configuration file for patient management system
export default {
    // API settings
    api: {
        baseUrl: '/api',
        timeout: 10000
    },

    // UI settings
    ui: {
        defaultTab: 'history',
        historyItemsPerPage: 5,
        dateFormat: 'DD.MM.YYYY',
        primaryColor: '#4285f4',
        secondaryColor: '#f8f9fa'
    },

    // Feature toggles
    features: {
        enableNotifications: true,
        enablePdfExport: true,
        enableDirectMessaging: false
    }
};