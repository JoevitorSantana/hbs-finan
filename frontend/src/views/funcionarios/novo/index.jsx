import React, { useState } from "react";
import { Button, Card, Col, Form, Row, Alert } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import {
  validarCPF,
  validarTelefone,
  aplicarMascaraCPF,
  aplicarMascaraTelefone,
  validarEmail
} from "hooks/useFuncionarios";

const NovoFuncionario = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("site");

  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    dataNascimento: '',
    fone: '',
    endereco: '',
    sexo: '',
    cpf: ''
  });

  const [formValidado, setFormValidado] = useState(false);
  const [cpfErro, setCpfErro] = useState("");
  const [telefoneErro, setTelefoneErro] = useState("");
  const [erroServidor, setErroServidor] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    let novoValor = value;

    if (name === "cpf") {
      novoValor = aplicarMascaraCPF(value);
      setCpfErro(validarCPF(novoValor.replace(/\D/g, '')) ? "" : "CPF inválido");
    }

    if (name === "fone") {
      novoValor = aplicarMascaraTelefone(value);
      setTelefoneErro(validarTelefone(novoValor.replace(/\D/g, '')) ? "" : "Telefone inválido");
    }

    setFormData(prev => ({
      ...prev,
      [name]: novoValor
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setFormValidado(true);
    setErroServidor("");

    const form = e.currentTarget;
    if (form.checkValidity() === false) {
      e.stopPropagation();
      return;
    }

    const cpfLimpo = formData.cpf.replace(/\D/g, '');
    const foneLimpo = formData.fone.replace(/\D/g, '');
    const dataNascimento = new Date(formData.dataNascimento);
    const hoje = new Date();

    if (isNaN(dataNascimento.getTime())) {
      setErroServidor("Data de nascimento inválida.");
      return;
    }

    if (dataNascimento > hoje) {
      setErroServidor("A data de nascimento não pode ser no futuro.");
      return;
    }

    const idade = hoje.getFullYear() - dataNascimento.getFullYear();
    const aniversarioPassou =
      hoje.getMonth() > dataNascimento.getMonth() ||
      (hoje.getMonth() === dataNascimento.getMonth() && hoje.getDate() >= dataNascimento.getDate());

    const idadeFinal = aniversarioPassou ? idade : idade - 1;

    if (idadeFinal < 18) {
      setErroServidor("O funcionário deve ter no mínimo 18 anos.");
      return;
    }

    if (!validarCPF(cpfLimpo)) {
      setErroServidor("CPF inválido. Corrija antes de enviar.");
      return;
    }

    if (!validarTelefone(foneLimpo)) {
      setErroServidor("Telefone inválido. Corrija antes de enviar.");
      return;
    }

    if (!validarEmail(formData.email)) {
      setErroServidor("Email inválido. Corrija antes de enviar.");
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/funcionarios/novo', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
          ...formData,
          cpf: cpfLimpo,
          fone: foneLimpo,
          dataNascimento: formData.dataNascimento
        })
      });

      if (response.ok) {
        toast.success("Funcionário cadastrado com sucesso!");
        setTimeout(() => {
          navigate('/funcionarios');
        }, 1500);
      } else {
        const errorData = await response.json();
        if (errorData.message && errorData.message.toLowerCase().includes("cpf")) {
          setCpfErro(errorData.message);
        } else {
          setErroServidor(errorData.message || 'Erro desconhecido ao cadastrar funcionário.');
        }
      }
    } catch (error) {
      console.error("Erro na comunicação:", error);
      setErroServidor('Erro na comunicação com o servidor.');
    }
  };

  return (
    <Row>
      <Col sm={12}>
        <Card>
          <Card.Header>
            <Card.Title as="h5">Novo Funcionário</Card.Title>
          </Card.Header>
          <Card.Body>
            {erroServidor && <Alert variant="danger">{erroServidor}</Alert>}
            <Form noValidate className={formValidado ? "was-validated" : ""} onSubmit={handleSubmit}>
              <Row>
                <Col md={6}>
                  <Form.Group className="mb-3" controlId="nome">
                    <Form.Label>Nome <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="text"
                      name="nome"
                      value={formData.nome}
                      onChange={handleChange}
                      required
                      isInvalid={formValidado && !formData.nome}
                      isValid={formValidado && !!formData.nome}
                    />
                    <Form.Control.Feedback type="invalid">
                      O nome é obrigatório.
                    </Form.Control.Feedback>
                    <Form.Control.Feedback type="valid">
                      Tudo certo!
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group className="mb-3" controlId="email">
                    <Form.Label>Email <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="email"
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      required
                      isInvalid={formValidado && (!formData.email || !validarEmail(formData.email))}
                      isValid={formValidado && formData.email && validarEmail(formData.email)}
                    />
                    <Form.Control.Feedback type="invalid">
                      Email é obrigatório.
                    </Form.Control.Feedback>
                    <Form.Control.Feedback type="valid">
                      Tudo certo!
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group className="mb-3" controlId="dataNascimento">
                    <Form.Label>Data de Nascimento <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="date"
                      name="dataNascimento"
                      value={formData.dataNascimento}
                      onChange={handleChange}
                      required
                      isInvalid={formValidado && !formData.dataNascimento}
                      isValid={formValidado && !!formData.dataNascimento}
                    />
                    <Form.Control.Feedback type="invalid">
                      Data de nascimento obrigatória.
                    </Form.Control.Feedback>
                    <Form.Control.Feedback type="valid">
                      Tudo certo!
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group className="mb-3" controlId="fone">
                    <Form.Label>Telefone <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="text"
                      name="fone"
                      value={formData.fone}
                      onChange={handleChange}
                      required
                      isInvalid={formValidado && (!formData.fone || !validarTelefone(formData.fone.replace(/\D/g, '')))}
                      isValid={formValidado && formData.fone && validarTelefone(formData.fone.replace(/\D/g, ''))}
                    />
                    <Form.Control.Feedback type="invalid">
                      Telefone é obrigatório.
                    </Form.Control.Feedback>
                    <Form.Control.Feedback type="valid">
                      Tudo certo!
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

                  <Form.Group className="mb-3" controlId="sexo">
                    <Form.Label>Sexo</Form.Label>
                    <Form.Select
                      name="sexo"
                      value={formData.sexo}
                      onChange={handleChange}
                    >
                      <option value="">Selecione</option>
                      <option value="Masculino">Masculino</option>
                      <option value="Feminino">Feminino</option>
                    </Form.Select>
                  </Form.Group>

                  <Form.Group className="mb-3" controlId="cpf">
                    <Form.Label>CPF <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="text"
                      name="cpf"
                      value={formData.cpf}
                      onChange={handleChange}
                      maxLength={14}
                      required
                      isInvalid={formValidado && (!formData.cpf || !validarCPF(formData.cpf.replace(/\D/g, '')))}
                      isValid={formValidado && formData.cpf && validarCPF(formData.cpf.replace(/\D/g, ''))}
                    />
                    <Form.Control.Feedback type="invalid">
                      {cpfErro || "CPF é obrigatório."}
                    </Form.Control.Feedback>
                    <Form.Control.Feedback type="valid">
                      Tudo certo!
                    </Form.Control.Feedback>
                  </Form.Group>
                </Col>

                <Col md={12}>
                  <Link to="/funcionarios">
                    <Button variant="secondary" className="me-2">Cancelar</Button>
                  </Link>
                  <Button variant="primary" type="submit">Cadastrar</Button>
                </Col>
              </Row>
            </Form>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};

export default NovoFuncionario;
