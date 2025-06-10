import React, { useState, useEffect } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';

const NovaAnotacao = () => {
    const navigate = useNavigate();
    const token = localStorage.getItem("site");

    const [errors, setErrors] = useState({});
    const [eventos, setEventos] = useState([]); // State to store the list of events

    const [formData, setFormData] = useState({
        anotacao: '',
        data: '',
        evento: { id: '' }
    });

    // Fetch events from the API when the component mounts
    useEffect(() => {
        const fetchEventos = async () => {
            try {
                const response = await fetch('http://localhost:8080/eventos/listar', {
                    headers: {
                        'Authorization': 'Bearer ' + token
                    }
                });
                if (response.ok) {
                    const data = await response.json();
                    setEventos(data);
                } else {
                    toast.error('Erro ao buscar eventos.');
                }
            } catch (error) {
                console.error('Erro ao buscar eventos:', error);
                toast.error('Erro na comunicação com o servidor.');
            }
        };

        fetchEventos();
    }, [token]);

    const validateFields = () => {
        const newErrors = {};
        if (!formData.anotacao) newErrors.anotacao = 'Anotação é obrigatória.';
        if (!formData.data) {
            newErrors.data = 'Data é obrigatória.';
        } else {
            const hoje = new Date();
            hoje.setHours(0, 0, 0, 0);
            const dataAnotacao = new Date(formData.data);
            if (dataAnotacao < hoje) {
                newErrors.data = 'A data deve ser hoje ou uma data futura.';
            }
        }
        if (!formData.evento.id) newErrors.evento = 'Selecione um evento.';
        return newErrors;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    // Updated to handle the event selection
    const handleEventoChange = (e) => {
        const { value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            evento: { id: value }
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const validationErrors = validateFields();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            toast.error('Por favor, corrija os erros no formulário.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/anotacoes/novo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                toast.success('Anotação cadastrada com sucesso!');
                setTimeout(() => {
                    navigate('/anotacoes');
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error('Erro ao cadastrar anotação: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao cadastrar anotação:', error);
            toast.error('Erro na comunicação com o servidor.');
        }
    };

    return (
        <React.Fragment>
            <ToastContainer />
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <Card.Title as="h5">Nova Anotação</Card.Title>
                        </Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="anotacao">
                                            <Form.Label>Anotação *</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="anotacao"
                                                value={formData.anotacao}
                                                placeholder="Digite a anotação"
                                                onChange={handleChange}
                                                isInvalid={!!errors.anotacao}
                                                isValid={formData.anotacao && !errors.anotacao}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.anotacao}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="data">
                                            <Form.Label>Data *</Form.Label>
                                            <Form.Control
                                                type="date"
                                                name="data"
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
                                        <Form.Group className="mb-3" controlId="evento.id">
                                            <Form.Label>Evento *</Form.Label>
                                            <Form.Select
                                                name="eventoId"
                                                value={formData.evento.id}
                                                onChange={handleEventoChange}
                                                isInvalid={!!errors.evento}
                                                isValid={formData.evento.id && !errors.evento}
                                            >
                                                <option value="">Selecione um evento</option>
                                                {eventos.map(evento => (
                                                    <option key={evento.id} value={evento.id}>
                                                        {evento.nome}
                                                    </option>
                                                ))}
                                            </Form.Select>
                                            <Form.Control.Feedback type="invalid">
                                                {errors.evento}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md={12}>
                                        <Link to="/anotacoes">
                                            <Button variant="secondary" className="me-2">Cancelar</Button>
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

export default NovaAnotacao;