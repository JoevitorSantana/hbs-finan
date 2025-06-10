import { useAnotacao } from "hooks/useAnotacoes";
import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';

const EditarAnotacao = () => {
    const idAnotacao = window.location.pathname.split('/').pop();
    const token = localStorage.getItem("site");
    const navigate = useNavigate();

    const { anotacao } = useAnotacao(idAnotacao);

    const [errors, setErrors] = useState({});
    const [formData, setFormData] = useState({
        anotacao: '',
        data: '',
    });

    useEffect(() => {
        if (anotacao) {
            setFormData({
                anotacao: anotacao.anotacao || '',
                // Format the date correctly for the date input
                data: anotacao.data ? new Date(anotacao.data).toISOString().split('T')[0] : '',
            });
        }
    }, [anotacao]);

    const validateFields = () => {
        const newErrors = {};
        if (!formData.anotacao) newErrors.anotacao = 'Anotação é obrigatória.';
        
        // --- INÍCIO DA VALIDAÇÃO DA DATA ---
        if (!formData.data) {
            newErrors.data = 'Data é obrigatória.';
        } else {
            const hoje = new Date();
            hoje.setHours(0, 0, 0, 0); // Zera o tempo para comparar apenas as datas

            // Adiciona o fuso horário local para evitar problemas de um dia a menos
            const dataSelecionada = new Date(formData.data + 'T00:00:00');

            if (dataSelecionada < hoje) {
                newErrors.data = 'A data deve ser hoje ou uma data futura.';
            }
        }
        // --- FIM DA VALIDAÇÃO DA DATA ---

        return newErrors;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrors({}); // Limpa os erros antigos antes de validar novamente

        const validationErrors = validateFields();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            toast.error('Por favor, corrija os erros no formulário.');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/anotacoes/editar/${idAnotacao}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                toast.success('Anotação alterada com sucesso!');
                setTimeout(() => {
                    navigate('/anotacoes');
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error('Erro ao alterar anotação: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao alterar anotação:', error);
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
                            <Card.Title as="h5">Editar Anotação</Card.Title>
                        </Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    <Col md={12}>
                                        <Form.Group className="mb-3" controlId="anotacao">
                                            <Form.Label>Anotação *</Form.Label>
                                            <Form.Control
                                                as="textarea"
                                                rows={5}
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
};

export default EditarAnotacao;