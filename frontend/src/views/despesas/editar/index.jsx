import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row, Alert } from "react-bootstrap";
import { Link, useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { useDespesas } from "hooks/useDespesas";

const EditarDespesas = () => {
  const { idDespesa } = useParams();
  const token = localStorage.getItem("site");
  const navigate = useNavigate();

  const getDataAtualFormatada = () => {
    const hoje = new Date();
    return hoje.toISOString().split('T')[0]; // yyyy-mm-dd
  };

  const [formData, setFormData] = useState({
    dataLancamento: getDataAtualFormatada(),
    dataVencimento: '',
    descricao: '',
    pagamentoTotal: '',
    valor: '',
    dataQuitacao: ''
  });

  const [erroServidor, setErroServidor] = useState("");
  const [errosValidacao, setErrosValidacao] = useState({});
  const [isQuitada, setIsQuitada] = useState(false); // Estado para saber se está quitada

  // Verifica se o token está expirado
  const isTokenExpired = () => {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 < Date.now();
    } catch (err) {
      return true;
    }
  };

  const { despesa } = useDespesas(idDespesa);

  const formatarData = (dataCompleta) => {
    if (!dataCompleta) return '';
    return new Date(dataCompleta).toISOString().split('T')[0];
  };

  useEffect(() => {
    if (despesa != null) {
      setFormData({
        dataLancamento: formatarData(despesa.dataLancamento),
        dataVencimento: formatarData(despesa.dataVencimento),
        descricao: despesa.descricao || '',
        pagamentoTotal: despesa.pagamentoTotal,
        valor: despesa.valor,
        dataQuitacao: formatarData(despesa.dataQuitacao),
      });

      setIsQuitada(!!despesa.dataQuitacao); // true se já quitada
    } else {
      setFormData(prev => ({
        ...prev,
        dataLancamento: getDataAtualFormatada()
      }));
      setIsQuitada(false);
    }
  }, [despesa]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    setErrosValidacao(prev => ({ ...prev, [name]: null }));
  };

  const validarCampos = () => {
    const erros = {};
    const dataLanc = formData.dataLancamento ? new Date(formData.dataLancamento) : null;
    const dataVenc = formData.dataVencimento ? new Date(formData.dataVencimento) : null;

    if (!formData.dataLancamento) {
      erros.dataLancamento = "Data de lançamento é obrigatória.";
    }

    if (!formData.dataVencimento) {
      erros.dataVencimento = "Data de vencimento é obrigatória.";
    } else if (dataLanc && dataVenc && dataVenc < dataLanc) {
      erros.dataVencimento = "Data de vencimento deve ser igual ou posterior à data de lançamento.";
    }

    if (!formData.descricao || formData.descricao.trim() === "") {
      erros.descricao = "Descrição é obrigatória.";
    }

    if (!formData.valor) {
      erros.valor = "Valor é obrigatório.";
    } else if (isNaN(Number(formData.valor))) {
      erros.valor = "Valor deve ser um número válido.";
    } else if (Number(formData.valor) < 0) {
      erros.valor = "Valor deve ser um número positivo.";
    }

    return erros;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErroServidor("");

    if (isQuitada) {
      setErroServidor("Não é possível editar uma despesa já quitada.");
      return;
    }

    if (!token || isTokenExpired()) {
      setErroServidor("Sessão expirada. Faça login novamente.");
      return;
    }

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
            {erroServidor && <Alert variant={isQuitada ? "info" : "danger"}>{erroServidor}</Alert>}
            {isQuitada && (
              <Alert variant="info">
                Esta despesa já está quitada e não pode ser editada.
              </Alert>
            )}
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
                      disabled={isQuitada}
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
                      disabled={isQuitada}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.dataVencimento}
                    </Form.Control.Feedback>
                  </Form.Group>
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
                      disabled={isQuitada}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.valor}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group className="mb-3" controlId="descricao">
                    <Form.Label>Descrição <span style={{ color: 'red' }}>*</span></Form.Label>
                    <Form.Control
                      type="text"
                      name="descricao"
                      value={formData.descricao}
                      onChange={handleChange}
                      isInvalid={!!errosValidacao.descricao}
                      disabled={isQuitada}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errosValidacao.descricao}
                    </Form.Control.Feedback>
                  </Form.Group>
                </Col>

                <Col md={12}>
                  <Link to="/despesas">
                    <Button variant="secondary" className="me-2">Cancelar</Button>
                  </Link>
                  <Button variant="primary" type="submit" disabled={isQuitada}>
                    {isQuitada ? "Despesa Quitada" : "Salvar Alterações"}
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

export default EditarDespesas;
