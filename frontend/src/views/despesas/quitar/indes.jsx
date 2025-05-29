import React, { useState, useEffect } from "react";
import { Button, Card, Col, Form, Row, Alert } from "react-bootstrap";
import { useParams, useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";

const QuitarDespesa = () => {
  const { id } = useParams(); // pega o id da despesa da URL
  const navigate = useNavigate();
  const token = localStorage.getItem("site");

  const [formData, setFormData] = useState({
    dataQuitacao: "",
    pagamentoTotal: ""
  });

  const [errosValidacao, setErrosValidacao] = useState({});
  const [erroServidor, setErroServidor] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    setErrosValidacao((prev) => ({ ...prev, [name]: null }));
  };

  const validarCampos = () => {
    const erros = {};
    if (!formData.dataQuitacao) {
      erros.dataQuitacao = "Data de quitação é obrigatória.";
    }
    if (!formData.pagamentoTotal) {
      erros.pagamentoTotal = "Valor do pagamento é obrigatório.";
    } else if (isNaN(Number(formData.pagamentoTotal)) || Number(formData.pagamentoTotal) < 0) {
      erros.pagamentoTotal = "Valor deve ser um número positivo.";
    }
    return erros;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErroServidor("");
    const erros = validarCampos();
    if (Object.keys(erros).length > 0) {
      setErrosValidacao(erros);
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/despesas/quitar/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        toast.success("Despesa quitada com sucesso!");
        setTimeout(() => navigate("/despesas"), 1500);
      } else {
        const data = await response.json();
        setErroServidor(data.message || "Erro ao quitar a despesa.");
      }
    } catch (err) {
      console.error("Erro ao quitar:", err);
      setErroServidor("Erro de comunicação com o servidor.");
    }
  };

  return (
    <Row>
      <Col sm={12}>
        <Card>
          <Card.Header>
            <Card.Title as="h5">Quitar Despesa</Card.Title>
          </Card.Header>
          <Card.Body>
            {erroServidor && <Alert variant="danger">{erroServidor}</Alert>}
            <Form onSubmit={handleSubmit}>
              <Row>
                <Col md={6}>
                  <Form.Group className="mb-3" controlId="dataQuitacao">
                    <Form.Label>Data da Quitação <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="date"
                      name="dataQuitacao"
                      value={formData.dataQuitacao}
                      onChange={handleChange}
                      isInvalid={!!errosValidacao.dataQuitacao}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.dataQuitacao}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Col>

                <Col md={6}>
                  <Form.Group className="mb-3" controlId="pagamentoTotal">
                    <Form.Label>Valor Pago <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="text"
                      name="pagamentoTotal"
                      value={formData.pagamentoTotal}
                      onChange={handleChange}
                      isInvalid={!!errosValidacao.pagamentoTotal}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.pagamentoTotal}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Col>

                <Col md={12}>
                  <Link to="/despesas">
                    <Button variant="secondary" className="me-2">Cancelar</Button>
                  </Link>
                  <Button variant="primary" type="submit">Quitar</Button>
                </Col>
              </Row>
            </Form>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};

export default QuitarDespesa;
