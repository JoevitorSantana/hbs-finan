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
        qtd: '',
        dataValidade: ''
    });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (produto != null) {
            let dataFormatada = '';
            if (produto.dataValidade) {
                dataFormatada = produto.dataValidade.split('T')[0]; // yyyy-MM-dd
            }
            setFormData({
                nome: produto.nome,
                qtd: produto.qtd,
                dataValidade: dataFormatada
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

    const validate = () => {
        const newErrors = {};
        if (!formData.nome.trim()) newErrors.nome = "Nome é obrigatório.";
        if (formData.qtd === '' || isNaN(formData.qtd) || Number(formData.qtd) < 0)
            newErrors.qtd = "Quantidade deve ser um número positivo.";
        return newErrors;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            return;
        }
        setErrors({});

        const dataParaEnviar = formData.dataValidade === '' ? null : formData.dataValidade;
        const corpo = {
            nome: formData.nome,
            qtd: formData.qtd,
            dataValidade: dataParaEnviar
        };

        console.log('Enviando dados:', corpo);

        try {
            const response = await fetch(`http://localhost:8080/produtos/editar/${idProduto}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify(corpo)
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
    };

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
                                    <Col md={4}>
                                        <Form.Group className="mb-3" controlId="nome">
                                            <Form.Label>Nome</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="nome"
                                                value={formData.nome}
                                                placeholder="Nome"
                                                onChange={handleChange}
                                                isInvalid={!!errors.nome}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.nome}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md={4}>
                                        <Form.Group className="mb-3" controlId="qtd">
                                            <Form.Label>Quantidade</Form.Label>
                                            <Form.Control
                                                type="number"
                                                name="qtd"
                                                placeholder="Quantidade"
                                                value={formData.qtd}
                                                onChange={handleChange}
                                                isInvalid={!!errors.qtd}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.qtd}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md={4}>
                                        <Form.Group className="mb-3" controlId="dataValidade">
                                            <Form.Label>Data</Form.Label>
                                            <Form.Control
                                                type="date"
                                                name="dataValidade"
                                                value={formData.dataValidade}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>

                                <Link to="/produtos">
                                    <Button variant="secondary" className="me-2">Cancelar</Button>
                                </Link>
                                <Button variant="primary" type="submit">Salvar</Button>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </React.Fragment>
    );
};

export default EditarProduto;