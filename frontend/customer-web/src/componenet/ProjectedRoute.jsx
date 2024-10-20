import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';

const ProjectedRoute = ({ children }) => {
  const { isCustomerAuthenticated } = useAuth();

  const navigate = useNavigate();

  useEffect(() => {
    if (!isCustomerAuthenticated()) {
      navigate('/');
    }
  });

  return isCustomerAuthenticated() ? children : '';
};

export default ProjectedRoute;
