import { useGrupo } from "hooks/useGrupos";
import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';

const EditarGrupo = () => {
    const idGrupo = window.location.pathname.split('/').pop();

    const token = localStorage.getItem("site");

    const [errors, setErrors] = useState({});
        
        const validateFields = () => {
            const newErrors = {};
            if (!formData.nome) newErrors.nome = 'Nome é obrigatório.';
            return newErrors;
        };
    
    
    const { grupo } = useGrupo(idGrupo);

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        nome: ''
    });

    useEffect(() => {
        if (grupo != null) {
            setFormData({
                nome: grupo.nome
            });
        }
    }, [grupo]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const validationErrors = validateFields();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            toast.error('Por favor, corrija o erro no formulário.');
            return;
        }
        
        try {
            const { nome } = formData;

            const response = await fetch(`http://localhost:8080/grupos/editar/${idGrupo}`, {
                method: 'PUT',
                headers: {
                    'Content-Type' : 'application/json',
                    'Authorization' : 'Bearer ' + token
                },
                body: JSON.stringify({
                    nome: nome
                })
            });

            if (response.status === 200) {
                toast.success('Grupo alterado com sucesso!');
                setTimeout(() => {
                    navigate('/grupos');
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error('Erro ao alterar grupo: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao alterar grupo:', error);
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
                            <Card.Title as="h5">Editar Grupo</Card.Title>
                        </Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="nome">
                                            <Form.Label>Nome *</Form.Label>
                                            <Form.Control
                                                type="text"
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
                                        <Link to="/grupos">
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

export default EditarGrupo;