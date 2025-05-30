import React, { useState } from "react";
import { Button, Card, Col, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';

const NovaDoacaoMonetaria = () => {
    const navigate = useNavigate();
    const token = localStorage.getItem("site");

    const [errors, setErrors] = useState({});
  const [formData, setFormData] = useState({
  valor: '',
  data: '',
  idApoiador: '', // era apoiador: { id: '' }
  idCaixa: '',    // era caixa: { id: '' }
});

    const validateFields = () => {
        const newErrors = {};
        
        if (!formData.valor || parseFloat(formData.valor) <= 0) newErrors.valor = 'Valor deve ser maior que zero.';
        if (!formData.data) {
            newErrors.data = 'Data é obrigatória.';
        } else {
            const hoje = new Date();
            hoje.setHours(0, 0, 0, 0);
            const dataDoacao = new Date(formData.data);
            if (dataDoacao > new Date()) {
                newErrors.data = 'A data não pode ser futura.';
            }
        }
        
        if (!formData.idApoiador) newErrors.idApoiador = 'ID do apoiador é obrigatório.';
        if (!formData.idCaixa) newErrors.idCaixa = 'ID do caixa é obrigatório.';
        return newErrors;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

   const handleChange2 = (e) => {
  const { name, value } = e.target;
  if (name === "apoiadorId") {
    setFormData(prevState => ({
      ...prevState,
      apoiador: { id: Number(value) }
    }));
  } else if (name === "caixaId") {
    setFormData(prevState => ({
      ...prevState,
      caixa: { id: Number(value) }
    }));
  } else {
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  }
};

 const handleSubmit = async (e) => {
  e.preventDefault();

  const validationErrors = validateFields();
  if (Object.keys(validationErrors).length > 0) {
    setErrors(validationErrors);
    toast.error('Por favor, corrija os erros no formulário.');
    return;
  }

  // Criar uma cópia removendo os campos extras
  const dataToSend = {
  valor: parseFloat(formData.valor),
  data: formData.data,
  idApoiador: Number(formData.idApoiador),
  idCaixa: Number(formData.idCaixa)
};

  try {
    console.log(JSON.stringify(dataToSend));
    const response = await fetch('http://localhost:8080/doacao-monetaria/novo', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      },
      body: JSON.stringify(dataToSend)
    });

    if (response.ok) {
      toast.success('Doação cadastrada com sucesso!');
      setTimeout(() => {
        navigate('/doacoes/monetarias');
      }, 2000);
    } else {
      const errorData = await response.json();
      toast.error('Erro ao cadastrar doação: ' + (errorData.message || 'Erro desconhecido'));
    }
  } catch (error) {
    console.error('Erro ao cadastrar doação:', error);
    toast.error('Erro na comunicação com o servidor.');
  }
};

    return (
        <React.Fragment>
            <ToastContainer />
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <Card.Title as="h5">Nova Doação Monetária</Card.Title>
                        </Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    <Col md={6}>
                                        {/* <Form.Group className="mb-3" controlId="nome">
                                            <Form.Label>Nome *</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="nome"
                                                value={formData.nome}
                                                placeholder="Nome da doação"
                                                onChange={handleChange}
                                                isInvalid={!!errors.nome}
                                                isValid={formData.nome && !errors.nome}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.nome}
                                            </Form.Control.Feedback>
                                        </Form.Group> */}

                                        <Form.Group className="mb-3" controlId="valor">
                                            <Form.Label>Valor (R$) *</Form.Label>
                                            <Form.Control
                                                type="number"
                                                name="valor"
                                                value={formData.valor}
                                                placeholder="Valor da doação"
                                                onChange={handleChange}
                                                isInvalid={!!errors.valor}
                                                isValid={formData.valor && !errors.valor}
                                                step="0.01"
                                                min="0"
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.valor}
                                            </Form.Control.Feedback>
                                        </Form.Group>

                                        {/* <Form.Group className="mb-3" controlId="descricao">
                                            <Form.Label>Descrição</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="descricao"
                                                value={formData.descricao}
                                                placeholder="Descrição da doação"
                                                onChange={handleChange}
                                            />
                                        </Form.Group> */}
                                        <Form.Group className="mb-3" controlId="idCaixa">
                                          <Form.Label>ID do Caixa *</Form.Label>
                                          <Form.Control
                                            type="number"
                                            name="idCaixa"
                                            value={formData.idCaixa}
                                            placeholder="ID do caixa"
                                            onChange={handleChange}
                                            isInvalid={!!errors.idCaixa}
                                            isValid={formData.idCaixa && !errors.idCaixa}
                                          />
                                          <Form.Control.Feedback type="invalid">
                                            {errors.idCaixa}
                                          </Form.Control.Feedback>
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
                                            <Form.Control.Feedback type="invalid">
                                                {errors.data}
                                            </Form.Control.Feedback>
                                        </Form.Group>

                                        {/* <Form.Group className="mb-3" controlId="tipo">
                                            <Form.Label>Tipo *</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="tipo"
                                                placeholder="Ex: PIX, Dinheiro, Transferência"
                                                value={formData.tipo}
                                                onChange={handleChange}
                                                isInvalid={!!errors.tipo}
                                                isValid={formData.tipo && !errors.tipo}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.tipo}
                                            </Form.Control.Feedback>
                                        </Form.Group> */}

                                        <Form.Group className="mb-3" controlId="idApoiador">
                                        <Form.Label>ID do Apoiador *</Form.Label>
                                        <Form.Control
                                          type="number"
                                          name="idApoiador"
                                          value={formData.idApoiador}
                                          placeholder="ID do apoiador"
                                          onChange={handleChange}
                                          isInvalid={!!errors.idApoiador}
                                          isValid={formData.idApoiador && !errors.idApoiador}
                                        />
                                        <Form.Control.Feedback type="invalid">
                                          {errors.idApoiador}
                                        </Form.Control.Feedback>
                                      </Form.Group>

                                        <Link to="/doacoes/monetarias">
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
        </React.Fragment>
    );
};

export default NovaDoacaoMonetaria;