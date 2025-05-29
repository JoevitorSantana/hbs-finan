import React, { useState } from "react";
import { Link } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";
import { Button, Card, Col, Row, Table, Modal, Form } from "react-bootstrap";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useDespesas } from "hooks/useDespesas";

const Despesas = () => {
  const { despesas } = useDespesas();
  const token = localStorage.getItem("site");

  const [showModal, setShowModal] = useState(false);
  const [despesaParaExcluir, setDespesaParaExcluir] = useState(null);
  const [filtroDescricao, setFiltroDescricao] = useState("");

  const handleAbrirModalExcluir = (despesa) => {
    setDespesaParaExcluir(despesa);
    setShowModal(true);
  };

  const handleFecharModal = () => {
    setShowModal(false);
    setDespesaParaExcluir(null);
  };

  const handleConfirmarExcluir = async () => {
    if (!despesaParaExcluir) return;

    try {
      const response = await fetch(
        `http://localhost:8080/despesas/excluir/${despesaParaExcluir.id}`,
        {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        }
      );

      if (response.ok) {
        toast.success("Despesa excluída com sucesso!");
        setShowModal(false);
        setTimeout(() => {
          window.location.reload();
        }, 1500);
      } else {
        const data = await response.json();
        toast.error(data.message || "Erro ao excluir despesa.");
      }
    } catch (error) {
      console.error("Erro ao excluir despesa:", error);
      toast.error("Erro na comunicação com o servidor.");
    }
  };

  const formatarData = (data) =>
    data ? new Date(data).toLocaleDateString("pt-BR") : "";

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
                <Card.Title as="h5">Despesas</Card.Title>
                <Link to="/despesas/novo">
                  <Button variant="primary">
                    <IoPersonAdd /> Nova Despesa
                  </Button>
                </Link>
              </div>
            </Card.Header>
            <Card.Body>
              <Row className="mb-3">
                <Col md={6}>
                  <Form.Control
                    type="text"
                    placeholder="Buscar por descrição..."
                    value={filtroDescricao}
                    onChange={(e) => setFiltroDescricao(e.target.value)}
                  />
                </Col>
              </Row>

              <Table responsive hover>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Data Lançamento</th>
                    <th>Data Vencimento</th>
                    <th>Descrição</th>
                    <th>Pagamento Total</th>
                    <th>Valor</th>
                    <th>Data Quitação</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {despesas &&
                    despesas
                      .filter((d) =>
                        d.descricao.toLowerCase().includes(filtroDescricao.toLowerCase())
                      )
                      .map((despesa) => (
                        <tr key={despesa.id}>
                          <td>{despesa.id}</td>
                          <td>{formatarData(despesa.dataLancamento)}</td>
                          <td>{formatarData(despesa.dataVencimento)}</td>
                          <td>{despesa.descricao}</td>
                          <td>{despesa.pagamentoTotal}</td>
                          <td>{despesa.valor}</td>
                          <td>{formatarData(despesa.dataQuitacao)}</td>
                          <td>
                            <Link to={`/despesas/editar/${despesa.id}`}>
                              <Button
                                size="sm"
                                className="me-2"
                                variant="warning"
                              >
                                <FaEdit />
                              </Button>
                            </Link>
                            <Button
                              size="sm"
                              variant="danger"
                              onClick={() => handleAbrirModalExcluir(despesa)}
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
          {despesaParaExcluir && (
            <p>
              Tem certeza que deseja excluir a despesa:{" "}
              <strong>{despesaParaExcluir.descricao}</strong>?
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

export default Despesas;
