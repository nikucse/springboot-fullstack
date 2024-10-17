import axios from 'axios';

export const getCustomers = async () => {
  return axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`);
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
    customer
  );
};

export const deleteCustomer = async (id) => {
  return await axios.delete(
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`
  );
};
