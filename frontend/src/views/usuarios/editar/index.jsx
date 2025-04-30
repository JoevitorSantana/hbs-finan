import { useUsuario } from "hooks/useUsers";
import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

const EditarUsuario = () => {
    const idUsuario = window.location.pathname.split('/').pop();

    const token = localStorage.getItem("site");

    const { usuario } = useUsuario(idUsuario);

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        nome: '',
        ultimoNome: '',
        email: '',
        senha: '',
        role: 'USER'
    });

    useEffect(() => {
        if (usuario != null) {
            setFormData({
                nome: usuario.nome,
                ultimoNome: usuario.ultimoNome,
                email: usuario.email,
                senha: usuario.senha,
                role: usuario.role
            });
        }
    }, [usuario]);

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
            const { nome, ultimoNome, email, role } = formData;

            const response = await fetch(`http://localhost:8080/usuarios/editar/${idUsuario}`, {
                method: 'PUT',
                headers: {
                    'Content-Type' : 'application/json',
                    'Authorization' : 'Bearer ' + token
                },
                body: JSON.stringify({
                    nome: nome,
                    ultimoNome: ultimoNome,
                    email: email,
                    role: role
                })
            });

            if (response.status === 200) {
                alert('Usuário alterado com sucesso!');
                navigate('/usuarios'); // redireciona para lista de usuários
            } else {
                const errorData = await response.json();
                alert('Erro ao alterar usuário: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao alterar usuário:', error);
            alert('Erro na comunicação com o servidor.');
        }
    }

    return (
        <React.Fragment>
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <Card.Title as="h5">Editar Usuário</Card.Title>
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
                                                value={formData.role}
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
                                        {/* <Form.Group className="mb-3" controlId="senha">
                                            <Form.Label>Senha</Form.Label>
                                            <Form.Control
                                                type="password"
                                                name="senha"
                                                placeholder="Senha"
                                                value={formData.senha}
                                                onChange={handleChange}
                                            />
                                        </Form.Group> */}
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

export default EditarUsuario;