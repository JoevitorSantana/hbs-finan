import { useApoiador } from "hooks/useApoiador";
import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

const EditarApoiador = () => {
    const idApoiador = window.location.pathname.split('/').pop();
    const token = localStorage.getItem("site");

    const { apoiador } = useApoiador(idApoiador);
    const [cpfErro, setCpfErro] = useState("");
    const [telefoneErro, setTelefoneErro] = useState("");
    const [erroServidor, setErroServidor] = useState("");
    const [validationErrors, setValidationErrors] = useState({});

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        cpf: '',
        data_nasc: '',
        email: '',
        endereco: '',
        fone: '',
        nome: '',
        sexo: '',
    });

    useEffect(() => {
        if (apoiador != null) {
            // Formata a data para yyyy-MM-dd para o input date funcionar corretamente
            const dataFormatada = apoiador.data_nasc
                ? new Date(apoiador.data_nasc).toISOString().split('T')[0]
                : '';

            setFormData({
                cpf: apoiador.cpf,
                data_nasc: dataFormatada,
                email: apoiador.email,
                endereco: apoiador.endereco,
                fone: apoiador.fone,
                nome: apoiador.nome,
                sexo: apoiador.sexo,
            });
        }
    }, [apoiador]);

    const validarCPF = (cpf) => {
        return cpf.length === 11;
    };

    const validarTelefone = (telefone) => {
        return telefone.length >= 10;
    };

    const validarCampos = () => {
        const errors = {};

        if (!formData.nome.trim()) errors.nome = "Nome é obrigatório";
        if (!formData.email.trim()) errors.email = "Email é obrigatório";
        if (!formData.cpf.trim() || cpfErro) errors.cpf = "CPF inválido ou vazio";
        if (!formData.data_nasc.trim()) errors.data_nasc = "Data de nascimento é obrigatória";
        if (!formData.fone.trim() || telefoneErro) errors.fone = "Telefone inválido ou vazio";
        if (!formData.sexo.trim()) errors.sexo = "Sexo é obrigatório";

        setValidationErrors(errors);

        return Object.keys(errors).length === 0;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === "cpf") {
            const somenteNumeros = value.replace(/\D/g, '').slice(0, 11);

            let cpfFormatado = somenteNumeros;
            if (somenteNumeros.length > 3)
                cpfFormatado = somenteNumeros.replace(/(\d{3})(\d)/, '$1.$2');
            if (somenteNumeros.length > 6)
                cpfFormatado = cpfFormatado.replace(/(\d{3})(\d)/, '$1.$2');
            if (somenteNumeros.length > 9)
                cpfFormatado = cpfFormatado.replace(/(\d{3})(\d{1,2})$/, '$1-$2');

            setCpfErro(somenteNumeros.length === 11 && !validarCPF(somenteNumeros) ? "CPF inválido" : "");

            setFormData(prev => ({
                ...prev,
                cpf: cpfFormatado
            }));
            return;
        }

        if (name === "fone") {
            const somenteNumeros = value.replace(/\D/g, '').slice(0, 11);
            let telefoneFormatado = somenteNumeros;

            if (somenteNumeros.length > 0) {
                telefoneFormatado = telefoneFormatado.replace(/^(\d{2})(\d)/g, '($1) $2');
            }
            if (somenteNumeros.length > 6) {
                telefoneFormatado = telefoneFormatado.replace(/(\d{5})(\d)/, '$1-$2');
            } else if (somenteNumeros.length > 2) {
                telefoneFormatado = telefoneFormatado.replace(/(\d{4})(\d)/, '$1-$2');
            }

            setTelefoneErro(somenteNumeros.length >= 10 && !validarTelefone(somenteNumeros) ? "Telefone inválido" : "");

            setFormData(prev => ({
                ...prev,
                fone: telefoneFormatado
            }));
            return;
        }

        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validarCampos()) {
            toast.error("Por favor, corrija os erros no formulário.");
            return;
        }

        try {
            const { cpf, data_nasc, email, endereco, fone, nome, sexo } = formData;
            console.log("Dados enviados:", formData);

            const response = await fetch(`http://localhost:8080/apoiador/editar/${idApoiador}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify({ cpf, data_nasc, email, endereco, fone, nome, sexo })
            });

            if (response.ok) {
                toast.success("Apoiador alterado com sucesso!");
                setTimeout(() => {
                    navigate('/apoiador');
                }, 1500);
            } else {
                const errorData = await response.json();
                setErroServidor(errorData.message || 'Erro desconhecido ao alterar o apoiador.');
            }
        } catch (error) {
            setErroServidor('Erro na comunicação com o servidor.');
        }
    }

    return (
        <>
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <Card.Title as="h5">Editar Apoiador</Card.Title>
                        </Card.Header>
                        <Card.Body>

                            <ToastContainer position="top-right" autoClose={3000} />

                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="nome">
                                            <Form.Label>Nome</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="nome"
                                                value={formData.nome}
                                                onChange={handleChange}
                                                isInvalid={!!validationErrors.nome}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {validationErrors.nome}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="email">
                                            <Form.Label>Email</Form.Label>
                                            <Form.Control
                                                type="email"
                                                name="email"
                                                value={formData.email}
                                                onChange={handleChange}
                                                isInvalid={!!validationErrors.email}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {validationErrors.email}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="endereco">
                                            <Form.Label>Endereço</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="endereco"
                                                value={formData.endereco}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="cpf">
                                            <Form.Label>CPF</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="cpf"
                                                value={formData.cpf}
                                                onChange={handleChange}
                                                isInvalid={!!validationErrors.cpf || !!cpfErro}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {validationErrors.cpf || cpfErro}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="data_nasc">
                                            <Form.Label>Data de Nascimento</Form.Label>
                                            <Form.Control
                                                type="date"
                                                name="data_nasc"
                                                value={formData.data_nasc || ''}
                                                onChange={handleChange}
                                                isInvalid={!!validationErrors.data_nasc}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {validationErrors.data_nasc}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="fone">
                                            <Form.Label>Telefone</Form.Label>
                                            <Form.Control
                                                type="tel"
                                                name="fone"
                                                value={formData.fone}
                                                onChange={handleChange}
                                                isInvalid={!!validationErrors.fone || !!telefoneErro}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {validationErrors.fone || telefoneErro}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="sexo">
                                            <Form.Label>Sexo</Form.Label>
                                            <Form.Control
                                                as="select"
                                                name="sexo"
                                                value={formData.sexo}
                                                onChange={handleChange}
                                                isInvalid={!!validationErrors.sexo}
                                            >
                                                <option value="">Selecione</option>
                                                <option value="FEMININO">FEMININO</option>
                                                <option value="MASCULINO">MASCULINO</option>
                                                <option value="PREFIRO_NAO_INFORMAR">PREFIRO NÃO INFORMAR</option>
                                            </Form.Control>
                                            <Form.Control.Feedback type="invalid">
                                                {validationErrors.sexo}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>

                                    <Col md={12}>
                                        <Link to="/apoiador">
                                            <Button variant="secondary" className="me-2">Cancelar</Button>
                                        </Link>
                                        <Button variant="primary" type="submit">Salvar</Button>
                                    </Col>
                                </Row>
                            </Form>

                            {erroServidor && <p className="text-danger mt-3">{erroServidor}</p>}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </>
    );
}

export default EditarApoiador;
