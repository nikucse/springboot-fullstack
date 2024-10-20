import {
  Alert,
  AlertIcon,
  Box,
  Button,
  FormLabel,
  Input,
  Select,
  Stack,
} from '@chakra-ui/react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { saveCustomer } from '../services/client';
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

const MySelect = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Select {...field} {...props} />
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
const CreateCustomerForm = ({ fetchCustomers }) => {
  return (
    <>
      <Formik
        initialValues={{
          name: '',
          email: '',
          age: 0,
          gender: '',
          password: '',
        }}
        validationSchema={Yup.object({
          name: Yup.string()
            .max(15, 'Must be 15 characters or less')
            .required('Name is Required'),
          email: Yup.string()
            .email('Invalid email address')
            .required(' Email is Required'),
          password: Yup.string()
            .min(4, 'Must be 4 characters or less')
            .max(15, 'Must be 15 characters or less')
            .required('Password is Required'),
          age: Yup.number()
            .min(16, 'Must be at least 16 years of age')
            .max(100, 'Must be less than 100 years of age')
            .required('Age is Required'),
          gender: Yup.string()
            .oneOf(['MALE', 'FEMALE'], 'Invalid Gender')
            .required('Gender is Required'),
        })}
        onSubmit={(customer, { setSubmitting }) => {
          saveCustomer(customer)
            .then((res) => {
              console.log(res);
              succcessNotification(
                'Customer saved',
                `${customer.name} was successfully saved`
              );
              fetchCustomers();
            })
            .catch((err) => {
              console.log(err);
              errorNotification(err.code, err.response.data.message);
            })
            .finally(() => setSubmitting(false));
        }}>
        {({ isValid, isSubmitting }) => {
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
                  label='Password'
                  name='password'
                  type='password'
                  placeholder={'Enter your password'}
                />

                <MyTextInput
                  label='Age'
                  name='age'
                  type='number'
                  placeholder='24'
                />

                <MySelect label='Gender' name='gender'>
                  <option value=''>Select gender</option>
                  <option value='MALE'>Male</option>
                  <option value='FEMALE'>Female</option>
                </MySelect>

                <Button disabled={!isValid || isSubmitting} type='submit'>
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

export default CreateCustomerForm;
