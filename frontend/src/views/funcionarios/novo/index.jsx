import React, { useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

const NovoFuncionario = () => {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        nome: '',
        email: '',
        data_nascimento: '',
        fone: '',
        endereco: '',
        sexo: '',
        cpf: ''
    });



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
            const response = await fetch('http://localhost:8080/funcionarios/novo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                alert('Funcionário cadastrado com sucesso!');
                navigate('/funcionarios'); // redireciona para lista de funci
            } else {
                const errorData = await response.json();
                alert('Erro ao cadastrar funcionario: ' + errorText);
            }
        } catch (error) {
            console.error('Erro ao cadastrar funcionario:', error);
            alert('Erro na comunicação com o servidor.');
        }
    }

    return (
        <React.Fragment>
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <Card.Title as="h5">Novo Funcionário</Card.Title>
                        </Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                            <Row>
                                <Col md={6}>

                                <Form.Group className="mb-3" controlId="nome:">
                                        <Form.Label>Nome</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="nome"
                                            placeholder="Nome"
                                            value={formData.nome}
                                            onChange={handleChange}
                                        />
                                    </Form.Group>
                                    <Form.Group className="mb-3" controlId="email">
                                        <Form.Label>Email</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="email"
                                            value={formData.email}
                                            placeholder="email"
                                            onChange={handleChange}
                                        />
                                    </Form.Group>

                                    <Form.Group className="mb-3" controlId="data_nascimento:">
                                        <Form.Label>Data Nascimento</Form.Label>
                                        <Form.Control
                                            type="date"
                                            name="data_nascimento:"
                                            placeholder="data_nascimento"
                                            value={formData.data_nascimento}
                                            onChange={handleChange}
                                        />
                                    </Form.Group>

                                    

                                    <Form.Group className="mb-3" controlId="fone">
                                        <Form.Label>Telefone</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="fone"
                                            placeholder="Telefone"
                                            value={formData.fone}
                                            onChange={handleChange}
                                        />
                                    </Form.Group>

                                    


                                    
                                </Col>

                    
                                <Col md={6}>

                                    <Form.Group className="mb-3" controlId="endereco">
                                        <Form.Label>Endereço</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="endereco"
                                            placeholder="Endereço"
                                            value={formData.endereco}
                                            onChange={handleChange}
                                        />
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
                                        />
                                    </Form.Group>

                                    <div className="d-flex gap-2 mt-4">
                                        <Link to="/funcionarios">
                                            <Button variant="secondary">Cancelar</Button>
                                        </Link>
                                        <Button variant="primary" type="submit">Salvar</Button>
                                    </div>
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

export default NovoFuncionario;