import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Form, Button, Card, Row, Col } from 'react-bootstrap';
import { toast, ToastContainer } from 'react-toastify';

const EditarDoacaoInstituicao = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const token = localStorage.getItem("site");

  // Estado para formulário
 const [formData, setFormData] = useState({
  instituicaoNome: '',
  cnpj: '',      // novo campo
  valor: '',
  data: '',
  caixaId: '',
});

  // Lista de caixas para select
  const [caixas, setCaixas] = useState([]);

  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(true);

  // Busca caixas para popular select
  useEffect(() => {
    const fetchCaixas = async () => {
      try {
        const res = await fetch('http://localhost:8080/caixa/listar', {
          headers: { 'Authorization': 'Bearer ' + token }
        });
        if (res.ok) {
          const data = await res.json();
          setCaixas(data);
        } else {
          toast.error("Erro ao carregar caixas");
        }
      } catch (error) {
        toast.error("Erro ao carregar caixas");
      }
    };

    fetchCaixas();
  }, [token]);

  // Buscar doação para popular formulário
  useEffect(() => {
    const fetchDoacao = async () => {
      try {

        const res = await fetch(`http://localhost:8080/doacao-instituicao/${id}`, {
          headers: { 'Authorization': 'Bearer ' + token }
        });
        if (res.ok) {
          const data = await res.json();
          setFormData({
            instituicaoNome: data.instituicao?.nome || '',
            cnpj: data.instituicao?.cnpj || '',
            valor: data.valor,
            data: data.data ? data.data.split('T')[0] : '',
            caixaId: data.caixa?.id || '',
            });
                    setLoading(false);
        } else {
          toast.error("Doação não encontrada.");
          navigate('/doacao/instituicao');
        }
      } catch (error) {
        toast.error("Erro ao buscar doação.");
        navigate('/doacao/instituicao');
      }
    };

    fetchDoacao();
  }, [id, token, navigate]);

  // Validação simples
  const validateFields = () => {
    const newErrors = {};

    if (!formData.instituicaoNome.trim()) {
      newErrors.instituicaoNome = 'Nome da instituição é obrigatório.';
    }

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

    if (!formData.caixaId) {
      newErrors.caixaId = 'Selecione um caixa.';
    }

    return newErrors;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationErrors = validateFields();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    // Monta dados para envio
    const dados = {
        nome: formData.instituicaoNome,
        cnpj: formData.cnpj,
        valor: parseFloat(formData.valor),
        data: formData.data,
        caixa: { id: parseInt(formData.caixaId, 10) },
        };

    try {
        console.log('Dados enviados:', JSON.stringify(dados));
      const response = await fetch(`http://localhost:8080/doacao-instituicao/editar/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token,
        },
        body: JSON.stringify(dados),
      });

      if (response.ok) {
        toast.success('Doação atualizada com sucesso!');
        setTimeout(() => navigate('/doacao/instituicao'), 1500);
      } else {
        const errorData = await response.json();
        toast.error(errorData.message || 'Erro ao atualizar doação.');
      }
    } catch (error) {
      toast.error('Erro na comunicação com o servidor.');
    }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <>
      <ToastContainer position="top-right" autoClose={3000} />
      <Row>
        <Col sm={12}>
          <Card>
            <Card.Header>
              <h5>Editar Doação #{id}</h5>
            </Card.Header>
            <Card.Body>
              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="instituicaoNome">
                      <Form.Label>Instituição *</Form.Label>
                      <Form.Control
                        type="text"
                        name="instituicaoNome"
                        value={formData.instituicaoNome}
                        onChange={handleChange}
                        isInvalid={!!errors.instituicaoNome}
                        isValid={formData.instituicaoNome && !errors.instituicaoNome}
                        placeholder="Nome da instituição"
                      />
                      <Form.Control.Feedback type="invalid">{errors.instituicaoNome}</Form.Control.Feedback>
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="caixaId">
                      <Form.Label>Caixa *</Form.Label>
                      <Form.Select
                        name="caixaId"
                        value={formData.caixaId}
                        onChange={handleChange}
                        isInvalid={!!errors.caixaId}
                        isValid={formData.caixaId && !errors.caixaId}
                      >
                        <option value="">Selecione o caixa</option>
                        {caixas.map(c => (
                          <option key={c.id} value={c.id}>
                            {c.nome}
                          </option>
                        ))}
                      </Form.Select>
                      <Form.Control.Feedback type="invalid">{errors.caixaId}</Form.Control.Feedback>
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3" controlId="valor">
                      <Form.Label>Valor *</Form.Label>
                      <Form.Control
                        type="number"
                        name="valor"
                        step="0.01"
                        min="0"
                        value={formData.valor}
                        onChange={handleChange}
                        isInvalid={!!errors.valor}
                        isValid={formData.valor && !errors.valor}
                        placeholder="Valor da doação"
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
                    <Form.Group className="mb-3" controlId="cnpj">
                    <Form.Label>CNPJ *</Form.Label>
                    <Form.Control
                        type="text"
                        name="cnpj"
                        value={formData.cnpj}
                        onChange={handleChange}
                        isInvalid={!!errors.cnpj}
                        isValid={formData.cnpj && !errors.cnpj}
                        placeholder="CNPJ da instituição"
                    />
                    <Form.Control.Feedback type="invalid">{errors.cnpj}</Form.Control.Feedback>
                    </Form.Group>
                    </Col>
                </Row>

                <Link to="/doacao/instituicao">
                  <Button variant="secondary" className="me-2">Cancelar</Button>
                </Link>
                <Button variant="primary" type="submit">Salvar Alterações</Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default EditarDoacaoInstituicao;