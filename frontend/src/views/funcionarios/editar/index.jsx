import { useFuncionario } from "hooks/useFuncionario";
import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

const EditarFuncionario = () => {
    const idFuncionario = window.location.pathname.split('/').pop();

    const token = localStorage.getItem("site");

    const { funcionario } = useFuncionario(idFuncionario);

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        nome: '',
        email: '',
        fone: '',
        endereco: '',
        data_nascimento: '',
        sexo: '',
        cpf: ''
    });

    useEffect(() => {
        if (funcionario != null) {
            setFormData({
                nome: funcionario.nome,
                email: funcionario.email,
                fone: funcionario.fone,
                endereco: funcionario.endereco,
                data_nascimento: funcionario.data_nascimento,
                sexo: funcionario.sexo,
                cpf: funcionario.cpf
            });
        }
    }, [funcionario]);

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
            const { nome, email, fone, endereco, data_nascimento, sexo, cpf } = formData;

            const response = await fetch(`http://localhost:8080/funcionarios/editar/${idFuncionario}`, {
                method: 'PUT',
                headers: {
                    'Content-Type' : 'application/json',
                    'Authorization' : 'Bearer ' + token
                },
                body: JSON.stringify({
                    nome: nome,
                    email: email,
                    fone: fone,
                    endereco: endereco,
                    data_nascimento: data_nascimento,
                    sexo: sexo,
                    cpf: cpf

        
                })
            });

            if (response.status === 200) {
                alert('Funcionário alterado com sucesso!');
                navigate('/funcionarios'); // redireciona para lista de usuários
            } else {
                const errorData = await response.json();
                alert('Erro ao alterar funcionário: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao alterar funcionário:', error);
            alert('Erro na comunicação com o servidor.');
        }
    }

    return (
        <React.Fragment>
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <Card.Title as="h5">Editar Funcionário</Card.Title>
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
                                        <Form.Group className="mb-3" controlId="email">
                                            <Form.Label>Email</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="email"
                                                placeholder="Email"
                                                value={formData.email}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Form.Group className="mb-3" controlId="dataNascimento">
                                            <Form.Label>Data Nascimento</Form.Label>
                                            <Form.Control
                                                type="date"
                                                name="dataNascimento"
                                                placeholder="Data nascimento"
                                                value={formData.dataNascimento}
                                                onChange={handleChange}
                                            >
                                            </Form.Control>
                                        </Form.Group>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="fone">
                                            <Form.Label>Telefone</Form.Label>
                                            <Form.Control
                                                type="fone"
                                                name="fone"
                                                placeholder="Telefone"
                                                value={formData.fone}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>

                                        <Form.Group className="mb-3" controlId="endereco">
                                            <Form.Label>Endereço</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="endereco"
                                                placeholder="Endereco"
                                                value={formData.endereco}
                                                onChange={handleChange}
                                            >
                                            </Form.Control>
                                        </Form.Group>

                                        <Form.Group className="mb-3" controlId="sexo">
                                        <Form.Label>Sexo</Form.Label>
                                        <Form.Select
                                            name="sexo"
                                            value={formData.sexo}
                                            onChange={handleChange}
                                        >
                                            <option value="">Selecione</option>
                                            <option value="M">Masculino</option>
                                            <option value="F">Feminino</option>
                                        </Form.Select>
                                    </Form.Group>

                                        <Form.Group className="mb-3" controlId="cpf">
                                            <Form.Label>CPF</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="cpf"
                                                placeholder="CPF"
                                                value={formData.cpf}
                                                onChange={handleChange}
                                            >
                                            </Form.Control>
                                        </Form.Group>

                                        <Link to="/funcionarios">
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

export default EditarFuncionario;