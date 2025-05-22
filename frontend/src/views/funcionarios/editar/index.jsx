import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row, Alert } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import {
  useFuncionario,
  validarCPF,
  aplicarMascaraCPF,
  aplicarMascaraTelefone,
  validarEmail
} from "hooks/useFuncionarios";

const EditarFuncionario = () => {
  const idFuncionario = window.location.pathname.split('/').pop();
  const token = localStorage.getItem("site");
  const { funcionario } = useFuncionario(idFuncionario);
  
  console.log('funcionario:', funcionario);

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    fone: '',
    endereco: '',
    dataNascimento: '',
    sexo: '',
    cpf: ''
  });

  const [erroServidor, setErroServidor] = useState("");

  const formatarData = (dataCompleta) => {
    if (!dataCompleta) return '';
    return new Date(dataCompleta).toISOString().split('T')[0];
  };

  useEffect(() => {
    if (funcionario != null) {
      setFormData({
        nome: funcionario.nome,
        email: funcionario.email,
        fone: aplicarMascaraTelefone(funcionario.fone),
        endereco: funcionario.endereco,
        dataNascimento: formatarData(funcionario.dataNascimento),
        sexo: funcionario.sexo,
        cpf: aplicarMascaraCPF(funcionario.cpf)
      });
    }
  }, [funcionario]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    let novoValor = value;

    if (name === 'cpf') {
      novoValor = aplicarMascaraCPF(value);
    } else if (name === 'fone') {
      novoValor = aplicarMascaraTelefone(value);
    }

    setFormData(prev => ({
      ...prev,
      [name]: novoValor
    }));
  };

  const handleSubmit = async (e) => {


    e.preventDefault();
    setErroServidor("");

    const cpfSemMascara = formData.cpf.replace(/[^\d]+/g, '');
    const foneSemMascara = formData.fone.replace(/[^\d]+/g, '');

    // Validação da data de naascimento
      const dataNascimento = new Date(formData.dataNascimento);
      const hoje = new Date();
      const idade = hoje.getFullYear() - dataNascimento.getFullYear();
      const aniversarioPassou =
      hoje.getMonth() > dataNascimento.getMonth() ||
      (hoje.getMonth() === dataNascimento.getMonth() && hoje.getDate() >= dataNascimento.getDate());

      const idadeFinal = aniversarioPassou ? idade : idade - 1;

      if (isNaN(dataNascimento.getTime())) {
        setErroServidor("Data de nascimento inválida.");
        return;
      }

      if (dataNascimento > hoje) {
        setErroServidor("A data de nascimento inválida");
        return;
      }

      if (idadeFinal < 18) {
        setErroServidor("O funcionário deve ter no mínimo 18 anos.");
        return;
      }

      if (!validarCPF(cpfSemMascara)) {
        setErroServidor("CPF inválido!");
        return;
      }

      if (!validarEmail(formData.email)) {
        setErroServidor("Email inválido!");
        return;
      }

    try {
      const { nome, email, endereco, dataNascimento, sexo } = formData;
      const response = await fetch(`http://localhost:8080/funcionarios/editar/${idFuncionario}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
          nome,
          email,
          fone: foneSemMascara,
          endereco,
          dataNascimento,
          sexo,
          cpf: cpfSemMascara
        })
      });

      if (response.status === 200) {
        toast.success("Funcionário atualizado com sucesso!");
        setTimeout(() => {
          navigate('/funcionarios');
        }, 1500);
      } else {
        const errorData = await response.json();
        setErroServidor(errorData.message || 'Erro desconhecido ao atualizar o funcionário.');
      }
    } catch (error) {
      setErroServidor('Erro na comunicação com o servidor.');
    }
  };

  return (
    <Row>
      <Col sm={12}>
        <Card>
          <Card.Header>
            <Card.Title as="h5">Editar Funcionário</Card.Title>
          </Card.Header>
          <Card.Body>
            {erroServidor && <Alert variant="danger">{erroServidor}</Alert>}
            <Form onSubmit={handleSubmit}>
              <Row>
                {/* Campos do formulário exatamente como antes */}
                <Col md={6}>
                  <Form.Group className="mb-3" controlId="nome">
                    <Form.Label>Nome</Form.Label>
                    <Form.Control
                      type="text"
                      name="nome"
                      value={formData.nome}
                      onChange={handleChange}
                      required
                    />
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
                      required
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group className="mb-3" controlId="dataNascimento">
                    <Form.Label>Data de Nascimento</Form.Label>
                    <Form.Control
                      type="date"
                      name="dataNascimento"
                      value={formData.dataNascimento}
                      onChange={handleChange}
                      required
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group className="mb-3" controlId="fone">
                    <Form.Label>Telefone</Form.Label>
                    <Form.Control
                      type="text"
                      name="fone"
                      value={formData.fone}
                      onChange={handleChange}
                      required
                    />
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
                  <Form.Group className="mb-3" controlId="sexo">
                    <Form.Label>Sexo</Form.Label>
                    <Form.Select name="sexo" value={formData.sexo} onChange={handleChange}>
                      <option value="">Selecione</option>
                      <option value="Masculino">Masculino</option>
                      <option value="Feminino">Feminino</option>
                    </Form.Select>
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
                      required
                    />
                  </Form.Group>
                </Col>
                <Col md={12}>
                  <Link to="/funcionarios">
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
  );
};

export default EditarFuncionario;
