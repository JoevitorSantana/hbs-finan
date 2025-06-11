import React, { useState, useEffect } from "react";
import { Button, Card, Col, Form, Row, Spinner } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const NovaDoacaoProduto = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("site");

  const [produtosDisponiveis, setProdutosDisponiveis] = useState([]);
  const [funcionarios, setFuncionarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erroServidor, setErroServidor] = useState("");

  const [formData, setFormData] = useState({
    funcionarioId: "",
    tipoDoacao: "",
    observacao: "",
    produtos: [{ produtoId: "", quantidade: "", tipoMovimentacao: "" }],
  });

  const [errors, setErrors] = useState({
    funcionarioId: false,
    tipoDoacao: false,
    produtos: [],
  });

  useEffect(() => {
    const fetchDados = async () => {
      if (!token) {
        setErroServidor("Usuário não autenticado.");
        setLoading(false);
        return;
      }
      try {
        const [resProdutos, resFuncionarios] = await Promise.all([
          fetch("http://localhost:8080/produtos/com-data-validade", {
            headers: { Authorization: "Bearer " + token },
          }),
          fetch("http://localhost:8080/funcionarios/listar", {
            headers: { Authorization: "Bearer " + token },
          }),
        ]);

        if (!resProdutos.ok || !resFuncionarios.ok) {
          throw new Error("Erro ao buscar dados.");
        }

        const produtosData = await resProdutos.json();
        const funcionariosData = await resFuncionarios.json();

        setProdutosDisponiveis(produtosData);
        setFuncionarios(funcionariosData);
      } catch (error) {
        setErroServidor("Erro ao buscar dados: " + error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchDados();
  }, [token]);

  const tiposMovimentacao = [
    { value: "ENTRADA_DOACAO_ALIMENTICIA", label: "Entrada por Doação Alimentícia" },
    { value: "SAIDA_DOACAO_ALIMENTICIA", label: "Saída por Doação Alimentícia" },
    { value: "ENTRADA_DOACAO_MATERIAL", label: "Entrada por Doação de Material" },
    { value: "SAIDA_DOACAO_MATERIAL", label: "Saída por Doação de Material" },
    { value: "ENTRADA_COMPRA", label: "Entrada por Compra" },
    { value: "AJUSTE_ENTRADA", label: "Ajuste de Inventário (Entrada)" },
    { value: "AJUSTE_SAIDA", label: "Ajuste de Inventário (Saída)" },
    { value: "SAIDA_PERDA_DANO", label: "Saída por Perda ou Dano" },
    { value: "ENTRADA_DEVOLUCAO", label: "Entrada por Devolução" },
    { value: "SAIDA_VENDA", label: "Saída por Venda" },
  ];

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: false }));
  };

  const handleProdutoChange = (index, field, value) => {
    const produtos = [...formData.produtos];
    produtos[index][field] = value;
    setFormData({ ...formData, produtos });

    if (errors.produtos[index]?.[field]) {
      const novosErrosProdutos = [...errors.produtos];
      novosErrosProdutos[index][field] = false;
      setErrors((prev) => ({ ...prev, produtos: novosErrosProdutos }));
    }
  };

  const adicionarProduto = () => {
    setFormData((prev) => ({
      ...prev,
      produtos: [...prev.produtos, { produtoId: "", quantidade: "", tipoMovimentacao: "" }],
    }));
    setErrors((prev) => ({
      ...prev,
      produtos: [...prev.produtos, { produtoId: false, quantidade: false, tipoMovimentacao: false }],
    }));
  };

  const removerProduto = (index) => {
    const novosProdutos = formData.produtos.filter((_, i) => i !== index);
    const novosErros = errors.produtos.filter((_, i) => i !== index);
    setFormData({ ...formData, produtos: novosProdutos });
    setErrors({ ...errors, produtos: novosErros });
  };

  const validarFormulario = () => {
    let valido = true;

    if (!formData.funcionarioId) {
      setErrors((prev) => ({ ...prev, funcionarioId: true }));
      valido = false;
    }

    if (!formData.tipoDoacao) {
      setErrors((prev) => ({ ...prev, tipoDoacao: true }));
      valido = false;
    }

    const novosErrosProdutos = formData.produtos.map((p) => ({
      produtoId: !p.produtoId,
      quantidade: !p.quantidade || Number(p.quantidade) <= 0,
      tipoMovimentacao: !p.tipoMovimentacao,
    }));

    if (novosErrosProdutos.some((e) => e.produtoId || e.quantidade || e.tipoMovimentacao)) {
      setErrors((prev) => ({ ...prev, produtos: novosErrosProdutos }));
      valido = false;
    }

    return valido;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validarFormulario()) {
      toast.error("Por favor, preencha todos os campos obrigatórios corretamente.");
      return;
    }

    const dataToSend = {
      funcionarioId: Number(formData.funcionarioId),
      tipoDoacao: formData.tipoDoacao,
      observacao: formData.observacao.trim() || null,
      movimentacoes: formData.produtos.map((p) => ({
        produtoId: Number(p.produtoId),
        quantidadeMovimentada: Number(p.quantidade),
        tipo: p.tipoMovimentacao,
      })),
    };

    try {
      const response = await fetch("http://localhost:8080/api/estoque/movimentar", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
        body: JSON.stringify(dataToSend),
      });

      if (response.ok) {
        toast.success("Doação cadastrada com sucesso!");
        setTimeout(() => navigate("/doacao"), 1500);
      } else {
        const errorData = await response.json();
        setErroServidor(errorData.message || "Erro desconhecido.");
      }
    } catch (error) {
      setErroServidor("Erro na comunicação com o servidor.");
    }
  };

  const bordaErro = { borderColor: "red", boxShadow: "0 0 5px red" };

  const LabelObrigatorio = ({ children }) => (
    <Form.Label>
      {children} <span style={{ color: "red" }}>*</span>
    </Form.Label>
  );

  return (
    <Row>
      <Col sm={12}>
        <Card>
          <Card.Header>
            <Card.Title as="h5">Nova Doação de Produto</Card.Title>
          </Card.Header>
          <Card.Body>
            <ToastContainer position="top-right" autoClose={3000} />
            {loading ? (
              <div className="d-flex justify-content-center align-items-center" style={{ height: "150px" }}>
                <Spinner animation="border" variant="primary" />
              </div>
            ) : erroServidor ? (
              <p className="text-danger">{erroServidor}</p>
            ) : (
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="funcionarioId">
                  <LabelObrigatorio>Funcionário</LabelObrigatorio>
                  <Form.Select
                    name="funcionarioId"
                    value={formData.funcionarioId}
                    onChange={handleChange}
                    style={errors.funcionarioId ? bordaErro : {}}
                    isInvalid={errors.funcionarioId}
                  >
                    <option value="">-- Selecione --</option>
                    {funcionarios.map((f) => (
                      <option key={f.id} value={f.id}>
                        {f.nome} (ID: {f.id})
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>

                <Form.Group className="mb-3" controlId="tipoDoacao">
                  <LabelObrigatorio>Tipo da Doação</LabelObrigatorio>
                  <Form.Select
                    name="tipoDoacao"
                    value={formData.tipoDoacao}
                    onChange={handleChange}
                    style={errors.tipoDoacao ? bordaErro : {}}
                    isInvalid={errors.tipoDoacao}
                  >
                    <option value="">-- Selecione --</option>
                    {tiposMovimentacao.map((tipo) => (
                      <option key={tipo.value} value={tipo.value}>
                        {tipo.label}
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>

                <Form.Group className="mb-3" controlId="observacao">
                  <Form.Label>Observação</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    name="observacao"
                    value={formData.observacao}
                    onChange={handleChange}
                    placeholder="Digite uma observação (opcional)"
                  />
                </Form.Group>

                {formData.produtos.map((produto, index) => (
                  <div key={index} className="border rounded p-3 mb-3">
                    <Row>
                      <Col md={4}>
                        <Form.Group>
                          <LabelObrigatorio>Produto</LabelObrigatorio>
                          <Form.Select
                            value={produto.produtoId}
                            onChange={(e) => handleProdutoChange(index, "produtoId", e.target.value)}
                            style={errors.produtos[index]?.produtoId ? bordaErro : {}}
                            isInvalid={errors.produtos[index]?.produtoId}
                          >
                            <option value="">-- Selecione --</option>
                            {produtosDisponiveis.map((p) => (
                              <option key={p.id} value={p.id}>
                                {p.nome}
                              </option>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </Col>
                      <Col md={2}>
                        <Form.Group>
                          <LabelObrigatorio>Quantidade</LabelObrigatorio>
                          <Form.Control
                            type="number"
                            min="1"
                            value={produto.quantidade}
                            onChange={(e) => handleProdutoChange(index, "quantidade", e.target.value)}
                            style={errors.produtos[index]?.quantidade ? bordaErro : {}}
                            isInvalid={errors.produtos[index]?.quantidade}
                          />
                        </Form.Group>
                      </Col>
                      <Col md={4}>
                        <Form.Group>
                          <LabelObrigatorio>Tipo de Movimentação</LabelObrigatorio>
                          <Form.Select
                            value={produto.tipoMovimentacao}
                            onChange={(e) => handleProdutoChange(index, "tipoMovimentacao", e.target.value)}
                            style={errors.produtos[index]?.tipoMovimentacao ? bordaErro : {}}
                            isInvalid={errors.produtos[index]?.tipoMovimentacao}
                          >
                            <option value="">-- Selecione --</option>
                            {tiposMovimentacao.map((tipo) => (
                              <option key={tipo.value} value={tipo.value}>
                                {tipo.label}
                              </option>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </Col>
                      <Col md={2} className="d-flex align-items-end">
                        {formData.produtos.length > 1 && (
                          <Button variant="danger" onClick={() => removerProduto(index)}>
                            Remover
                          </Button>
                        )}
                      </Col>
                    </Row>
                  </div>
                ))}

                <Button variant="secondary" onClick={adicionarProduto}>
                  Adicionar Produto
                </Button>

                <div className="mt-4 d-flex justify-content-end">
                  <Button variant="primary" type="submit">
                    Salvar
                  </Button>
                </div>
              </Form>
            )}
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};

export default NovaDoacaoProduto;
