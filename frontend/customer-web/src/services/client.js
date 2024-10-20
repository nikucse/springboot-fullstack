import axios from 'axios';

const getAuthConfig = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem('JWT_TOKEN')}`,
  },
});

export const getCustomers = async () => {
  return axios.get(
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
    getAuthConfig()
  );
};

export const saveCustomer = async (customer) => {
  return await axios.post(
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
    customer
  );
};

export const updateCustomer = async (id, customer) => {
  return await axios.put(
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
    customer,
    getAuthConfig()
  );
};

export const deleteCustomer = async (id) => {
  return await axios.delete(
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
    getAuthConfig()
  );
};

export const login = async (request) => {
  return await axios.post(
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`,
    request
  );
};
