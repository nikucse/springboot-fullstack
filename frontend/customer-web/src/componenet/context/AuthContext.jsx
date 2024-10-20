import { createContext, useContext, useEffect, useState } from 'react';
import { login as performLogin } from '../../services/client';
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext({});

const AuthProvider = ({ children }) => {
  const [customer, setCustomer] = useState(null);

  useEffect(() => {
    let token = localStorage.getItem('JWT_TOKEN');
    if (token) {
      token = jwtDecode(token);
      setCustomer({
        username: token.sub,
        roles: token.scopes,
      });
    }
  }, []);

  const login = async (request) => {
    return new Promise((resolve, reject) => {
      performLogin(request)
        .then((res) => {
          const jwtToken = res.headers['authorization'];
          localStorage.setItem('JWT_TOKEN', jwtToken);
          const decodedToken = jwtDecode(jwtToken);
          setCustomer({
            username: decodedToken.sub,
            roles: decodedToken.scopes,
          });
          resolve(res);
        })
        .catch((err) => reject(err));
    });
  };

  const logout = () => {
    localStorage.removeItem('JWT_TOKEN');
    setCustomer(null);
  };

  const isCustomerAuthenticated = () => {
    const token = localStorage.getItem('JWT_TOKEN');
    if (!token) {
      return false;
    }
    const { exp } = jwtDecode(token);
    if (Date.now() > exp * 1000) {
      logout();
      return false;
    }

    return true;
  };

  return (
    <AuthContext.Provider
      value={{ customer, login, logout, isCustomerAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;
