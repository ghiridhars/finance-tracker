import axios from 'axios';

const API_BASE_URL = '/api'; // Vite proxy should handle this, or set absolute URL

export const statementService = {
    uploadStatement: async (file: File, bank: string) => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('bank', bank);
        formData.append('save', 'true');

        const response = await axios.post(`${API_BASE_URL}/statements/upload`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return response.data;
    },

    getTransactions: async (from?: string, to?: string) => {
        const params: any = {};
        if (from) params.from = from;
        if (to) params.to = to;

        const response = await axios.get(`${API_BASE_URL}/transactions`, { params });
        return response.data;
    }
};
