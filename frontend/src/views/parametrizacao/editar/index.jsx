import { useParametros } from "hooks/useParametros";
import React, { useEffect, useState } from "react";
import { Button, Card, Col, Form, Row, Spinner, Alert } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

// ... [as funções auxiliares continuam iguais]

function formatCNPJ(value) {
  value = value.replace(/\D/g, "");
  return value.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2}).*/, "$1.$2.$3/$4-$5");
}
function formatTelefone(value) {
  value = value.replace(/\D/g, "");
  if (value.length === 10)
    return value.replace(/^(\d{2})(\d{4})(\d{4}).*/, "($1) $2-$3");
  if (value.length === 11)
    return value.replace(/^(\d{2})(\d{5})(\d{4}).*/, "($1) $2-$3");
  return value;
}
function formatCelular(value) {
  value = value.replace(/\D/g, "");
  return value.replace(/^(\d{2})(\d{5})(\d{4}).*/, "($1) $2-$3");
}
function formatCEP(value) {
  value = value.replace(/\D/g, "");
  return value.replace(/^(\d{5})(\d{3}).*/, "$1-$2");
}

function isValidCNPJ(value) {
  value = value.replace(/\D/g, "");
  if (value.length !== 14) return false;
  if (/^(\d)\1{13}$/.test(value)) return false;
  const digits = value.split("").map((d) => +d);
  const calc = (slice, weights) => {
    const sum = slice.reduce((acc, num, idx) => acc + num * weights[idx], 0);
    const r = sum % 11;
    return r < 2 ? 0 : 11 - r;
  };
  const weights1 = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
  const weights2 = [6].concat(weights1);
  const d1 = calc(digits.slice(0, 12), weights1);
  const d2 = calc(digits.slice(0, 12).concat(d1), weights2);
  return d1 === digits[12] && d2 === digits[13];
}

const EditarParametrizacao = () => {
  //const { parametros, c } = useParametros();
  const { parametros, loading, error } = useParametros();
  const token = localStorage.getItem("site");
  const navigate = useNavigate();

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

  const requiredFields = [
    "nomeEmpresa", "razaoSocial", "email", "telefone", "celular", "nomeProprietario", "cnpj",
    "enderecoRua", "enderecoBairro", "enderecoCidade", "enderecoEstado", "enderecoCep",
    "logoPequenaUrl", "logoGrandeUrl",
  ];

  const [errors, setErrors] = useState({});
  const examplePlaceholders = {
    telefone: "Exemplo: (00) 0000-0000",
    celular: "Exemplo: (00) 00000-0000",
    cnpj: "Exemplo: 00.000.000/0000-00",
    enderecoCep: "Exemplo: 00000-000",
  };

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
      setErrors({});
    }
  }, [parametros]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    let formattedValue = value;

    if (name === "telefone") formattedValue = formatTelefone(value);
    else if (name === "celular") formattedValue = formatCelular(value);
    else if (name === "cnpj") formattedValue = formatCNPJ(value);
    else if (name === "enderecoCep") formattedValue = formatCEP(value);

    setFormData((prev) => ({ ...prev, [name]: formattedValue }));

    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: false }));
    }
  };


  const handleSubmit = async (e) => {
    e.preventDefault();
    const newErrors = {};

    requiredFields.forEach((field) => {
      if (!formData[field] || formData[field].toString().trim() === "") {
        newErrors[field] = true;
      }
    });

    if (!newErrors.cnpj && !isValidCNPJ(formData.cnpj)) {
      newErrors.cnpj = true;
      toast.error("CNPJ inválido. Verifique os dígitos verificadores.");
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      toast.error("Preencha todos os campos obrigatórios corretamente!");
      return;
    }

    // Formata os campos antes de enviar
    const payload = {
      ...formData,
      cnpj: formatCNPJ(formData.cnpj),
      telefone: formatTelefone(formData.telefone),
      celular: formatCelular(formData.celular),
      enderecoCep: formatCEP(formData.enderecoCep),
    };

    try {
      const response = await fetch("http://localhost:8080/parametrizacao/editar", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        toast.success("Parametrização atualizada com sucesso!");
        setTimeout(() => navigate("/parametrizacao"), 1800);
      } else if (response.status === 401) {
        toast.error("Sessão expirada. Faça login novamente.");
        localStorage.removeItem("site");
        setTimeout(() => navigate("/login"), 1500);
      } else {
        const text = await response.text();
        toast.error(`Erro (${response.status}): ${text || "desconhecido"}`);
      }
    } catch (err) {
      console.error("Erro ao atualizar parametrização:", err);
      toast.error("Erro na comunicação com o servidor.");
    }
  };

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

  const column1Fields = [
    { id: "nomeEmpresa", label: "Nome da Empresa", type: "text" },
    { id: "razaoSocial", label: "Razão Social", type: "text" },
    { id: "email", label: "Email", type: "email" },
    { id: "telefone", label: "Telefone", type: "text" },
    { id: "celular", label: "Celular", type: "text" },
    { id: "nomeProprietario", label: "Nome do Proprietário", type: "text" },
    { id: "cnpj", label: "CNPJ", type: "text" },
  ];

  const column2Fields = [
    { id: "enderecoRua", label: "Rua" },
    { id: "enderecoBairro", label: "Bairro" },
    { id: "enderecoCidade", label: "Cidade" },
    { id: "enderecoEstado", label: "Estado" },
    { id: "enderecoCep", label: "CEP" },
    { id: "logoPequenaUrl", label: "Logo Pequena (URL)" },
    { id: "logoGrandeUrl", label: "Logo Grande (URL)" },
  ];

  return (
    <Row>
      <ToastContainer position="top-right" autoClose={2500} />
      <Col sm={12}>
        <Card>
          <Card.Header>
            <Card.Title as="h5">Editar Parametrização</Card.Title>
          </Card.Header>
          <Card.Body>
            <Form noValidate onSubmit={handleSubmit}>
              <Row>
                {/* Coluna 1 */}
                <Col md={6}>
                  {column1Fields.map(({ id, label, type }) => (
                    <Form.Group className="mb-3" controlId={id} key={id}>
                      <Form.Label>
                        {label} <span className="text-danger">*</span>
                      </Form.Label>
                      <Form.Control
                        type={type}
                        name={id}
                        value={formData[id]}
                        onChange={handleChange}
                        isInvalid={errors[id]}
                        required
                        placeholder={
                          formData[id] === "" && examplePlaceholders[id]
                            ? examplePlaceholders[id]
                            : ""
                        }
                      />
                      <Form.Control.Feedback type="invalid">
                        {id === "cnpj" && errors[id]
                          ? "CNPJ inválido"
                          : "Este campo é obrigatório."}
                      </Form.Control.Feedback>
                    </Form.Group>
                  ))}
                </Col>

                {/* Coluna 2 */}
                <Col md={6}>
                  {column2Fields.map(({ id, label }) => (
                    <Form.Group className="mb-3" controlId={id} key={id}>
                      <Form.Label>
                        {label} <span className="text-danger">*</span>
                      </Form.Label>
                      <Form.Control
                        name={id}
                        value={formData[id]}
                        onChange={handleChange}
                        isInvalid={errors[id]}
                        required
                        placeholder={
                          formData[id] === "" && examplePlaceholders[id]
                            ? examplePlaceholders[id]
                            : ""
                        }
                      />
                      <Form.Control.Feedback type="invalid">
                        "Este campo é obrigatório."
                      </Form.Control.Feedback>
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
