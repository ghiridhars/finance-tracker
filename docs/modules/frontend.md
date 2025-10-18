# Frontend Module

*Related Documentation:*
- [🌐 API Documentation](../technical/api-documentation.md) - Complete API reference
- [📝 Web Module](finance-web.md) - Backend API implementation
- [📐 Architecture Overview](../ARCHITECTURE.md) - System design and flow

## Overview

The Frontend module provides a modern single-page application (SPA) interface for the Finance Tracker, built with React and TypeScript. It connects to the REST APIs provided by the Web module for file uploads, parsing requests, and data retrieval.

## Technology Stack

- **Framework**: React + TypeScript
- **Build Tool**: Vite
- **State Management**: React Query (server state), Zustand (planned for client state)
- **API Integration**: Axios for HTTP requests
- **Development Tools**: ESLint, TypeScript
- **Testing**: Jest + React Testing Library (planned)

## Project Structure

```
frontend/
├── src/
│   ├── api/           # API clients and types
│   ├── components/    # Reusable UI components
│   ├── features/      # Feature-specific code
│   ├── hooks/         # Custom React hooks
│   ├── App.tsx        # Root component
│   └── main.tsx      # Application entry point
├── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts    # Vite and dev server configuration
```

## Development Setup

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Start development server:
```bash
npm run dev
```

The development server runs on http://localhost:5173 and proxies API requests to the Spring Boot backend (http://localhost:8080).

## API Integration

The frontend connects to the following REST endpoints:

- POST `/api/credit-card/statements/upload` - Upload and parse statements
- GET `/api/credit-card/statements/{cardNumber}` - Get statements by card
- GET `/api/credit-card/statements?startDate&endDate` - Get statements by date range

API types are generated from the OpenAPI specification exposed by the backend at `/v3/api-docs`.

## Planned Features

1. Statement Management
   - Upload interface with drag-and-drop
   - Statement list and details view
   - Filter and search capabilities

2. User Interface
   - Responsive dashboard
   - Interactive data visualizations
   - Statement analysis tools

3. Future Enhancements
   - Authentication integration
   - Offline capabilities
   - Advanced analytics views