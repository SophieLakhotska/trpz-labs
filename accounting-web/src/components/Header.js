import React from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, useLocation } from 'react-router-dom';

const Header = () => {
    const { logout } = useAuth();
    const navigate = useNavigate(); // Додаємо useNavigate
    const location = useLocation(); // Додаємо useLocation для перевірки шляху

    return (
        <header>
            <h1>Accounting App</h1>
            {(location.pathname !== '/login' && location.pathname !== '/register') && (
                <button onClick={() => logout(navigate)}>Logout</button> // Передаємо navigate
            )}
        </header>
    );
};

export default Header;
