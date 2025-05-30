import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

const EditarDoacao = () => {
  const idDoacao = window.location.pathname.split('/').pop();
  const token = localStorage.getItem("site");
  const navigate = useNavigate();

  const [apoiadores, setApoiadores] = useState([]);
  const [caixas, setCaixas] = useState([]);
  const [errors, setErrors] = useState({});

  const [formData, setFormData] = useState({
    valor: '',
    data: '',
    idApoiador: '',
    idCaixa: '',
  });

  // Validação simples dos campos do formulário
  const validateFields = () => {
    const newErrors = {};

    if (!formData.valor || isNaN(parseFloat(formData.valor)) || parseFloat(formData.valor) <= 0) {
      newErrors.valor = 'Valor deve ser maior que zero.';
    }

    if (!formData.data) {
      newErrors.data = 'Data é obrigatória.';
    } else {
      const hojeStr = new Date().toISOString().split('T')[0];
      if (formData.data < hojeStr) {
        newErrors.data = 'A data deve ser hoje ou uma data futura.';
      }
    }

    if (!formData.idApoiador) {
      newErrors.idApoiador = 'Apoiador é obrigatório.';
    }

    if (!formData.idCaixa) {
      newErrors.idCaixa = 'Caixa é obrigatório.';
    }

    return newErrors;
  };

  // Buscar apoiadores e caixas para popular selects
  useEffect(() => {
    const fetchApoiadores = async () => {
      try {
        const res = await fetch("http://localhost:8080/apoiador/listar", {
          headers: { 'Authorization': 'Bearer ' + token }
        });
        if (res.ok) {
          const data = await res.json();
          setApoiadores(data);
        }
      } catch (error) {
        toast.error("Erro ao buscar apoiadores.");
      }
    };

    const fetchCaixas = async () => {
      try {
        const res = await fetch("http://localhost:8080/caixa/listar", {
          headers: { 'Authorization': 'Bearer ' + token }
        });
        if (res.ok) {
          const data = await res.json();
          setCaixas(data);
        }
      } catch (error) {
        toast.error("Erro ao buscar caixas.");
      }
    };

    fetchApoiadores();
    fetchCaixas();
  }, [token]);

  // Buscar dados da doação atual para popular o formulário
  useEffect(() => {
    const fetchDoacao = async () => {
      try {
        const res = await fetch(`http://localhost:8080/doacao-monetaria/${idDoacao}`, {
          headers: { 'Authorization': 'Bearer ' + token }
        });
        if (res.ok) {
          const data = await res.json();
          setFormData({
            valor: data.valor,
            data: data.data ? data.data.split('T')[0] : '',
            idApoiador: data.idApoiador || '',
            idCaixa: data.idCaixa || '',
          });
        } else {
          toast.error("Doação não encontrada.");
          navigate('/doacoes');
        }
      } catch (error) {
        toast.error("Erro ao buscar doação.");
        navigate('/doacoes');
      }
    };

    fetchDoacao();
  }, [idDoacao, token, navigate]);

 const handleChange = (e) => {
  const { name, value } = e.target;
  setFormData(prev => {
    const newForm = { ...prev, [name]: value };
    console.log('formData updated:', newForm);
    return newForm;
  });
};

const handleSubmit = async (e) => {
  e.preventDefault();

  const validationErrors = validateFields();
  if (Object.keys(validationErrors).length > 0) {
    setErrors(validationErrors);
    return;
  }

  const dados = {
    valor: parseFloat(formData.valor),
    data: formData.data,
    apoiador: { id: parseInt(formData.idApoiador) },
    caixa: { id: parseInt(formData.idCaixa) }
  };

  try {
    const response = await fetch(`http://localhost:8080/doacao-monetaria/editar/${idDoacao}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      },
      body: JSON.stringify(dados),
    });

    if (response.ok) {
      toast.success("Doação atualizada com sucesso!");
      navigate("/doacoes");
    } else {
      const erro = await response.text();
      console.error("Erro ao atualizar:", erro);
      toast.error("Erro ao atualizar a doação.");
    }
  } catch (error) {
    console.error("Erro na requisição:", error);
    toast.error("Erro na requisição.");
  }
};
  return (
    <>
      <ToastContainer />
      <Row>
        <Col sm={12}>
          <Card>
            <Card.Header>
              <Card.Title as="h5">Editar Doação Monetária</Card.Title>
            </Card.Header>
            <Card.Body>
              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="valor">
                      <Form.Label>Valor *</Form.Label>
                      <Form.Control
                        type="number"
                        step="0.01"
                        min="0"
                        name="valor"
                        value={formData.valor}
                        placeholder="Valor da doação"
                        onChange={handleChange}
                        isInvalid={!!errors.valor}
                        isValid={formData.valor && !errors.valor}
                      />
                      <Form.Control.Feedback type="invalid">{errors.valor}</Form.Control.Feedback>
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
                      <Form.Control.Feedback type="invalid">{errors.data}</Form.Control.Feedback>
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="idApoiador">
                      <Form.Label>Apoiador *</Form.Label>
                      <Form.Select
                        name="idApoiador"
                        value={formData.idApoiador}
                        onChange={handleChange}
                        isInvalid={!!errors.idApoiador}
                        isValid={formData.idApoiador && !errors.idApoiador}
                      >
                        <option value="">Selecione um apoiador</option>
                        {apoiadores.map(a => (
                          <option key={a.id} value={a.id}>
                            {a.nome}
                          </option>
                        ))}
                      </Form.Select>
                      <Form.Control.Feedback type="invalid">{errors.idApoiador}</Form.Control.Feedback>
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="idCaixa">
                      <Form.Label>Caixa *</Form.Label>
                      <Form.Select
                        name="idCaixa"
                        value={formData.idCaixa}
                        onChange={handleChange}
                        isInvalid={!!errors.idCaixa}
                        isValid={formData.idCaixa && !errors.idCaixa}
                      >
                        <option value="">Selecione um caixa</option>
                        {caixas.map(c => (
                          <option key={c.id} value={c.id}>
                            {c.nome}
                          </option>
                        ))}
                      </Form.Select>
                      <Form.Control.Feedback type="invalid">{errors.idCaixa}</Form.Control.Feedback>
                    </Form.Group>
                  </Col>
                </Row>

                <Link to="/doacoes">
                  <Button variant="secondary" className="me-2">Cancelar</Button>
                </Link>
                <Button variant="primary" type="submit">Salvar</Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default EditarDoacao;