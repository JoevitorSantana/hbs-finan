import React, { useState, useEffect } from "react";
import { Card, Table, Button, Form, Row, Col, Modal, Tabs, Tab } from "react-bootstrap";
import { ToastContainer, toast } from "react-toastify";
import { FaRegTrashAlt } from "react-icons/fa";

const ListarDoacoesMonetarias = () => {
  const [tab, setTab] = useState("doador");
  const [buscaDoador, setBuscaDoador] = useState("");
  const [buscaCampanha, setBuscaCampanha] = useState("");

  const [doadores, setDoadores] = useState([]);
  const [campanhas, setCampanhas] = useState([]);
  const [doacoes, setDoacoes] = useState([]);

  // Resultados filtrados
  const [resultadoDoador, setResultadoDoador] = useState([]);
  const [resultadoCampanha, setResultadoCampanha] = useState([]);

  const [showModal, setShowModal] = useState(false);
  const [doacaoSelecionada, setDoacaoSelecionada] = useState(null);
  const [filtroTipo, setFiltroTipo] = useState(""); // "doador" ou "campanha"

  const token = localStorage.getItem("site");

  // Carregar dados iniciais
  useEffect(() => {
    fetch("http://localhost:8080/doador/listar", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
      .then((res) => res.json())
      .then((data) => setDoadores(Array.isArray(data) ? data : []))
      .catch(() => setDoadores([]));

    fetch("http://localhost:8080/campanhas/listar", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
      .then((res) => res.json())
      .then((data) => setCampanhas(Array.isArray(data) ? data : []))
      .catch(() => setCampanhas([]));

    fetch("http://localhost:8080/doacao-monetaria/listar", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
      .then((res) => res.json())
      .then((data) => setDoacoes(Array.isArray(data) ? data : []))
      .catch(() => setDoacoes([]));
  }, [token]);

  // --- FILTRO POR DOADOR ---
  useEffect(() => {
    const buscaTrim = buscaDoador.trim().toLowerCase();
    if (!buscaTrim) {
      setResultadoDoador(doacoes); // Exibe todas as doações se não filtrar
      return;
    }

    const doacoesDoDoador = doacoes.filter((d) => {
      if (!d.doador) return false;

      const nomeDoador = d.doador.nome ? d.doador.nome.toLowerCase() : "";
      const cpfDoadorNumeros = d.doador.cpf ? d.doador.cpf.replace(/\D/g, "") : "";

      if (nomeDoador.includes(buscaTrim)) return true;

      const buscaTrimNumeros = buscaTrim.replace(/\D/g, "");
      if (buscaTrimNumeros.length > 0 && cpfDoadorNumeros.includes(buscaTrimNumeros)) return true;

      return false;
    });

    setResultadoDoador(doacoesDoDoador);
  }, [buscaDoador, doacoes]);

  // --- FILTRO POR CAMPANHA ---
  useEffect(() => {
    const buscaTrim = buscaCampanha.trim().toLowerCase();
    if (!buscaTrim) {
      setResultadoCampanha([]);
      return;
    }
    const campanha = campanhas.find(
      (c) =>
        (c.nome && c.nome.toLowerCase().includes(buscaTrim)) ||
        (c.descricao && c.descricao.toLowerCase().includes(buscaTrim))
    );
    if (campanha) {
      const doacoesCampanha = doacoes.filter(
        (d) => d.campanha && d.campanha.id === campanha.id
      );
      const res = doacoesCampanha.map((d) => ({
        ...d,
        campanha: campanha,
        doador: doadores.find((doa) => doa.id === (d.doador?.id || d.doador)),
      }));
      setResultadoCampanha(res);
    } else {
      setResultadoCampanha([]);
    }
  }, [buscaCampanha, doadores, campanhas, doacoes]);

  // Modal handlers
  const handleShowModal = (doacao, tipo) => {
    setDoacaoSelecionada(doacao);
    setFiltroTipo(tipo);
    setShowModal(true);
  };
  const handleCloseModal = () => {
    setShowModal(false);
    setDoacaoSelecionada(null);
  };

  // Remover doação com re-fetch
  const handleRemoverDoacao = async () => {
    if (!doacaoSelecionada) return;
    try {
      const response = await fetch(
        `http://localhost:8080/doacao-monetaria/excluir/${doacaoSelecionada.id}`,
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
        // Recarregar lista
        fetch("http://localhost:8080/doacao-monetaria/listar", {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        })
          .then((res) => res.json())
          .then((data) => setDoacoes(Array.isArray(data) ? data : []));
      } else {
        toast.error("Erro ao remover doação.");
      }
    } catch (err) {
      toast.error("Erro ao remover doação.");
    } finally {
      handleCloseModal();
    }
  };

  return (
    <div>
      <ToastContainer position="top-right" autoClose={3000} />
      <Tabs activeKey={tab} onSelect={(k) => setTab(k)} className="mb-4" justify>
        {/* Pesquisa por Doador */}
        <Tab eventKey="doador" title="Pesquisar por Doador">
          <Card>
            <Card.Body>
              <Row>
                <Col md={8}>
                  <Form>
                    <Form.Group>
                      <Form.Label>Buscar doador (nome ou CPF):</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Ex: João ou 123.456.789-00"
                        value={buscaDoador}
                        onChange={(e) => setBuscaDoador(e.target.value)}
                      />
                    </Form.Group>
                  </Form>
                </Col>
              </Row>
              <Table responsive hover className="mt-4">
                <thead>
                  <tr>
                    <th>Campanha</th>
                    <th>Descrição</th>
                    <th>Data</th>
                    <th>Valor</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {resultadoDoador.length === 0 ? (
                    <tr>
                      <td colSpan={5} className="text-center text-muted">
                        Nenhuma doação encontrada para este doador.
                      </td>
                    </tr>
                  ) : (
                    resultadoDoador.map((item) => (
                      <tr key={item.id}>
                        <td>{item.campanha?.nome}</td>
                        <td>{item.campanha?.descricao}</td>
                        <td>
                          {item.data
                            ? new Date(item.data).toLocaleDateString()
                            : ""}
                        </td>
                        <td>{item.valor ? item.valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) : ""}</td>
                        <td>
                          <Button
                            size="sm"
                            variant="danger"
                            onClick={() => handleShowModal(item, "doador")}
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

        {/* Pesquisa por Campanha */}
        <Tab eventKey="campanha" title="Pesquisar por Campanha">
          <Card>
            <Card.Body>
              <Row>
                <Col md={8}>
                  <Form>
                    <Form.Group>
                      <Form.Label>Buscar campanha (nome ou descrição):</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Ex: Campanha Natal ou Alimentos"
                        value={buscaCampanha}
                        onChange={(e) => setBuscaCampanha(e.target.value)}
                      />
                    </Form.Group>
                  </Form>
                </Col>
              </Row>
              <Table responsive hover className="mt-4">
                <thead>
                  <tr>
                    <th>Nome do Doador</th>
                    <th>CPF</th>
                    <th>E-mail</th>
                    <th>Data Nasc.</th>
                    <th>Valor</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {resultadoCampanha.length === 0 ? (
                    <tr>
                      <td colSpan={6} className="text-center text-muted">
                        Nenhuma doação encontrada para esta campanha.
                      </td>
                    </tr>
                  ) : (
                    resultadoCampanha.map((item) => (
                      <tr key={item.id}>
                        <td>{item.doador?.nome}</td>
                        <td>{item.doador?.cpf}</td>
                        <td>{item.doador?.email}</td>
                        <td>
                          {item.doador?.dataNasc
                            ? new Date(item.doador.dataNasc).toLocaleDateString()
                            : ""}
                        </td>
                        <td>{item.valor ? item.valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) : ""}</td>
                        <td>
                          <Button
                            size="sm"
                            variant="danger"
                            onClick={() => handleShowModal(item, "campanha")}
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

      {/* Modal para confirmar exclusão */}
      <Modal show={showModal} onHide={handleCloseModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Confirmação de exclusão</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Tem certeza que deseja remover esta doação? Essa ação não poderá ser desfeita.
        </Modal.Body>
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

export default ListarDoacoesMonetarias;