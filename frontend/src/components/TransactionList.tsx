import React, { useEffect, useState } from 'react';
import { statementService } from '../api/services/statementService';

interface Transaction {
    id: number;
    date: string;
    description: string;
    withdrawalAmount?: number;
    depositAmount?: number;
    closingBalance: number;
    type: string;
    referenceNumber: string;
}

const TransactionList: React.FC = () => {
    const [transactions, setTransactions] = useState<Transaction[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const fetchTransactions = async () => {
        setLoading(true);
        try {
            const data = await statementService.getTransactions();
            setTransactions(data);
            setError('');
        } catch (err: any) {
            setError('Failed to fetch transactions');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTransactions();
    }, []);

    if (loading) return <div>Loading transactions...</div>;
    if (error) return <div style={{ color: 'red' }}>{error}</div>;

    return (
        <div style={{ marginTop: '30px' }}>
            <h2>Recent Transactions</h2>
            <button onClick={fetchTransactions} style={{ marginBottom: '10px' }}>Refresh</button>
            <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
                <thead>
                    <tr style={{ backgroundColor: '#f2f2f2', textAlign: 'left' }}>
                        <th style={{ padding: '10px', borderBottom: '1px solid #ddd' }}>Date</th>
                        <th style={{ padding: '10px', borderBottom: '1px solid #ddd' }}>Description</th>
                        <th style={{ padding: '10px', borderBottom: '1px solid #ddd' }}>Reference</th>
                        <th style={{ padding: '10px', borderBottom: '1px solid #ddd' }}>Debit</th>
                        <th style={{ padding: '10px', borderBottom: '1px solid #ddd' }}>Credit</th>
                        <th style={{ padding: '10px', borderBottom: '1px solid #ddd' }}>Balance</th>
                    </tr>
                </thead>
                <tbody>
                    {transactions.length === 0 ? (
                        <tr>
                            <td colSpan={6} style={{ padding: '20px', textAlign: 'center' }}>No transactions found.</td>
                        </tr>
                    ) : (
                        transactions.map((t) => (
                            <tr key={t.id} style={{ borderBottom: '1px solid #eee' }}>
                                <td style={{ padding: '10px' }}>{t.date}</td>
                                <td style={{ padding: '10px' }}>{t.description}</td>
                                <td style={{ padding: '10px' }}>{t.referenceNumber}</td>
                                <td style={{ padding: '10px', color: '#d9534f' }}>
                                    {t.withdrawalAmount ? `₹${t.withdrawalAmount}` : '-'}
                                </td>
                                <td style={{ padding: '10px', color: '#28a745' }}>
                                    {t.depositAmount ? `₹${t.depositAmount}` : '-'}
                                </td>
                                <td style={{ padding: '10px', fontWeight: 'bold' }}>₹{t.closingBalance}</td>
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default TransactionList;
