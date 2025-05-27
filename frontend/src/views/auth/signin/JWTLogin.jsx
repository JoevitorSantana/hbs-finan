import React from 'react';
import { ToastContainer, toast } from 'react-toastify';
import { Row, Col, Alert, Button } from 'react-bootstrap';
import * as Yup from 'yup';
import { Formik } from 'formik';
import { useAuth } from 'contexts/AuthProvider';
import { useNavigate } from 'react-router-dom';

const JWTLogin = () => {
  const navigate = useNavigate();
  const { user, loginAction } = useAuth();

  if (user)
      navigate("/");

  return (
    <React.Fragment>
      <ToastContainer position="top-right" autoClose={3000} />
    <Formik
      initialValues={{
        email: '',
        senha: '',
        submit: null
      }}

      validationSchema={Yup.object().shape({ //isso aqui só verifica formato, tamanho e se está vazio.
        email: Yup.string().email('Email inválido!').max(255).required('Email obrigatório'),
        senha: Yup.string().max(255).required('Senha obrigatória!')
      })}

      onSubmit={async (values, { setErrors, setStatus, setSubmitting }) => { //isso aqui que faz a validação no banco de dados
        try {
          const response = await loginAction(values.email, values.senha); //backend consulta o banco de dados para conferir as credenciais.
          if (response.token)
            toast.success('Login realizado com sucesso!');
          else
            toast.error(response.message || "Erro desconhecido!");
        } catch (error) {
          console.error(error);
          setStatus({ success: false });
          setErrors({ submit: error.response?.data?.message || 'Erro ao realizar login' });
          toast.error(errorMessage);
        } finally {
          setSubmitting(false);
        }
      }}
    >
      {({ errors, handleBlur, handleChange, handleSubmit, isSubmitting, touched, values }) => (
        <form noValidate onSubmit={handleSubmit}>
          
          <div className="form-group mb-3">
            <input
              className="form-control"
              label="Email"
              name="email"
              placeholder="Email"
              onBlur={handleBlur}
              onChange={handleChange}
              type="email"
              value={values.email}
            />
            {touched.email && errors.email && <small className="text-danger form-text">{errors.email}</small>}
          </div>

          
          <div className="form-group mb-4">
            <input
              className="form-control"
              placeholder="Senha"
              label="Senha"
              name="senha"
              onBlur={handleBlur}
              onChange={handleChange}
              type="password"
              value={values.senha}
            />
            {touched.senha && errors.senha && <small className="text-danger form-text">{errors.senha}</small>}
          </div>

          <div className="custom-control custom-checkbox  text-start mb-4 mt-2">
            <input type="checkbox" className="custom-control-input mx-2" id="customCheck1" />
            <label className="custom-control-label" htmlFor="customCheck1">
              Lembrar
            </label>
          </div>

          {errors.submit && (
            <Col sm={12}>
              <Alert>{errors.submit}</Alert>
            </Col>
          )}

          <Row>
            <Col mt={2}>
              <Button
                className="btn-block mb-4"
                color="primary"
                disabled={isSubmitting}
                size="large"
                 type="submit"
                 variant="primary"
              >
                Login
              </Button>
            </Col>
          </Row>

        </form>
      )}
    </Formik>
    </React.Fragment>
  );
};

export default JWTLogin;