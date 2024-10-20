import { Spinner, Text, Wrap, WrapItem } from '@chakra-ui/react';
import SidebarWithHeader from './componenet/SidebarWithHeader';
import { useEffect, useState } from 'react';
import { getCustomers } from './services/client';
import Card from './componenet/Card';
import CreateCustomerDrawer from './componenet/CreateCustomerDrawer';
import { errorNotification } from './services/notification';

const App = () => {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchCustomers = () => {
    setLoading(true);

    getCustomers()
      .then((res) => {
        setCustomers(res.data);
      })
      .catch((err) => {
        const errorMessage =
          err.response?.data?.message || 'Something went wrong';
        errorNotification(err.code, errorMessage);
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchCustomers();
  }, []);

  if (loading) {
    return (
      <SidebarWithHeader>
        <Spinner
          thickness='4px'
          speed='0.65s'
          emptyColor='gray.200'
          color='blue.500'
          size='xl'
        />
      </SidebarWithHeader>
    );
  }

  if (customers.length <= 0) {
    return (
      <SidebarWithHeader>
        <CreateCustomerDrawer fetchCustomers={fetchCustomers} />
        <Text mt={5}>No Customer available</Text>
      </SidebarWithHeader>
    );
  }

  return (
    <SidebarWithHeader>
      <CreateCustomerDrawer fetchCustomers={fetchCustomers} />
      <Wrap justify='center' spacing={'30px'}>
        {customers.map((customer, index) => (
          <WrapItem key={customer.id}>
            <Card
              {...customer}
              fetchCustomers={fetchCustomers}
              imageNumber={index}
            />
          </WrapItem>
        ))}
      </Wrap>
    </SidebarWithHeader>
  );
};

export default App;
