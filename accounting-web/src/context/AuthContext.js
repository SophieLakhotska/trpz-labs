import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(localStorage.getItem('jwtToken') || '');

    const login = (newToken, navigate) => {
        setToken(newToken);
        localStorage.setItem('jwtToken', newToken);
        navigate('/'); // Викликаємо передану функцію для редіректу
    };

    const logout = (navigate) => {
        setToken('');
        localStorage.removeItem('jwtToken');
        navigate('/login'); // Викликаємо передану функцію для редіректу
    };

    return (
        <AuthContext.Provider value={{ token, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
