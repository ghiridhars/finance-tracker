import React, { useState } from 'react';
import { statementService } from '../api/services/statementService';

const StatementUpload: React.FC = () => {
    const [file, setFile] = useState<File | null>(null);
    const [bank, setBank] = useState('HDFC_SAVINGS');
    const [status, setStatus] = useState<'idle' | 'uploading' | 'success' | 'error'>('idle');
    const [message, setMessage] = useState('');

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            setFile(e.target.files[0]);
        }
    };

    const handleUpload = async () => {
        if (!file) {
            alert('Please select a file');
            return;
        }

        setStatus('uploading');
        setMessage('');

        try {
            await statementService.uploadStatement(file, bank);
            setStatus('success');
            setMessage('Statement uploaded successfully!');
        } catch (error: any) {
            setStatus('error');
            setMessage('Upload failed: ' + (error.response?.data || error.message));
        }
    };

    return (
        <div style={{ padding: '20px', border: '1px solid #ccc', borderRadius: '8px', maxWidth: '500px', margin: '20px auto' }}>
            <h2>Upload Statement</h2>
            <div style={{ marginBottom: '15px' }}>
                <label style={{ display: 'block', marginBottom: '5px' }}>Accout Type:</label>
                <select
                    value={bank}
                    onChange={(e) => setBank(e.target.value)}
                    style={{ width: '100%', padding: '8px' }}
                >
                    <option value="HDFC_SAVINGS">HDFC Savings Account</option>
                    {/* Future: Add more banks here */}
                </select>
            </div>
            <div style={{ marginBottom: '15px' }}>
                <label style={{ display: 'block', marginBottom: '5px' }}>Select PDF:</label>
                <input
                    type="file"
                    accept=".pdf"
                    onChange={handleFileChange}
                    style={{ width: '100%' }}
                />
            </div>

            <button
                onClick={handleUpload}
                disabled={status === 'uploading' || !file}
                style={{
                    padding: '10px 20px',
                    backgroundColor: '#007bff',
                    color: 'white',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: status === 'uploading' ? 'not-allowed' : 'pointer'
                }}
            >
                {status === 'uploading' ? 'Uploading...' : 'Upload'}
            </button>

            {message && (
                <div style={{
                    marginTop: '15px',
                    padding: '10px',
                    backgroundColor: status === 'success' ? '#d4edda' : '#f8d7da',
                    color: status === 'success' ? '#155724' : '#721c24',
                    borderRadius: '4px'
                }}>
                    {message}
                </div>
            )}
        </div>
    );
};

export default StatementUpload;
