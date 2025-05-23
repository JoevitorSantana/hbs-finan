import React, { useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';

const NovoEvento = () => {

    const navigate = useNavigate();

    const token = localStorage.getItem("site");

    const [errors, setErrors] = useState({});

    const validateFields = () => {
        const newErrors = {};
        if (!formData.nome) newErrors.nome = 'Nome é obrigatório.';
        if (!formData.local) newErrors.local = 'Local é obrigatório.';
        if (!formData.data) newErrors.data = 'Data é obrigatória.';
        if (!formData.funcionario.id) newErrors.funcionario = 'ID do funcionario é obrigatório.';
        return newErrors;
    };

    const [formData, setFormData] = useState({
        local: '',
        data: '',
        descricao: '',
        nome: '',
        materiais: '',
        funcionario: { id: '' }
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleChange2 = (e) => {
    const { name, value } = e.target;

    if (name === "funcionarioId") {
        setFormData(prevState => ({
            ...prevState,
            funcionario: { id: value }
        }));
    } else {
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    }
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
            const response = await fetch('http://localhost:8080/eventos/novo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization' : 'Bearer ' + token
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                toast.success('Evento cadastrado com sucesso!');
                setTimeout(() => {
                    navigate('/eventos');
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error('Erro ao cadastrar evento: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao cadastrar evento:', error);
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
                            <Card.Title as="h5">Novo Evento</Card.Title>
                        </Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="local">
                                            <Form.Label>Local *</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="local"
                                                value={formData.local}
                                                placeholder="Local do evento"
                                                onChange={handleChange}
                                                isInvalid={!!errors.local}
                                                isValid={formData.local && !errors.local}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.local}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="data">
                                            <Form.Label>Data *</Form.Label>
                                            <Form.Control
                                                type="date"
                                                name="data"
                                                placeholder=""
                                                value={formData.data}
                                                onChange={handleChange}
                                                isInvalid={!!errors.data}
                                                isValid={formData.data && !errors.data}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.data}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="descricao">
                                            <Form.Label>Descrição</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="descricao"
                                                placeholder="Descrição do evento"
                                                value={formData.descricao}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>

                                        <Form.Group className="mb-3" controlId="nome">
                                            <Form.Label>Nome *</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="nome"
                                                placeholder="Nome do evento"
                                                value={formData.nome}
                                                onChange={handleChange}
                                                isInvalid={!!errors.nome}
                                                isValid={formData.nome && !errors.nome}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.nome}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                        <Form.Group className="mb-3" controlId="materiais">
                                            <Form.Label>Materiais</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="materiais"
                                                placeholder="Materiais necessarios para o evento"
                                                value={formData.materiais}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                        <Form.Group className="mb-3" controlId="funcionario.id">
                                            <Form.Label>ID do funcionario *</Form.Label>
                                            <Form.Control
                                                type="number"
                                                name="funcionarioId"
                                                value={formData.funcionario.id}
                                                placeholder="ID do funcionário"
                                                onChange={handleChange2}
                                                isInvalid={!!errors.funcionario}
                                                isValid={formData.funcionario.id && !errors.funcionario}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.funcionario}
                                            </Form.Control.Feedback>
                                        </Form.Group>

                                        <Link to="/eventos">
                                            <Button variant="secondary">Cancelar</Button>
                                        </Link>
                                        <Button variant="primary" type="submit">Salvar</Button>
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

export default NovoEvento;