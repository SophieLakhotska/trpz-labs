import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AccountList from './pages/AccountList';
import Transactions from './pages/Transactions';
import Header from "./components/Header";
import Login from "./pages/Login";
import Register from './pages/Register'; // Імпортуємо компонент реєстрації

function App() {
    return (
        <Router>
            <div className="App">
                <Header />
                <Routes>
                    <Route path="/" element={<AccountList />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/accounts/:accountId/transactions" element={<Transactions />} />
                    <Route path="/register" element={<Register />} /> {/* Новий маршрут */}
                </Routes>
            </div>
        </Router>
    );
}

export default App;
