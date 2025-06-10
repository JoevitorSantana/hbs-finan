import React, { useState } from "react";
import { Link } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";
import { useFuncionarios } from "hooks/useFuncionarios";
import { Button, Card, Col, Row, Table, Modal, Form } from "react-bootstrap";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Funcionarios = () => {
  const { funcionarios } = useFuncionarios();
  const token = localStorage.getItem("site");

  const [showModal, setShowModal] = useState(false);
  const [funcionarioParaExcluir, setFuncionarioParaExcluir] = useState(null);

  const [filtroNome, setFiltroNome] = useState("");
  const [filtroCpf, setFiltroCpf] = useState("");

  const handleAbrirModalExcluir = (funcionario) => {
    setFuncionarioParaExcluir(funcionario);
    setShowModal(true);
  };

  const handleFecharModal = () => {
    setShowModal(false);
    setFuncionarioParaExcluir(null);
  };

  const handleConfirmarExcluir = async () => {
    if (!funcionarioParaExcluir) return;

    try {
      const response = await fetch(
        `http://localhost:8080/funcionarios/excluir/${funcionarioParaExcluir.id}`,
        {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        }
      );

      if (response.ok) {
        toast.success("Funcionário excluído com sucesso!");
        setShowModal(false);
        setTimeout(() => {
          window.location.reload();
        }, 1500);
      } else {
        const data = await response.json();
        toast.error(data.message || "Erro ao excluir funcionário.");
      }
    } catch (error) {
      console.error("Erro ao excluir funcionário:", error);
      toast.error("Erro na comunicação com o servidor.");
    }
  };

  return (
    <>
      <Row>
        <Col sm={12}>
          <Card>
            <Card.Header>
              <div
                style={{
                  width: "100%",
                  display: "flex",
                  justifyContent: "space-between",
                }}
              >
                <Card.Title as="h5">Funcionários</Card.Title>
                <Link to="/funcionarios/novo">
                  <Button variant="primary">
                    <IoPersonAdd /> Novo
                  </Button>
                </Link>
              </div>
            </Card.Header>
            <Card.Body>
              <Row className="mb-3">
                <Col md={6}>
                  <Form.Control
                    type="text"
                    placeholder="Buscar por nome..."
                    value={filtroNome}
                    onChange={(e) => setFiltroNome(e.target.value)}
                  />
                </Col>
                <Col md={6}>
                  <Form.Control
                    type="text"
                    placeholder="Buscar por CPF..."
                    value={filtroCpf}
                    onChange={(e) => setFiltroCpf(e.target.value)}
                  />
                </Col>
              </Row>

              <Table responsive hover>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Nome</th>
                    <th>Email</th>
                    <th>Telefone</th>
                    <th>Endereço</th>
                    <th>Data Nascimento</th>
                    <th>Sexo</th>
                    <th>CPF</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
  {funcionarios &&
    funcionarios
      .filter((func) => {
        const nomeValido = func.nome?.toLowerCase().includes(filtroNome.toLowerCase());
        const cpfFunc = func.cpf ?? "";
        const cpfFiltro = filtroCpf ?? "";
        const cpfValido = cpfFunc.replace(/\D/g, "").includes(cpfFiltro.replace(/\D/g, ""));
        return nomeValido && cpfValido;
      })
      .map((funcionario) => (
        <tr key={funcionario.id}>
          <th scope="row">{funcionario.id}</th>
          <td>{funcionario.nome}</td>
          <td>{funcionario.email}</td>
          <td>{funcionario.fone}</td>
          <td>{funcionario.endereco}</td>
          <td>
            {funcionario.dataNascimento
              ? new Date(funcionario.dataNascimento).toLocaleDateString("pt-BR")
              : ""}
          </td>
          <td>{funcionario.sexo}</td>
          <td>{funcionario.cpf}</td>
          <td>
            <Link to={`/funcionarios/editar/${funcionario.id}`}>
              <Button size="sm" className="label theme-bg text-white f-12">
                <FaEdit />
              </Button>
            </Link>
            <Button
              size="sm"
              className="label theme-bg2 text-white f-12"
              onClick={() => handleAbrirModalExcluir(funcionario)}
            >
              <FaRegTrashAlt />
            </Button>
          </td>
        </tr>
      ))}
</tbody>

              </Table>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Modal show={showModal} onHide={handleFecharModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Confirmar Exclusão</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {funcionarioParaExcluir && (
            <p>
              Tem certeza que deseja excluir o funcionário{" "}
              <strong>{funcionarioParaExcluir.nome}</strong>?
            </p>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleFecharModal}>
            Cancelar
          </Button>
          <Button variant="danger" onClick={handleConfirmarExcluir}>
            Excluir
          </Button>
        </Modal.Footer>
      </Modal>

      <ToastContainer position="top-right" autoClose={3000} />
    </>
  );
};

export default Funcionarios;