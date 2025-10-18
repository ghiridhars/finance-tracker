import React from 'react';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';

function useHealth() {
  return useQuery({
    queryKey: ['health'],
    queryFn: async () => {
      const res = await axios.get('/health');
      return res.data;
    },
  });
}

export default function App() {
  const { data, isLoading, error } = useHealth();
  return (
    <div style={{ padding: 32 }}>
      <h1>Finance Tracker Frontend</h1>
      <p>
        Backend health: {isLoading ? 'Loading...' : error ? 'Error' : String(data)}
      </p>
    </div>
  );
}
