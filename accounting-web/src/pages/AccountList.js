import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Modal from '../components/Modal';
import useAxios from '../api/axiosInstance';

const currencySymbols = {
    '840': '$', // USD
    '978': '€', // EUR
    '826': '£', // GBP
    '392': '¥'  // JPY
};

const codeToSymbol = (code) => currencySymbols[code] || code;
const symbolToCode = (symbol) => {
    return Object.keys(currencySymbols).find(key => currencySymbols[key] === symbol) || symbol;
};

const AccountList = () => {
    const [accounts, setAccounts] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newAccount, setNewAccount] = useState({
        name: '',
        description: '',
        balance: 0,
        currencyCode: '840'
    });
    const [error, setError] = useState('');
    const axios = useAxios();

    // Fetch accounts
    const fetchAccounts = async () => {
        try {
            const response = await axios.get('/accounts');
            setAccounts(response.data);
        } catch (error) {
            console.error('Error fetching accounts', error);
        }
    };

    // Create new account
    const createAccount = async (e) => {
        e.preventDefault();
        try {
            const requestData = {
                ...newAccount,
                currencyCode: parseInt(newAccount.currencyCode) // Ensure numeric code
            };
            const response = await axios.post('/accounts', requestData);
            setAccounts([...accounts, response.data]);
            setNewAccount({ name: '', description: '', balance: 0, currencyCode: '840' });
            setIsModalOpen(false);
        } catch (error) {
            setError('Failed to create account. Please try again.');
            console.error('Error creating account', error);
        }
    };

    useEffect(() => {
        fetchAccounts();
    }, []);

    return (
        <div>
            <h2>Accounts</h2>
            <ul>
                {accounts.map((account) => (
                    <li key={account.id}>
                        <Link to={`/accounts/${account.id}/transactions`}>
                            <strong>{account.name}</strong> - {account.description}
                        </Link>
                        <div>
                            <p>Balance: {account.balance} {codeToSymbol(account.currencyCode)}</p>
                            <p>User: {account.user}</p>
                        </div>
                    </li>
                ))}
            </ul>

            <button onClick={() => setIsModalOpen(true)}>Create New Account</button>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <h3>Create New Account</h3>
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <form onSubmit={createAccount}>
                    <div>
                        <label>Name:</label>
                        <input
                            type="text"
                            value={newAccount.name}
                            onChange={(e) => setNewAccount({ ...newAccount, name: e.target.value })}
                            required
                        />
                    </div>
                    <div>
                        <label>Description:</label>
                        <input
                            type="text"
                            value={newAccount.description}
                            onChange={(e) => setNewAccount({ ...newAccount, description: e.target.value })}
                            required
                        />
                    </div>
                    <div>
                        <label>Balance:</label>
                        <input
                            type="number"
                            value={newAccount.balance}
                            onChange={(e) => setNewAccount({ ...newAccount, balance: parseFloat(e.target.value) })}
                            required
                        />
                    </div>
                    <div>
                        <label>Currency:</label>
                        <select
                            value={newAccount.currencyCode}
                            onChange={(e) => setNewAccount({ ...newAccount, currencyCode: e.target.value })}
                            required
                        >
                            {Object.entries(currencySymbols).map(([code, symbol]) => (
                                <option key={code} value={code}>{symbol}</option>
                            ))}
                        </select>
                    </div>
                    <button type="submit">Create Account</button>
                </form>
            </Modal>
        </div>
    );
};

export default AccountList;
