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
    movimentacaoTipo: "",
    observacao: "", // campo observacao adicionado
    produtos: [{ produtoId: "", quantidade: "" }],
  });

  const initialProdutoErrors = { produtoId: false, quantidade: false };

  const [errors, setErrors] = useState({
    funcionarioId: false,
    movimentacaoTipo: false,
    produtos: [initialProdutoErrors],
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

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: false }));
  };

  const handleProdutoChange = (index, field, value) => {
    const produtos = [...formData.produtos];
    produtos[index][field] = value;
    setFormData({ ...formData, produtos });

    if (
      errors.produtos &&
      errors.produtos[index] &&
      errors.produtos[index][field]
    ) {
      const novosErrosProdutos = [...errors.produtos];
      novosErrosProdutos[index][field] = false;
      setErrors((prev) => ({ ...prev, produtos: novosErrosProdutos }));
    }
  };

  const adicionarProduto = () => {
    setFormData((prev) => ({
      ...prev,
      produtos: [...prev.produtos, { produtoId: "", quantidade: "" }],
    }));
    setErrors((prev) => ({
      ...prev,
      produtos: [...(prev.produtos || []), initialProdutoErrors],
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

    if (!formData.movimentacaoTipo) {
      setErrors((prev) => ({ ...prev, movimentacaoTipo: true }));
      valido = false;
    }

    const novosErrosProdutos = formData.produtos.map((p) => ({
      produtoId: !p.produtoId,
      quantidade: !p.quantidade || Number(p.quantidade) <= 0,
    }));

    if (novosErrosProdutos.some((e) => e.produtoId || e.quantidade)) {
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

    const dataHoje = new Date().toISOString().slice(0, 10);

    const itens = formData.produtos.map((p) => {
      const produtoCompleto = produtosDisponiveis.find(
        (prod) => prod.id === Number(p.produtoId)
      );

      return {
        produto: { id: Number(p.produtoId) },
        quantidade: Number(p.quantidade),
        dataValidade: produtoCompleto ? produtoCompleto.dataValidade : null,
      };
    });

    const dataToSend = {
      funcionario: { id: Number(formData.funcionarioId) },
      tipoMovimentacao: formData.movimentacaoTipo,
      data: dataHoje,
      observacao: formData.observacao,  // enviado para a API aqui
      itens,
    };

    console.log("Dados enviados para o backend:", JSON.stringify(dataToSend, null, 2));

    try {
      const response = await fetch("http://localhost:8080/api/doacoes", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
        body: JSON.stringify(dataToSend),
      });

      if (response.ok) {
        toast.success("Doação cadastrada com sucesso!");

        // Atualiza o estoque
        for (const item of itens) {
          const estoqueData = {
            produtoId: item.produto.id,
            tipo: "DECREMENTAR",
            quantidade: item.quantidade,
          };

          try {
            const estoqueResponse = await fetch("http://localhost:8080/estoque/atualizar", {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token,
              },
              body: JSON.stringify(estoqueData),
            });

            if (!estoqueResponse.ok) {
              console.error(`Erro ao atualizar o estoque do produto ID ${item.produto.id}`);
            }
          } catch (estoqueError) {
            console.error("Erro na comunicação com a API de estoque:", estoqueError);
          }
        }

        setTimeout(() => navigate("/doacao/alimenticia"), 1500);
      } else {
        const errorData = await response.json();
        setErroServidor(errorData.message || "Erro desconhecido ao salvar doação.");
      }
    } catch (error) {
      console.error("Erro geral:", error);
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
              <div
                className="d-flex justify-content-center align-items-center"
                style={{ height: "150px" }}
              >
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

                <Form.Group className="mb-3" controlId="movimentacaoTipo">
                  <LabelObrigatorio>Tipo de Movimento</LabelObrigatorio>
                  <Form.Select
                    name="movimentacaoTipo"
                    value={formData.movimentacaoTipo}
                    onChange={handleChange}
                    style={errors.movimentacaoTipo ? bordaErro : {}}
                    isInvalid={errors.movimentacaoTipo}
                  >
                    <option value="">-- Selecione --</option>
                    <option value="INCREMENTAR">Incrementar (Entrada)</option>
                    <option value="DECREMENTAR">Decrementar (Saída)</option>
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
                      <Col md={6}>
                        <Form.Group>
                          <LabelObrigatorio>Produto</LabelObrigatorio>
                          <Form.Select
                            value={produto.produtoId}
                            onChange={(e) =>
                              handleProdutoChange(index, "produtoId", e.target.value)
                            }
                            style={errors.produtos[index]?.produtoId ? bordaErro : {}}
                            isInvalid={errors.produtos[index]?.produtoId}
                          >
                            <option value="">-- Selecione --</option>
                            {produtosDisponiveis.map((p) => (
                              <option key={p.id} value={p.id}>
                                {p.nome} - Validade: {p.dataValidade}
                              </option>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </Col>
                      <Col md={4}>
                        <Form.Group>
                          <LabelObrigatorio>Quantidade</LabelObrigatorio>
                          <Form.Control
                            type="number"
                            min="1"
                            value={produto.quantidade}
                            onChange={(e) =>
                              handleProdutoChange(index, "quantidade", e.target.value)
                            }
                            style={errors.produtos[index]?.quantidade ? bordaErro : {}}
                            isInvalid={errors.produtos[index]?.quantidade}
                          />
                        </Form.Group>
                      </Col>
                      <Col md={2} className="d-flex align-items-end">
                        {formData.produtos.length > 1 && (
                          <Button
                            variant="danger"
                            onClick={() => removerProduto(index)}
                          >
                            Remover
                          </Button>
                        )}
                      </Col>
                    </Row>
                  </div>
                ))}

                <Button variant="secondary" onClick={adicionarProduto}>
                  + Adicionar Produto
                </Button>

                <div className="mt-4">
                  <Button type="submit" variant="primary">
                    Salvar Doação
                  </Button>
                  <Button
                    variant="outline-secondary"
                    className="ms-2"
                    onClick={() => navigate("/doacao/alimenticia")}
                  >
                    Cancelar
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
