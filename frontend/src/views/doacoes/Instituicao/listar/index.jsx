import React, { useState, useEffect } from "react";
import { Card, Table, Button, Form, Tabs, Tab, Modal } from "react-bootstrap";
import { ToastContainer, toast } from "react-toastify";
import { FaRegTrashAlt } from "react-icons/fa";

const ListarDoacoesInstituicoes = () => {
  const [tab, setTab] = useState("instituicao");
  const [buscaInstituicao, setBuscaInstituicao] = useState("");
  const [doacoes, setDoacoes] = useState([]);
  const [resultadoInstituicao, setResultadoInstituicao] = useState([]);

  const [showModal, setShowModal] = useState(false);
  const [doacaoSelecionada, setDoacaoSelecionada] = useState(null);

  const token = localStorage.getItem("site");

  useEffect(() => {
    console.log("Executando fetch das doações...");
    fetch("http://localhost:8080/doacao-instituicao/listar", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
      .then((res) => {
        console.log("Resposta do fetch:", res);
        return res.json();
      })
      .then((data) => {
        console.log("Dados recebidos (formato bruto):", data);

        // Supondo que o backend retorne um array diretamente ou um objeto com doacoes[]
        const lista = Array.isArray(data) ? data : data.doacoes || [];
        setDoacoes(lista);
      })
      .catch((err) => {
        console.error("Erro no fetch:", err);
        setDoacoes([]);
      });
  }, [token]);

  useEffect(() => {
    const busca = buscaInstituicao.trim().toLowerCase();
    const buscaLimpa = busca.replace(/\D/g, "");
    if (!busca) {
      setResultadoInstituicao(doacoes);
      return;
    }

    const filtrado = doacoes.filter((d) => {
      const nome = (d.nome || d.instituicaoNome || "").toLowerCase();
      const cnpj = (d.cnpj || d.instituicaoCnpj || "").replace(/\D/g, "");

      return (
        nome.includes(busca) ||
        (buscaLimpa.length > 0 && cnpj.includes(buscaLimpa))
      );
    });

    setResultadoInstituicao(filtrado);
  }, [buscaInstituicao, doacoes]);

  const handleShowModal = (doacao) => {
    setDoacaoSelecionada(doacao);
    setShowModal(true);
  };
  const handleCloseModal = () => {
    setShowModal(false);
    setDoacaoSelecionada(null);
  };

  const handleRemoverDoacao = async () => {
    if (!doacaoSelecionada) return;
    try {
      const response = await fetch(
        `http://localhost:8080/doacao-instituicao/excluir/${doacaoSelecionada.id}`,
        {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        }
      );
      if (response.ok) {
        toast.success("Doação removida com sucesso!");
        setDoacoes((prev) =>
          prev.filter((d) => d.id !== doacaoSelecionada.id)
        );
      } else {
        toast.error("Erro ao remover doação.");
      }
    } catch (error) {
      console.error("Erro ao remover doação:", error);
      toast.error("Erro ao remover doação.");
    } finally {
      handleCloseModal();
    }
  };

  return (
    <div>
      <ToastContainer position="top-right" autoClose={3000} />
      <Tabs activeKey={tab} onSelect={(k) => setTab(k)} className="mb-4" justify>
        <Tab eventKey="instituicao" title="Pesquisar por Instituição">
          <Card>
            <Card.Body>
              <Form.Group controlId="buscaInstituicao">
                <Form.Label>Buscar instituição (nome ou CNPJ):</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Ex: Lar Esperança ou 00.000.000/0001-00"
                  value={buscaInstituicao}
                  onChange={(e) => setBuscaInstituicao(e.target.value)}
                />
              </Form.Group>

              <Table responsive hover className="mt-4">
                <thead>
                  <tr>
                    <th>Instituição</th>
                    <th>CNPJ</th>
                    <th>Data</th>
                    <th>Valor</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {resultadoInstituicao.length === 0 ? (
                    <tr>
                      <td colSpan={5} className="text-center text-muted">
                        Nenhuma doação encontrada.
                      </td>
                    </tr>
                  ) : (
                    resultadoInstituicao.map((item) => (
                      <tr key={item.id}>
                        <td>{item.nome || item.instituicaoNome || "Sem nome"}</td>
                        <td>{item.cnpj || item.instituicaoCnpj || "Sem CNPJ"}</td>
                        <td>
                          {item.data
                            ? new Date(item.data).toLocaleDateString("pt-BR")
                            : "—"}
                        </td>
                        <td>
                          {typeof item.valor === "number"
                            ? item.valor.toLocaleString("pt-BR", {
                                style: "currency",
                                currency: "BRL",
                              })
                            : "—"}
                        </td>
                        <td>
                          <Button
                            variant="danger"
                            size="sm"
                            onClick={() => handleShowModal(item)}
                          >
                            <FaRegTrashAlt />
                          </Button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Tab>
      </Tabs>

      <Modal show={showModal} onHide={handleCloseModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Confirmação</Modal.Title>
        </Modal.Header>
        <Modal.Body>Tem certeza que deseja remover esta doação?</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseModal}>
            Cancelar
          </Button>
          <Button variant="danger" onClick={handleRemoverDoacao}>
            Remover
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default ListarDoacoesInstituicoes;