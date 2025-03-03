import React, { useEffect, useState } from 'react';
import { Translate, ValidatedField, translate } from 'react-jhipster';
import { Alert, Button, Form } from 'reactstrap';
import { Link } from 'react-router-dom';
import { type FieldError, useForm } from 'react-hook-form';

export interface ILoginPageProps {
  loginError: boolean;
  handleLogin: (username: string, password: string, rememberMe: boolean) => void;
}

const LoginPage = (props: ILoginPageProps) => {
  const [logoPosition, setLogoPosition] = useState('-200px');

  useEffect(() => {
    const timer = setTimeout(() => {
      setLogoPosition('50%');
    }, 2000); // الشعار يتحرك بعد ثانيتين

    return () => clearTimeout(timer);
  }, []);

  const login = ({ username, password, rememberMe }) => {
    props.handleLogin(username, password, rememberMe);
  };

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const handleLoginSubmit = e => {
    handleSubmit(login)(e);
  };

  const { loginError } = props;

  return (
    <div className="container-fluid login-page d-flex p-0" style={{ height: '100vh' }}>
      <div
        className="login-image d-flex align-items-center justify-content-center"
        style={{ flex: 1, backgroundColor: '#004080', margin: 0, position: 'relative', overflow: 'hidden', borderRadius: '20px' }}
      >
        <div
          className="logo"
          style={{
            position: 'absolute',
            top: '50%',
            left: logoPosition,
            transform: 'translate(-50%, -50%)',
            transition: 'left 1s ease-in-out',
          }}
        >
          <img src="/content/images/Asset 3.png" alt="Logo" style={{ width: '400px', height: '300px' }} />
        </div>
      </div>
      <div className="login-form d-flex align-items-center justify-content-center" style={{ flex: 1, margin: 0 }}>
        <div style={{ width: '80%' }}>
          <h1 id="login-title" data-cy="loginTitle" className="text-center">
            <Translate contentKey="login.title">Login</Translate>
          </h1>
          <Form onSubmit={handleLoginSubmit}>
            {loginError && (
              <Alert color="danger" data-cy="loginError">
                <Translate contentKey="login.messages.error.authentication">
                  <strong>Failed to sign in!</strong> Please check your credentials and try again.
                </Translate>
              </Alert>
            )}
            <ValidatedField
              name="username"
              label={translate('global.form.username.label')}
              placeholder={translate('global.form.username.placeholder')}
              required
              autoFocus
              data-cy="username"
              validate={{ required: 'Username cannot be empty!' }}
              register={register}
              error={errors.username as FieldError}
              isTouched={touchedFields.username}
            />
            <ValidatedField
              name="password"
              type="password"
              label={translate('login.form.password')}
              placeholder={translate('login.form.password.placeholder')}
              required
              data-cy="password"
              validate={{ required: 'Password cannot be empty!' }}
              register={register}
              error={errors.password as FieldError}
              isTouched={touchedFields.password}
            />
            <ValidatedField
              name="rememberMe"
              type="checkbox"
              check
              label={translate('login.form.rememberme')}
              value={true}
              register={register}
            />
            <Button color="warning" type="submit" data-cy="submit" className="mt-3 w-100">
              <Translate contentKey="login.form.button">Sign in</Translate>
            </Button>
          </Form>
          <div className="additional-links mt-3 text-center">
            <Link to="/account/reset/request" data-cy="forgetYourPasswordSelector" className="reset-link">
              <Translate contentKey="login.password.reset">Reset password / Get username</Translate>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
