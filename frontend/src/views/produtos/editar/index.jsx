import { useProduto } from "hooks/useProdutos";
import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

const EditarProduto = () => {
    const idProduto = window.location.pathname.split('/').pop();

    const token = localStorage.getItem("site");

    const { produto } = useProduto(idProduto);

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        nome: '',
        qtd: ''

    });

    useEffect(() => {
        if (produto != null) {
            setFormData({
                nome: produto.nome,
                qtd: produto.qtd
            });
        }
    }, [produto]);

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
            const { nome, qtd } = formData;

            const response = await fetch(`http://localhost:8080/produtos/editar/${idProduto}`, {
                method: 'PUT',
                headers: {
                    'Content-Type' : 'application/json',
                    'Authorization' : 'Bearer ' + token
                },
                body: JSON.stringify({
                    nome: nome,
                    qtd: qtd
                })
            });

            if (response.status === 200) {
                alert('Produto alterado com sucesso!');
                navigate('/produtos');
            } else {
                const errorData = await response.json();
                alert('Erro ao alterar produto: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao alterar produto:', error);
            alert('Erro na comunicação com o servidor.');
        }
    }

    return (
        <React.Fragment>
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <Card.Title as="h5">Editar Produto</Card.Title>
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
                                        <Form.Group className="mb-3" controlId="qtd">
                                            <Form.Label>Quantidade</Form.Label>
                                            <Form.Control
                                                type="number"
                                                name="qtd"
                                                placeholder="qtd"
                                                value={formData.qtd}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                        <Link to="/produtos">
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

export default EditarProduto;