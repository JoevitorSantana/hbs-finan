import React, { useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

const NovoUsuario = () => {

    const navigate = useNavigate();

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

        try {
            const response = await fetch('http://localhost:8080/usuarios/novo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                alert('Usuário cadastrado com sucesso!');
                navigate('/usuarios'); // redireciona para lista de usuários
            } else {
                const errorData = await response.json();
                alert('Erro ao cadastrar usuário: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao cadastrar usuário:', error);
            alert('Erro na comunicação com o servidor.');
        }
    }

    return (
        <React.Fragment>
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
                                            />
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
                                            />
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
                                            />
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
                                            />
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