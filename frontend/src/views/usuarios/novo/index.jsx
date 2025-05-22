import React, { useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';

const NovoUsuario = () => {

    const navigate = useNavigate();

    const token = localStorage.getItem("site");

    const [errors, setErrors] = useState({});

    const validateFields = () => {
        const newErrors = {};
        if (!formData.nome) newErrors.nome = 'Nome é obrigatório.';
        if (!formData.ultimoNome) newErrors.ultimoNome = 'Último nome é obrigatório.';
        if (!formData.email) newErrors.email = 'Email é obrigatório.';
        if (!formData.senha) newErrors.senha = 'Senha é obrigatória.';
        return newErrors;
    };

    const [formData, setFormData] = useState({
        nome: '',
        ultimoNome: '',
        email: '',
        senha: '',
        role: 'USER'
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validação dos campos
        const validationErrors = validateFields();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            toast.error('Por favor, corrija os erros no formulário.');
            return;
        }
        try {
            const response = await fetch('http://localhost:8080/usuarios/novo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization' : 'Bearer ' + token
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                toast.success('Usuário cadastrado com sucesso!');
                setTimeout(() => {
                    navigate('/usuarios');
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error('Erro ao cadastrar usuário: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao cadastrar usuário:', error);
            toast.error('Erro na comunicação com o servidor.');
        }
    }

    return (
        <React.Fragment>
            <ToastContainer />
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <Card.Title as="h5">Novo Usuário</Card.Title>
                        </Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="nome">
                                            <Form.Label>Nome</Form.Label>
                                            <Form.Control
                                                type="nome"
                                                name="nome"
                                                value={formData.nome}
                                                placeholder="Nome"
                                                onChange={handleChange}
                                                isInvalid={!!errors.nome}
                                                isValid={formData.nome && !errors.nome}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.nome}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="ultimoNome">
                                            <Form.Label>Último Nome</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="ultimoNome"
                                                placeholder="Último Nome"
                                                value={formData.ultimoNome}
                                                onChange={handleChange}
                                                isInvalid={!!errors.ultimoNome}
                                                isValid={formData.ultimoNome && !errors.ultimoNome}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.ultimoNome}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="email">
                                            <Form.Label>Email</Form.Label>
                                            <Form.Control
                                                type="email"
                                                name="email"
                                                placeholder="Email"
                                                value={formData.email}
                                                onChange={handleChange}
                                                isInvalid={!!errors.email}
                                                isValid={formData.email && !errors.email}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.email}
                                            </Form.Control.Feedback>
                                        </Form.Group>

                                        <Form.Group className="mb-3" controlId="role">
                                            <Form.Label>Nível</Form.Label>
                                            <Form.Control
                                                as="select"
                                                name="role"
                                                value={formData.nivel}
                                                onChange={handleChange}
                                            >
                                                <option value="ADMIN">Administrador</option>
                                                <option value="USER">Usuário</option>
                                            </Form.Control>
                                        </Form.Group>

                                        <Link to="/usuarios">
                                            <Button variant="secondary">Cancelar</Button>
                                        </Link>
                                        <Button variant="primary" type="submit">Salvar</Button>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="senha">
                                            <Form.Label>Senha</Form.Label>
                                            <Form.Control
                                                type="password"
                                                name="senha"
                                                placeholder="Senha"
                                                value={formData.senha}
                                                onChange={handleChange}
                                                isInvalid={!!errors.senha}
                                                isValid={formData.senha && !errors.senha}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.senha}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                </Row>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </React.Fragment>
    );
}

export default NovoUsuario;