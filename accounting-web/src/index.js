import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { AuthProvider } from './context/AuthContext';

const rootElement = document.getElementById('root'); // Знайти контейнерний елемент

if (rootElement) {
    const root = ReactDOM.createRoot(rootElement); // Створити корінь React
    root.render(
        <React.StrictMode>
            <AuthProvider>
                <App />
            </AuthProvider>
        </React.StrictMode>
    );
} else {
    console.error("Root element not found. Please check your index.html.");
}
