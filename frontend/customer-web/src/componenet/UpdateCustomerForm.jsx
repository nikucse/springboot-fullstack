import {
  Alert,
  AlertIcon,
  Box,
  Button,
  FormLabel,
  Input,
  Stack,
} from '@chakra-ui/react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { updateCustomer } from '../services/client';
import {
  errorNotification,
  succcessNotification,
} from '../services/notification';

const MyTextInput = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  return (
    <>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Input className='text-input' {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className='error' status='error'>
          <AlertIcon />
          {meta.error}
        </Alert>
      ) : null}
    </>
  );
};

const MyCheckbox = ({ children, ...props }) => {
  const [field, meta] = useField({ ...props, type: 'checkbox' });
  return (
    <Box>
      <FormLabel className='checkbox-input'>
        <Input type='checkbox' {...field} {...props} />
        {children}
      </FormLabel>
      {meta.touched && meta.error ? (
        <Alert className='error' status='error'>
          <AlertIcon />
          {meta.error}
        </Alert>
      ) : null}
    </Box>
  );
};

// And now we can use these
const UpdateCustomerForm = ({ fetchCustomers, initialValues, customerId }) => {
  console.log(initialValues);

  return (
    <>
      <Formik
        initialValues={initialValues}
        validationSchema={Yup.object({
          name: Yup.string()
            .max(15, 'Must be 15 characters or less')
            .required('Name is Required'),

          email: Yup.string()
            .email('Invalid email address')
            .required(' Email is Required'),
          age: Yup.number()
            .min(16, 'Must be at least 16 years of age')
            .max(100, 'Must be less than 100 years of age')
            .required('Age is Required'),
        })}
        onSubmit={(customer, { setSubmitting }) => {
          updateCustomer(customerId, customer)
            .then((res) => {
              console.log(res);
              succcessNotification(
                'Customer updated',
                `${customer.name} was successfully updated`
              );
              fetchCustomers();
            })
            .catch((err) => {
              console.log(err);
              errorNotification(err.code, err.response.data.message);
            })
            .finally(() => setSubmitting(false));
        }}>
        {({ isValid, isSubmitting, dirty }) => {
          return (
            <Form>
              <Stack spacing={'24px'}>
                <MyTextInput
                  label='Name'
                  name='name'
                  type='text'
                  placeholder='Jane'
                />

                <MyTextInput
                  label='Email'
                  name='email'
                  type='email'
                  placeholder='doe@gmail.com'
                />

                <MyTextInput
                  label='Age'
                  name='age'
                  type='number'
                  placeholder='24'
                />

                <Button
                  disabled={!(!isValid && dirty) || isSubmitting}
                  type='submit'>
                  Submit
                </Button>
              </Stack>
            </Form>
          );
        }}
      </Formik>
    </>
  );
};

export default UpdateCustomerForm;
