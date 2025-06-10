import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Card, Form, Button, Alert, Row, Col } from 'react-bootstrap';

const NovaDespesa = () => {
  const { idCaixa } = useParams();
  const navigate = useNavigate();
  const token = localStorage.getItem("site");

  const [descricao, setDescricao] = useState('');
  const [valor, setValor] = useState('');
  const [dataVencimento, setDataVencimento] = useState('');

  // Estados para validação visual
  const [isDescricaoValid, setIsDescricaoValid] = useState(null);
  const [isValorValid, setIsValorValid] = useState(null);
  const [isDataVencimentoValid, setIsDataVencimentoValid] = useState(null);

  const [mensagemErro, setMensagemErro] = useState('');
  const [mensagemSucesso, setMensagemSucesso] = useState('');

  const dataLancamento = new Date().toISOString().split('T')[0];

  // Validações
  const validarDescricao = (valor) => {
    const valido = valor.trim().length > 0;
    setIsDescricaoValid(valido);
    return valido;
  };

  const validarValor = (valor) => {
    const numero = parseFloat(valor);
    const valido = !isNaN(numero) && numero > 0;
    setIsValorValid(valido);
    return valido;
  };

  const validarDataVencimento = (data) => {
    if (!data) {
      setIsDataVencimentoValid(false);
      return false;
    }
    const hoje = new Date();
    const dataInformada = new Date(data + 'T00:00:00');
    const valido = dataInformada >= new Date(hoje.getFullYear(), hoje.getMonth(), hoje.getDate());
    setIsDataVencimentoValid(valido);
    return valido;
  };

  // Handlers para mudanças nos inputs
  const handleDescricaoChange = (e) => {
    setDescricao(e.target.value);
    validarDescricao(e.target.value);
  };

  const handleValorChange = (e) => {
    setValor(e.target.value);
    validarValor(e.target.value);
  };

  const handleDataVencimentoChange = (e) => {
    setDataVencimento(e.target.value);
    validarDataVencimento(e.target.value);
  };

  // Submit do form
  const handleSalvar = async (e) => {
    e.preventDefault();

    const descValida = validarDescricao(descricao);
    const valValido = validarValor(valor);
    const dataValida = validarDataVencimento(dataVencimento);

    if (!idCaixa) {
      setMensagemErro('ID do caixa não informado na URL.');
      setMensagemSucesso('');
      return;
    }

    const novaDespesa = {
      descricao,
      valor: parseFloat(valor),
      dataLancamento,
      dataVencimento
    };

    try {
      await axios.post(`http://localhost:8080/despesas/caixa/${idCaixa}`, novaDespesa, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      setMensagemSucesso('Despesa criada com sucesso!');
      setMensagemErro('');

      // Resetar formulário e validações
      setDescricao('');
      setValor('');
      setDataVencimento('');
      setIsDescricaoValid(null);
      setIsValorValid(null);
      setIsDataVencimentoValid(null);

      setTimeout(() => navigate('/despesas'), 1500);

    } catch (error) {
      console.error(error);
      setMensagemErro('Erro ao salvar despesa. Tente novamente mais tarde.');
      setMensagemSucesso('');
    }
  };

  return (
    <Row>
      <Col sm={12}>
        <Card>
          <Card.Header>
            <Card.Title as="h5">Nova Despesa - Caixa {idCaixa}</Card.Title>
          </Card.Header>
          <Card.Body>
            {mensagemErro && <Alert variant="danger">{mensagemErro}</Alert>}
            {mensagemSucesso && <Alert variant="success">{mensagemSucesso}</Alert>}

            <Form noValidate onSubmit={handleSalvar}>
              <Form.Group className="mb-3" controlId="descricao">
                <Form.Label>Descrição</Form.Label>
                <Form.Control
                  type="text"
                  value={descricao}
                  onChange={handleDescricaoChange}
                  isInvalid={isDescricaoValid === false}
                  isValid={isDescricaoValid === true}
                  placeholder="Digite a descrição da despesa"
                  required
                />
                <Form.Control.Feedback type="invalid">
                  A descrição é obrigatória.
                </Form.Control.Feedback>
                <Form.Control.Feedback type="valid">
                  Descrição válida.
                </Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3" controlId="valor">
                <Form.Label>Valor (R$)</Form.Label>
                <Form.Control
                  type="number"
                  step="0.01"
                  value={valor}
                  onChange={handleValorChange}
                  isInvalid={isValorValid === false}
                  isValid={isValorValid === true}
                  placeholder="Informe o valor da despesa"
                  required
                  min="0.01"
                />
                <Form.Control.Feedback type="invalid">
                  Informe um valor maior que zero.
                </Form.Control.Feedback>
                <Form.Control.Feedback type="valid">
                  Valor válido.
                </Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3" controlId="dataVencimento">
                <Form.Label>Data de Vencimento</Form.Label>
                <Form.Control
                  type="date"
                  value={dataVencimento}
                  onChange={handleDataVencimentoChange}
                  isInvalid={isDataVencimentoValid === false}
                  isValid={isDataVencimentoValid === true}
                  min={new Date().toISOString().split('T')[0]}
                  required
                />
                <Form.Control.Feedback type="invalid">
                  Data inválida. Não pode ser anterior à data atual.
                </Form.Control.Feedback>
                <Form.Control.Feedback type="valid">
                  Data válida.
                </Form.Control.Feedback>
              </Form.Group>

              <Button variant="secondary" onClick={() => navigate('/despesas')} className="me-2">
                Cancelar
              </Button>
              <Button variant="primary" type="submit">
                Salvar Despesa
              </Button>
            </Form>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};

export default NovaDespesa;
