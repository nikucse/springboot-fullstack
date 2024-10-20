import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App.jsx';
import { ChakraProvider } from '@chakra-ui/react';
import { createStandaloneToast } from '@chakra-ui/react';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Login from './componenet/login/Login.jsx';
import AuthProvider from './componenet/context/AuthContext.jsx';
import ProjectedRoute from './componenet/ProjectedRoute.jsx';

import './index.css';

const { ToastContainer } = createStandaloneToast();

const router = createBrowserRouter([
  {
    path: '/',
    element: <Login />,
  },
  {
    path: 'dashboard',
    element: (
      <ProjectedRoute>
        <App />{' '}
      </ProjectedRoute>
    ),
  },
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ChakraProvider>
      <AuthProvider>
        <RouterProvider router={router} />
      </AuthProvider>
      <ToastContainer />
    </ChakraProvider>
  </StrictMode>
);
