import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row, Alert } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { useDespesa } from "../hooks/useDespesas";

const EditarDespesas = () => {
  const idDespesa = window.location.pathname.split('/').pop();
  const token = localStorage.getItem("site");
  const { despesa } = useDespesa(idDespesa);
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    dataLancamento: '',
    dataVencimento: '',
    Desc: '',
    pagamentoTotal: '',
    valor: '',
    dataQuitacao: ''
  });

  const [erroServidor, setErroServidor] = useState("");
  const [errosValidacao, setErrosValidacao] = useState({});

  const formatarData = (dataCompleta) => {
    if (!dataCompleta) return '';
    return new Date(dataCompleta).toISOString().split('T')[0];
  };

  useEffect(() => {
    if (despesa != null) {
      setFormData({
        dataLancamento: formatarData(despesa.dataLancamento),
        dataVencimento: formatarData(despesa.dataVencimento),
        Desc: despesa.Desc || '',
        pagamentoTotal: despesa.pagamentoTotal,
        valor: despesa.valor,
        dataQuitacao: formatarData(despesa.dataQuitacao),
      });
    }
  }, [despesa]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    setErrosValidacao(prev => ({ ...prev, [name]: null }));
  };

  const validarCampos = () => {
    const erros = {};
    const dataLanc = new Date(formData.dataLancamento);
    const dataVenc = new Date(formData.dataVencimento);
    const dataQuit = new Date(formData.dataQuitacao);

    if (!formData.dataLancamento) erros.dataLancamento = "Data de lançamento é obrigatória.";
    if (!formData.dataVencimento) erros.dataVencimento = "Data de vencimento é obrigatória.";
    if (!formData.Desc || formData.Desc.trim() === "") erros.Desc = "Descrição é obrigatória.";

    if (!formData.pagamentoTotal) {
      erros.pagamentoTotal = "Pagamento total é obrigatório.";
    } else if (isNaN(Number(formData.pagamentoTotal)) || Number(formData.pagamentoTotal) < 0) {
      erros.pagamentoTotal = "Pagamento total deve ser um número positivo.";
    }

    if (!formData.valor) {
      erros.valor = "Valor é obrigatório.";
    } else if (isNaN(Number(formData.valor)) || Number(formData.valor) < 0) {
      erros.valor = "Valor deve ser um número positivo.";
    }

    if (!formData.dataQuitacao) erros.dataQuitacao = "Data da quitação é obrigatória.";

    if (formData.dataLancamento && formData.dataVencimento && dataLanc > dataVenc) {
      erros.dataVencimento = "Data de vencimento deve ser igual ou posterior à data de lançamento.";
    }
    if (formData.dataQuitacao && formData.dataLancamento && dataQuit < dataLanc) {
      erros.dataQuitacao = "Data da quitação deve ser igual ou posterior à data de lançamento.";
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
      const response = await fetch(`http://localhost:8080/despesas/editar/${idDespesa}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        toast.success("Despesa atualizada com sucesso!");
        setTimeout(() => navigate('/despesas'), 1500);
      } else {
        const errorData = await response.json().catch(() => ({}));
        setErroServidor(errorData.message || "Erro ao atualizar a despesa.");
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
            <Card.Title as="h5">Editar Despesa</Card.Title>
          </Card.Header>
          <Card.Body>
            {erroServidor && <Alert variant="danger">{erroServidor}</Alert>}
            <Form onSubmit={handleSubmit}>
              <Row>
                <Col md={6}>
                  <Form.Group className="mb-3" controlId="dataLancamento">
                    <Form.Label>Data de Lançamento <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="date"
                      name="dataLancamento"
                      value={formData.dataLancamento}
                      onChange={handleChange}
                      isInvalid={!!errosValidacao.dataLancamento}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.dataLancamento}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group className="mb-3" controlId="dataVencimento">
                    <Form.Label>Data de Vencimento <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="date"
                      name="dataVencimento"
                      value={formData.dataVencimento}
                      onChange={handleChange}
                      isInvalid={!!errosValidacao.dataVencimento}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.dataVencimento}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group className="mb-3" controlId="Desc">
                    <Form.Label>Descrição <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="text"
                      name="Desc"
                      value={formData.Desc}
                      onChange={handleChange}
                      isInvalid={!!errosValidacao.Desc}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.Desc}
                    </Form.Control.Feedback>
                  </Form.Group>

                  {/* <Form.Group className="mb-3" controlId="pagamentoTotal">
                    <Form.Label>Pagamento Total <span style={{ color: 'red' }}>*</span></Form.Label>
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
                  </Form.Group> */}
                </Col>

                <Col md={6}>
                  <Form.Group className="mb-3" controlId="valor">
                    <Form.Label>Valor <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="text"
                      name="valor"
                      value={formData.valor}
                      onChange={handleChange}
                      isInvalid={!!errosValidacao.valor}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.valor}
                    </Form.Control.Feedback>
                  </Form.Group>

                 {/* <Form.Group className="mb-3" controlId="dataQuitacao">
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
                  </Form.Group> */}
                </Col>

                <Col md={12}>
                  <Link to="/despesas">
                    <Button variant="secondary" className="me-2">Cancelar</Button>
                  </Link>
                  <Button variant="primary" type="submit">Salvar Alterações</Button>
                </Col>
              </Row>
            </Form>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};

export default EditarDespesas;
