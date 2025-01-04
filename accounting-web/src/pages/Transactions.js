import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import useAxios from '../api/axiosInstance';
import Modal from '../components/Modal';

const currencySymbols = {
    '840': '$', // USD
    '978': '€', // EUR
    '826': '£', // GBP
    '392': '¥'  // JPY
};

const codeToSymbol = (code) => currencySymbols[code] || code;

const Transactions = () => {
    const { accountId } = useParams();
    const [transactions, setTransactions] = useState([]);
    const [currencyCode, setCurrencyCode] = useState('840');
    const [newTransaction, setNewTransaction] = useState({
        name: '',
        description: '',
        amount: 0,
        transactionType: 'WITHDRAWAL'
    });
    const [newPeriodicTransaction, setNewPeriodicTransaction] = useState({
        name: '',
        description: '',
        amount: 0,
        period: 'DAILY',
        transactionType: 'WITHDRAWAL'
    });
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isPeriodicModalOpen, setIsPeriodicModalOpen] = useState(false);
    const axios = useAxios();

    const fetchAccountDetails = async () => {
        try {
            const response = await axios.get(`/accounts/${accountId}`);
            setCurrencyCode(response.data.currencyCode);
        } catch (error) {
            console.error('Error fetching account details', error);
        }
    };

    const fetchTransactions = async () => {
        try {
            const response = await axios.get(`/accounts/${accountId}/transactions`);
            setTransactions(response.data);
        } catch (error) {
            console.error('Error fetching transactions', error);
        }
    };

    const createTransaction = async () => {
        try {
            const response = await axios.post(`/accounts/${accountId}/transactions`, newTransaction);
            setTransactions([...transactions, response.data]);
            setIsModalOpen(false);
        } catch (error) {
            console.error('Error creating transaction', error);
        }
    };

    const createPeriodicTransaction = async () => {
        try {
            const response = await axios.post(`/accounts/${accountId}/transactions/period`, newPeriodicTransaction);
            setIsPeriodicModalOpen(false);
        } catch (error) {
            console.error('Error creating periodic transaction', error);
        }
    };

    const exportTransactions = async (format) => {
        try {
            const response = await axios.get(`/accounts/${accountId}/statistics`, {
                params: { exportType: format },
                responseType: 'blob'
            });
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `transactions.${format}`);
            document.body.appendChild(link);
            link.click();
        } catch (error) {
            console.error(`Error exporting transactions as ${format}`, error);
        }
    };

    useEffect(() => {
        fetchAccountDetails();
        fetchTransactions();
    }, [accountId]);

    return (
        <div>
            <h2>Transactions</h2>
            <ul>
                {transactions.map(transaction => (
                    <li key={transaction.id}>
                        {transaction.name} - {transaction.amount} {codeToSymbol(currencyCode)} - {transaction.transactionType}
                    </li>
                ))}
            </ul>
            <button onClick={() => setIsModalOpen(true)}>Create Transaction</button>
            <button onClick={() => setIsPeriodicModalOpen(true)}>Create Periodic Transaction</button>
            <button onClick={() => exportTransactions('XLS')}>Export to Excel</button>
            <button onClick={() => exportTransactions('CSV')}>Export to CSV</button>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <h3>Create Transaction</h3>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                    <label>
                        Transaction Name:
                        <input
                            type="text"
                            value={newTransaction.name}
                            onChange={e => setNewTransaction({ ...newTransaction, name: e.target.value })}
                        />
                    </label>
                    <label>
                        Amount:
                        <input
                            type="number"
                            value={newTransaction.amount}
                            onChange={e => setNewTransaction({ ...newTransaction, amount: parseFloat(e.target.value) })}
                        />
                    </label>
                    <label>
                        Transaction Type:
                        <select
                            value={newTransaction.transactionType}
                            onChange={e => setNewTransaction({ ...newTransaction, transactionType: e.target.value })}
                        >
                            <option value="WITHDRAWAL">Withdrawal</option>
                            <option value="DEPOSIT">Deposit</option>
                        </select>
                    </label>
                </div>
                <button onClick={createTransaction}>Add Transaction</button>
            </Modal>

            <Modal isOpen={isPeriodicModalOpen} onClose={() => setIsPeriodicModalOpen(false)}>
                <h3>Create Periodic Transaction</h3>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                    <label>
                        Name:
                        <input
                            type="text"
                            value={newPeriodicTransaction.name}
                            onChange={e => setNewPeriodicTransaction({ ...newPeriodicTransaction, name: e.target.value })}
                        />
                    </label>
                    <label>
                        Amount:
                        <input
                            type="number"
                            value={newPeriodicTransaction.amount}
                            onChange={e => setNewPeriodicTransaction({ ...newPeriodicTransaction, amount: parseFloat(e.target.value) })}
                        />
                    </label>
                    <label>
                        Period:
                        <select
                            value={newPeriodicTransaction.period}
                            onChange={e => setNewPeriodicTransaction({ ...newPeriodicTransaction, period: e.target.value })}
                        >
                            <option value="MINUTELY">Minutely</option>
                            <option value="HOURLY">Hourly</option>
                            <option value="DAILY">Daily</option>
                            <option value="WEEKLY">Weekly</option>
                            <option value="MONTHLY">Monthly</option>
                            <option value="YEARLY">Yearly</option>
                        </select>
                    </label>
                    <label>
                        Transaction Type:
                        <select
                            value={newPeriodicTransaction.transactionType}
                            onChange={e => setNewPeriodicTransaction({ ...newPeriodicTransaction, transactionType: e.target.value })}
                        >
                            <option value="WITHDRAWAL">Withdrawal</option>
                            <option value="DEPOSIT">Deposit</option>
                        </select>
                    </label>
                </div>
                <button onClick={createPeriodicTransaction}>Add Periodic Transaction</button>
            </Modal>

            <Link to="/">Back to Accounts</Link>
        </div>
    );
};

export default Transactions;
