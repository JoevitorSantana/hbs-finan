import { validarCPF, validarTelefone } from "hooks/useApoiador";
import React, { useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

const NovoApoiador = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("site");

  const [formData, setFormData] = useState({
    id: '',
    cpf: '',
    dataNasc: '',
    email: '',
    endereco: '',
    fone: '',
    nome: '',
    sexo: ''
  });

  const [cpfErro, setCpfErro] = useState("");
  const [telefoneErro, setTelefoneErro] = useState("");
  const [erroServidor, setErroServidor] = useState('');

  // Estado para erros dos campos obrigatórios
  const [camposObrigatoriosErro, setCamposObrigatoriosErro] = useState({});

  // Estado para indicar erro geral do formulário (para bordas vermelhas em todos os campos)
  const [formErro, setFormErro] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "dataNasc") {
      setFormData(prevState => ({
        ...prevState,
        dataNasc: value
      }));
      return;
    }

    if (name === "cpf") {
      const somenteNumeros = value.replace(/\D/g, '').slice(0, 11);

      let cpfFormatado = somenteNumeros;
      if (somenteNumeros.length > 3) {
        cpfFormatado = somenteNumeros.replace(/(\d{3})(\d)/, '$1.$2');
      }
      if (somenteNumeros.length > 6) {
        cpfFormatado = cpfFormatado.replace(/(\d{3})(\d)/, '$1.$2');
      }
      if (somenteNumeros.length > 9) {
        cpfFormatado = cpfFormatado.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
      }

      if (somenteNumeros.length === 11 && !validarCPF(somenteNumeros)) {
        setCpfErro("CPF inválido");
      } else {
        setCpfErro("");
      }

      setFormData(prevState => ({
        ...prevState,
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

      if (somenteNumeros.length >= 10 && !validarTelefone(somenteNumeros)) {
        setTelefoneErro("Telefone inválido");
      } else {
        setTelefoneErro("");
      }

      setFormData(prevState => ({
        ...prevState,
        fone: telefoneFormatado
      }));
      return;
    }

    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validação campos obrigatórios (sem id_grupo)
    const camposObrigatorios = ['nome', 'endereco', 'fone', 'dataNasc'];
    const novosErros = {};

    camposObrigatorios.forEach(campo => {
      if (!formData[campo]) {
        novosErros[campo] = true;
      }
    });

    if (cpfErro) {
      setFormErro(true);
      toast.error("CPF inválido.");
      return;
    }
    if (telefoneErro) {
      setFormErro(true);
      toast.error("Telefone inválido.");
      return;
    }

    if (Object.keys(novosErros).length > 0) {
      setCamposObrigatoriosErro(novosErros);
      setFormErro(true);
      toast.error("Preencha todos os campos obrigatórios corretamente.");
      return;
    }

    setCamposObrigatoriosErro({});
    setFormErro(false);

    // Remove id_grupo do que será enviado
    const dataToSend = { ...formData };

    try {
      const response = await fetch('http://localhost:8080/apoiador/novo', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(dataToSend)
      });

      if (response.ok) {
        toast.success("Apoiador cadastrado com sucesso!");
        setTimeout(() => {
          navigate('/apoiador');
        }, 1500);
      } else {
        const errorData = await response.json();
        setErroServidor(errorData.message || 'Erro desconhecido ao cadastrar o apoiador.');
      }
    } catch (error) {
      setErroServidor('Erro na comunicação com o servidor.');
    }
  };

  return (
    <>
      <Row>
        <Col sm={12}>
          <Card>
            <Card.Header>
              <Card.Title as="h5">Novo Apoiador</Card.Title>
            </Card.Header>
            <Card.Body>
              <ToastContainer
                position="top-right"
                autoClose={3000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                pauseOnFocusLoss
                draggable
                pauseOnHover
              />
              <Form onSubmit={handleSubmit} noValidate>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="nome">
                      <Form.Label>Nome <abbr title="campo obrigatório" style={{ color: 'red', fontWeight: 'bold' }}>*</abbr></Form.Label>
                      <Form.Control
                        type="text"
                        name="nome"
                        placeholder="Nome"
                        value={formData.nome}
                        onChange={handleChange}
                        isInvalid={formErro}
                      />
                      <Form.Control.Feedback type="invalid">
                        Nome é obrigatório.
                      </Form.Control.Feedback>
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="email">
                      <Form.Label>Email</Form.Label>
                      <Form.Control
                        type="email"
                        name="email"
                        placeholder="Email"
                        value={formData.email}
                        onChange={handleChange}
                        isInvalid={formErro}
                      />
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="endereco">
                      <Form.Label>Endereço <abbr title="campo obrigatório" style={{ color: 'red', fontWeight: 'bold' }}>*</abbr></Form.Label>
                      <Form.Control
                        type="text"
                        name="endereco"
                        placeholder="Endereço"
                        value={formData.endereco}
                        onChange={handleChange}
                        isInvalid={formErro}
                      />
                      <Form.Control.Feedback type="invalid">
                        Endereço é obrigatório.
                      </Form.Control.Feedback>
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="cpf">
                      <Form.Label>CPF</Form.Label>
                      <Form.Control
                        type="text"
                        name="cpf"
                        placeholder="000.000.000-00"
                        value={formData.cpf}
                        onChange={handleChange}
                        isInvalid={formErro}
                      />
                      <Form.Control.Feedback type="invalid">
                        {cpfErro || "CPF inválido."}
                      </Form.Control.Feedback>
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="dataNasc">
                      <Form.Label>Data de nascimento <abbr title="campo obrigatório" style={{ color: 'red', fontWeight: 'bold' }}>*</abbr></Form.Label>
                      <Form.Control
                        type="date"
                        name="dataNasc"
                        value={formData.dataNasc}
                        onChange={handleChange}
                        isInvalid={formErro}
                        max={new Date().toISOString().split('T')[0]}
                      />
                      <Form.Control.Feedback type="invalid">
                        Data de nascimento é obrigatória.
                      </Form.Control.Feedback>
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="fone">
                      <Form.Label>Telefone <abbr title="campo obrigatório" style={{ color: 'red', fontWeight: 'bold' }}>*</abbr></Form.Label>
                      <Form.Control
                        type="tel"
                        name="fone"
                        placeholder="(00) 00000-0000"
                        value={formData.fone}
                        onChange={handleChange}
                        isInvalid={formErro}
                      />
                      <Form.Control.Feedback type="invalid">
                        {telefoneErro || "Telefone é obrigatório."}
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
                        isInvalid={formErro}
                      >
                        <option value="" disabled hidden>SELECIONE A OPÇÃO</option>
                        <option value="FEMININO">FEMININO</option>
                        <option value="MASCULINO">MASCULINO</option>
                        <option value="PREFIRO_NAO_INFORMAR">PREFIRO NÃO INFORMAR</option>
                      </Form.Control>
                    </Form.Group>
                  </Col>
                </Row>

                <Row className="mt-3">
                  <Col>
                    <Link to="/apoiador">
                      <Button variant="secondary" className="me-2">Cancelar</Button>
                    </Link>
                    <Button variant="primary" type="submit">Salvar</Button>
                  </Col>
                </Row>
              </Form>

              {erroServidor && (
                <p className="text-danger mt-3">{erroServidor}</p>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default NovoApoiador;
