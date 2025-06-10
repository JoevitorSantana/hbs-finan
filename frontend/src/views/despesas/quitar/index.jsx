import React, { useState, useEffect } from "react";
import { Button, Card, Col, Form, Row, Alert } from "react-bootstrap";
import { useParams, useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";

const QuitarDespesa = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const token = localStorage.getItem("site");

  // Função para pegar data atual no formato YYYY-MM-DD
  const getDataAtualFormatada = () => {
    const hoje = new Date();
    const ano = hoje.getFullYear();
    const mes = String(hoje.getMonth() + 1).padStart(2, "0"); // mês começa do 0
    const dia = String(hoje.getDate()).padStart(2, "0");
    return `${ano}-${mes}-${dia}`;
  };

  // Inicializa o estado com data atual no campo dataQuitacao
  const [formData, setFormData] = useState({
    dataQuitacao: getDataAtualFormatada(),
    pagamentoTotal: ""
  });

  const [errosValidacao, setErrosValidacao] = useState({});
  const [erroServidor, setErroServidor] = useState("");
  const [loading, setLoading] = useState(false);

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

    setLoading(true);

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
    } finally {
      setLoading(false);
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
                  <Form.Group controlId="dataQuitacao" className="mb-3">
                    <Form.Label>
                      Data da Quitação <span style={{ color: "red" }}>*</span>
                    </Form.Label>
                    <Form.Control
                      type="date"
                      name="dataQuitacao"
                      value={formData.dataQuitacao}
                      onChange={handleChange}
                      isInvalid={!!errosValidacao.dataQuitacao}
                      disabled={loading}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.dataQuitacao}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Col>

                <Col md={6}>
                  <Form.Group controlId="pagamentoTotal" className="mb-3">
                    <Form.Label>
                      Valor Pago <span style={{ color: "red" }}>*</span>
                    </Form.Label>
                    <Form.Control
                      type="text"
                      name="pagamentoTotal"
                      value={formData.pagamentoTotal}
                      onChange={handleChange}
                      isInvalid={!!errosValidacao.pagamentoTotal}
                      disabled={loading}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.pagamentoTotal}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Col>

                <Col md={12}>
                  <Link to="/despesas">
                    <Button variant="secondary" className="me-2" disabled={loading}>
                      Cancelar
                    </Button>
                  </Link>
                  <Button variant="primary" type="submit" disabled={loading}>
                    {loading ? "Processando..." : "Quitar"}
                  </Button>
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
