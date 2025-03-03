import React, { useEffect } from 'react';
import { Navigate, useLocation } from 'react-router-dom';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { login } from 'app/shared/reducers/authentication';
import LoginPage from './login-page';

export const Login = () => {
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const loginError = useAppSelector(state => state.authentication.loginError);
  const pageLocation = useLocation();

  const handleLogin = (username, password, rememberMe = false) => dispatch(login(username, password, rememberMe));

  const { from } = pageLocation.state || { from: { pathname: '/', search: pageLocation.search } };
  if (isAuthenticated) {
    return <Navigate to={from} replace />;
  }

  return <LoginPage handleLogin={handleLogin} loginError={loginError} />;
};

export default Login;
