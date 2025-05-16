import { useParametros } from "hooks/useParametros";
import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row, Spinner, Alert } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

/**
 * Formulário de edição da parametrização da empresa.
 * Carrega os dados via `useParametros`, permite editar e envia PUT /api/parametrizacao.
 */
const EditarParametrizacao = () => {
  const { parametros, loading, error } = useParametros();
  const token = localStorage.getItem("site");
  const navigate = useNavigate();

  // Mantemos o estado plano para coincidir com os nomes do back‑end
  const [formData, setFormData] = useState({
    id: "",
    nomeEmpresa: "",
    razaoSocial: "",
    enderecoRua: "",
    enderecoBairro: "",
    enderecoCidade: "",
    enderecoCep: "",
    enderecoEstado: "",
    email: "",
    telefone: "",
    celular: "",
    nomeProprietario: "",
    cnpj: "",
    logoPequenaUrl: "",
    logoGrandeUrl: "",
  });

  // Pré‑enche quando os dados chegam
  useEffect(() => {
    if (parametros) {
      setFormData({
        id: parametros.id ?? "",
        nomeEmpresa: parametros.nomeEmpresa ?? "",
        razaoSocial: parametros.razaoSocial ?? "",
        enderecoRua: parametros.enderecoRua ?? "",
        enderecoBairro: parametros.enderecoBairro ?? "",
        enderecoCidade: parametros.enderecoCidade ?? "",
        enderecoCep: parametros.enderecoCep ?? "",
        enderecoEstado: parametros.enderecoEstado ?? "",
        email: parametros.email ?? "",
        telefone: parametros.telefone ?? "",
        celular: parametros.celular ?? "",
        nomeProprietario: parametros.nomeProprietario ?? "",
        cnpj: parametros.cnpj ?? "",
        logoPequenaUrl: parametros.logoPequenaUrl ?? "",
        logoGrandeUrl: parametros.logoGrandeUrl ?? "",
      });
    }
  }, [parametros]);

  // Atualiza qualquer campo
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // Envia o PUT
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      console.log("Payload sendo enviado:", formData);

      const response = await fetch("http://localhost:8080/api/parametrizacao", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        alert("Parametrização atualizada com sucesso!");
        navigate("/parametrizacao");
      } else if (response.status === 401) {
        alert("Sessão expirada. Faça login novamente.");
        localStorage.removeItem("site");
        navigate("/login");
      } else {
        const text = await response.text();
        alert(`Erro (${response.status}): ${text || "desconhecido"}`);
      }
    } catch (err) {
      console.error("Erro ao atualizar parametrização:", err);
      alert("Erro na comunicação com o servidor.");
    }
  };

  // Loading e erros
  if (loading) {
    return (
      <div className="d-flex justify-content-center mt-5">
        <Spinner animation="border" />
      </div>
    );
  }
  if (error) {
    return (
      <Alert variant="danger" className="mt-3">
        Falha ao carregar parametrização: {error.message}
      </Alert>
    );
  }

  return (
    <Row>
      <Col sm={12}>
        <Card>
          <Card.Header>
            <Card.Title as="h5">Editar Parametrização</Card.Title>
          </Card.Header>
          <Card.Body>
            <Form onSubmit={handleSubmit}>
              <Row>
                {/* Coluna 1 */}
                <Col md={6}>
                  {[
                    { id: "nomeEmpresa", label: "Nome da Empresa" },
                    { id: "razaoSocial", label: "Razão Social" },
                    { id: "email", label: "Email", type: "email" },
                    { id: "telefone", label: "Telefone" },
                    { id: "celular", label: "Celular" },
                    { id: "nomeProprietario", label: "Nome do Proprietário" },
                    { id: "cnpj", label: "CNPJ" },
                  ].map(({ id, label, type }) => (
                    <Form.Group className="mb-3" controlId={id} key={id}>
                      <Form.Label>{label}</Form.Label>
                      <Form.Control
                        type={type || "text"}
                        name={id}
                        value={formData[id]}
                        onChange={handleChange}
                      />
                    </Form.Group>
                  ))}
                </Col>

                {/* Coluna 2 */}
                <Col md={6}>
                  {[
                    { id: "enderecoRua", label: "Rua" },
                    { id: "enderecoBairro", label: "Bairro" },
                    { id: "enderecoCidade", label: "Cidade" },
                    { id: "enderecoEstado", label: "Estado" },
                    { id: "enderecoCep", label: "CEP" },
                    { id: "logoPequenaUrl", label: "Logo Pequena (URL)" },
                    { id: "logoGrandeUrl", label: "Logo Grande (URL)" },
                  ].map(({ id, label }) => (
                    <Form.Group className="mb-3" controlId={id} key={id}>
                      <Form.Label>{label}</Form.Label>
                      <Form.Control
                        name={id}
                        value={formData[id]}
                        onChange={handleChange}
                      />
                    </Form.Group>
                  ))}
                </Col>
              </Row>

              <div className="d-flex justify-content-end gap-2">
                <Link to="/parametrizacao">
                  <Button variant="secondary">Cancelar</Button>
                </Link>
                <Button variant="primary" type="submit">
                  Salvar
                </Button>
              </div>
            </Form>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};

export default EditarParametrizacao;
