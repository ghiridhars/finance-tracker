import React from 'react';
import StatementUpload from './components/StatementUpload';
import TransactionList from './components/TransactionList';

function App() {
  return (
    <div style={{ fontFamily: 'Arial, sans-serif', maxWidth: '1200px', margin: '0 auto', padding: '20px' }}>
      <header style={{ marginBottom: '30px', borderBottom: '1px solid #eee', paddingBottom: '20px' }}>
        <h1 style={{ color: '#333' }}>Personal Finance Tracker</h1>
      </header>

      <main>
        <section style={{ marginBottom: '40px' }}>
          <StatementUpload />
        </section>

        <section>
          <TransactionList />
        </section>
      </main>
    </div>
  );
}

export default App;
