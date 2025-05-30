import React, { useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';

const NovaDoacaoInstituicao = () => {
    const navigate = useNavigate();
    const token = localStorage.getItem("site");

    const [errors, setErrors] = useState({});
    const [formData, setFormData] = useState({
        nome: '',
        cnpj: '',
        data: '',
        valor: '',
        idCaixa: ''
    });

    const validateFields = () => {
        const newErrors = {};
        if (!formData.nome.trim()) newErrors.nome = 'Nome da instituição é obrigatório.';
        if (!formData.cnpj.trim()) newErrors.cnpj = 'CNPJ é obrigatório.';
        if (!formData.valor || parseFloat(formData.valor) <= 0) newErrors.valor = 'Valor deve ser maior que zero.';
        if (!formData.data) {
            newErrors.data = 'Data é obrigatória.';
        } else {
            const hoje = new Date();
            hoje.setHours(0, 0, 0, 0);
            const dataDoacao = new Date(formData.data);
            if (dataDoacao > hoje) {
                newErrors.data = 'A data não pode ser futura.';
            }
        }
        if (!formData.idCaixa) newErrors.idCaixa = 'ID do caixa é obrigatório.';
        return newErrors;
    };

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
            toast.error('Por favor, corrija os erros no formulário.');
            return;
        }

        const dataToSend = {
            nome: formData.nome,
            cnpj: formData.cnpj,
            data: formData.data,
            valor: parseFloat(formData.valor),
            idCaixa: Number(formData.idCaixa)
        };

        try {
            const response = await fetch('http://localhost:8080/doacao-instituicao/novo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify(dataToSend)
            });

            if (response.ok) {
                toast.success('Doação para instituição cadastrada com sucesso!');
                setTimeout(() => {
                    navigate('/doacoes/instituicoes');
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error('Erro ao cadastrar: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao cadastrar:', error);
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
                            <Card.Title as="h5">Nova Doação para Instituição</Card.Title>
                        </Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="nome">
                                            <Form.Label>Nome da Instituição *</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="nome"
                                                value={formData.nome}
                                                placeholder="Nome da instituição"
                                                onChange={handleChange}
                                                isInvalid={!!errors.nome}
                                                isValid={formData.nome && !errors.nome}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.nome}
                                            </Form.Control.Feedback>
                                        </Form.Group>

                                        <Form.Group className="mb-3" controlId="cnpj">
                                            <Form.Label>CNPJ *</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="cnpj"
                                                value={formData.cnpj}
                                                placeholder="00.000.000/0000-00"
                                                onChange={handleChange}
                                                isInvalid={!!errors.cnpj}
                                                isValid={formData.cnpj && !errors.cnpj}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.cnpj}
                                            </Form.Control.Feedback>
                                        </Form.Group>

                                        <Form.Group className="mb-3" controlId="valor">
                                            <Form.Label>Valor (R$) *</Form.Label>
                                            <Form.Control
                                                type="number"
                                                name="valor"
                                                value={formData.valor}
                                                placeholder="Valor da doação"
                                                onChange={handleChange}
                                                isInvalid={!!errors.valor}
                                                isValid={formData.valor && !errors.valor}
                                                step="0.01"
                                                min="0"
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.valor}
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

                                        <Form.Group className="mb-3" controlId="idCaixa">
                                            <Form.Label>ID do Caixa *</Form.Label>
                                            <Form.Control
                                                type="number"
                                                name="idCaixa"
                                                value={formData.idCaixa}
                                                placeholder="ID do caixa"
                                                onChange={handleChange}
                                                isInvalid={!!errors.idCaixa}
                                                isValid={formData.idCaixa && !errors.idCaixa}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.idCaixa}
                                            </Form.Control.Feedback>
                                        </Form.Group>

                                        <Link to="/doacao/instituicoes">
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
};

export default NovaDoacaoInstituicao;