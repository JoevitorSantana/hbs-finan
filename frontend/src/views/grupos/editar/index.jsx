import { useGrupo } from "hooks/useGrupos";
import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

const EditarGrupo = () => {
    const idGrupo = window.location.pathname.split('/').pop();

    const token = localStorage.getItem("site");

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
                alert('Grupo alterado com sucesso!');
                navigate('/grupos');
            } else {
                const errorData = await response.json();
                alert('Erro ao alterar grupo: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao alterar grupo:', error);
            alert('Erro na comunicação com o servidor.');
        }
    }

    return (
        <React.Fragment>
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
                                            <Form.Label>Nome</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="nome"
                                                value={formData.nome}
                                                placeholder="Nome"
                                                onChange={handleChange}
                                            />
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