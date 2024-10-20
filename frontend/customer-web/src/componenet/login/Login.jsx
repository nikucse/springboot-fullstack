import {
  Button,
  Flex,
  FormLabel,
  Heading,
  Input,
  Stack,
  Image,
  Alert,
  AlertIcon,
} from '@chakra-ui/react';
import { Form, Formik, useField } from 'formik';

import * as Yup from 'yup';
import { useAuth } from '../context/AuthContext';
import { errorNotification } from '../../services/notification';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

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

const LoginForm = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  return (
    <Formik
      validateOnMount={true}
      validationSchema={Yup.object({
        username: Yup.string()
          .email('Must be valid email')
          .required('Email is required'),
        password: Yup.string()
          .max(20, "Password can't be more than 20 char")
          .required('Password is required'),
      })}
      initialValues={{ username: '', password: '' }}
      onSubmit={(values, { setSubmitting }) => {
        login(values)
          .then((res) => {
            navigate('/dashboard');
          })
          .catch((err) => {
            errorNotification(err.code, err.response.data.message);
          })
          .finally(() => setSubmitting(false));
      }}>
      {({ isValid, isSubmitting }) => (
        <Form>
          <Stack spacing={15}>
            <MyTextInput
              label={'Email'}
              name={'username'}
              type={'email'}
              placeholder={'nikul@gmail.com'}
            />
            <MyTextInput
              label={'Password'}
              name={'password'}
              type={'password'}
              placeholder={'Type your password'}
            />
            <Button type='submit' disabled={!isValid || isSubmitting}>
              Login
            </Button>
          </Stack>
        </Form>
      )}
    </Formik>
  );
};

const Login = () => {
  const { customer } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (customer) {
      navigate('/dashboard');
    }
  });

  return (
    <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
      <Flex p={8} flex={1} align={'center'} justify={'center'}>
        <Stack spacing={4} w={'full'} maxW={'md'}>
          <Heading fontSize={'2xl'}>Sign in to your account</Heading>
          <LoginForm />
        </Stack>
      </Flex>
      <Flex flex={1}>
        <Image
          alt={'Login Image'}
          objectFit={'cover'}
          src={
            'https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1352&q=80'
          }
        />
      </Flex>
    </Stack>
  );
};

export default Login;
