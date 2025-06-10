import { useApoiadores, formatarData } from "hooks/useApoiador";
import React, { useState } from "react";
import { Button, Card, Col, Row, Table, Form } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Apoiadores = () => {
  const { apoiadores = [] } = useApoiadores(); 
  const token = localStorage.getItem("site");
  const [filtro, setFiltro] = useState("");

  // Função para exibir confirmação via toast
  const confirmarExclusao = (id) =>
    new Promise((resolve) => {
      const toastId = toast.info(
        <div>
          <p>Tem certeza que deseja excluir este apoiador?</p>
          <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
            <Button
              variant="secondary"
              size="sm"
              onClick={() => {
                toast.dismiss(toastId);
                resolve(false);
              }}
            >
              Cancelar
            </Button>
            <Button
              variant="danger"
              size="sm"
              onClick={() => {
                toast.dismiss(toastId);
                resolve(true);
              }}
            >
              Confirmar
            </Button>
          </div>
        </div>,
        {
          autoClose: false,
          closeOnClick: false,
          closeButton: false,
          draggable: false,
        }
      );
    });

  const handleDeleteApoiador = async (id) => {
    const confirmado = await confirmarExclusao(id);
    if (!confirmado) return;

    try {
      const response = await fetch(`http://localhost:8080/apoiador/excluir/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      });

      if (response.ok) {
        toast.success("Apoiador excluído com sucesso!");
        setTimeout(() => {
          window.location.reload();
        }, 1500);
      } else {
        toast.error("Falha ao excluir o apoiador.");
      }
    } catch (error) {
      console.error("Erro ao excluir apoiador:", error);
      toast.error("Erro ao excluir apoiador.");
    }
  };

  const apoiadoresFiltrados = apoiadores.filter((a) => {
    const filtroLower = filtro.toLowerCase();
    return (
      a.nome.toLowerCase().includes(filtroLower) ||
      a.cpf.toLowerCase().includes(filtroLower)
    );
  });

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
                <Card.Title as="h5">Apoiadores</Card.Title>
                <Link to="/apoiador/novo">
                  <Button variant="primary">
                    <IoPersonAdd /> Novo
                  </Button>
                </Link>
              </div>
            </Card.Header>
            <Card.Body>
              <ToastContainer
                position="top-right"
                autoClose={3000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                pauseOnFocusLoss
                draggable
                pauseOnHover
              />

              <Form.Control
                type="text"
                placeholder="Buscar por nome ou CPF..."
                value={filtro}
                onChange={(e) => setFiltro(e.target.value)}
                className="mb-3"
              />

              <Table responsive hover>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>Email</th>
                    <th>Fone</th>
                    <th>Sexo</th>
                    <th>Data Nasc.</th>
                    <th>Endereço</th>
                    {/* Coluna Grupo removida */}
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {apoiadoresFiltrados.length > 0 ? (
                    apoiadoresFiltrados.map((apoiador) => (
                      <tr key={apoiador.id}>
                        <th scope="row">{apoiador.id}</th>
                        <td>{apoiador.nome}</td>
                        <td>{apoiador.cpf}</td>
                        <td>{apoiador.email}</td>
                        <td>{apoiador.fone}</td>
                        <td>{apoiador.sexo}</td>
                        <td>{formatarData(apoiador.dataNasc)}</td>
                        <td>{apoiador.endereco}</td>
                        {/* Removida a célula do grupo */}
                        <td>
                          <Link to={`/apoiador/editar/${apoiador.id}`}>
                            <Button
                              size="sm"
                              className="label theme-bg text-white f-12"
                            >
                              <FaEdit />
                            </Button>
                          </Link>
                          <Button
                            size="sm"
                            className="label theme-bg2 text-white f-12"
                            onClick={() => handleDeleteApoiador(apoiador.id)}
                          >
                            <FaRegTrashAlt />
                          </Button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="9" className="text-center">
                        Nenhum apoiador encontrado.
                      </td>
                    </tr>
                  )}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default Apoiadores;
