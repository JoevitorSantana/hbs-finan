import React, { useState, useEffect } from "react";
import { Card, Table, Button, Form, Row, Col, Modal, Tabs, Tab } from "react-bootstrap";
import { ToastContainer, toast } from "react-toastify";
import { FaRegTrashAlt } from "react-icons/fa";

const ListarInscricaoEvento = () => {
  const [tab, setTab] = useState("apoiador");
  const [buscaApoiador, setBuscaApoiador] = useState("");
  const [buscaEvento, setBuscaEvento] = useState("");

  const [apoiadores, setApoiadores] = useState([]);
  const [eventos, setEventos] = useState([]);
  const [inscricoes, setInscricoes] = useState([]);

  // Resultados filtrados
  const [resultadoApoiador, setResultadoApoiador] = useState([]);
  const [resultadoEvento, setResultadoEvento] = useState([]);

  const [showModal, setShowModal] = useState(false);
  const [inscricaoSelecionada, setInscricaoSelecionada] = useState(null);
  const [filtroTipo, setFiltroTipo] = useState(""); // "apoiador" ou "evento"

  const token = localStorage.getItem("site");

  // Carregar dados iniciais
  useEffect(() => {
    fetch("http://localhost:8080/apoiador/listar", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
      .then((res) => res.json())
      .then((data) => setApoiadores(Array.isArray(data) ? data : []))
      .catch(() => setApoiadores([]));

    fetch("http://localhost:8080/eventos/listar", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
      .then((res) => res.json())
      .then((data) => setEventos(Array.isArray(data) ? data : []))
      .catch(() => setEventos([]));

    fetch("http://localhost:8080/inscricoes-evento/listar", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
      .then((res) => res.json())
      .then((data) => setInscricoes(Array.isArray(data) ? data : []))
      .catch(() => setInscricoes([]));
  }, [token]);

    // --- FILTRO POR APOIADOR (CORRIGIDO) ---
    useEffect(() => {
    const buscaTrim = buscaApoiador.trim().toLowerCase();
    if (!buscaTrim) {
        setResultadoApoiador([]);
        return;
    }

    const inscricoesDoApoiador = inscricoes.filter((i) => {
        // Verifica se a inscrição possui um apoiador associado
        if (!i.apoiador) {
        return false;
        }

        const nomeApoiador = i.apoiador.nome ? i.apoiador.nome.toLowerCase() : "";
        const cpfApoiadorNumeros = i.apoiador.cpf ? i.apoiador.cpf.replace(/\D/g, "") : "";

        // 1. Tenta fazer a correspondência pelo nome do apoiador
        if (nomeApoiador.includes(buscaTrim)) {
        return true;
        }

        // 2. Tenta fazer a correspondência pelo CPF
        //    Remove caracteres não numéricos do termo de busca para comparar com o CPF
        const buscaTrimNumeros = buscaTrim.replace(/\D/g, "");
        //    Só tenta a busca por CPF se o termo de busca convertido para números não for vazio
        if (buscaTrimNumeros.length > 0 && cpfApoiadorNumeros.includes(buscaTrimNumeros)) {
        return true;
        }

        // Se não houve correspondência nem por nome nem por CPF
        return false;
    });

    // Define o resultado. Como suas 'inscricoes' já vêm com os objetos 'apoiador'
    // e 'evento' aninhados e completos (conforme seu JSON do Postman),
    // não é necessário mapear ou buscar dados adicionais aqui.
    setResultadoApoiador(inscricoesDoApoiador);

    }, [buscaApoiador, inscricoes]); // Dependências do useEffect






  // --- FILTRO POR EVENTO ---
  useEffect(() => {
    const buscaTrim = buscaEvento.trim().toLowerCase();
    if (!buscaTrim) {
      setResultadoEvento([]);
      return;
    }
    const evento = eventos.find(
      (e) =>
        (e.nome && e.nome.toLowerCase().includes(buscaTrim)) ||
        (e.local && e.local.toLowerCase().includes(buscaTrim))
    );
    if (evento) {
      const inscricoesEvento = inscricoes.filter(
        (i) => i.evento && i.evento.id === evento.id
      );
      const res = inscricoesEvento.map((insc) => ({
        ...insc,
        evento: evento,
        apoiador: apoiadores.find((a) => a.id === (insc.apoiador?.id || insc.apoiador)),
      }));
      setResultadoEvento(res);
    } else {
      setResultadoEvento([]);
    }
  }, [buscaEvento, apoiadores, eventos, inscricoes]);

  // Modal handlers
  const handleShowModal = (inscricao, tipo) => {
    setInscricaoSelecionada(inscricao);
    setFiltroTipo(tipo);
    setShowModal(true);
  };
  const handleCloseModal = () => {
    setShowModal(false);
    setInscricaoSelecionada(null);
  };

  // Remover inscrição com re-fetch!
  const handleDesinscrever = async () => {
    if (!inscricaoSelecionada) return;
    try {
      const response = await fetch(
        `http://localhost:8080/inscricoes-evento/excluir/${inscricaoSelecionada.id}`,
        {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        }
      );
      if (response.ok) {
        toast.success("Inscrição removida com sucesso!");
        // REFETCH após exclusão
        fetch("http://localhost:8080/inscricoes-evento/listar", {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        })
          .then((res) => res.json())
          .then((data) => setInscricoes(Array.isArray(data) ? data : []));
      } else {
        toast.error("Erro ao remover inscrição.");
      }
    } catch (err) {
      toast.error("Erro ao remover inscrição.");
    } finally {
      handleCloseModal();
    }
  };

  return (
    <div>
      <ToastContainer position="top-right" autoClose={3000} />
      <Tabs
        activeKey={tab}
        onSelect={(k) => setTab(k)}
        className="mb-4"
        justify
      >
        {/* ----------- PESQUISA POR APOIADOR ----------- */}
        <Tab eventKey="apoiador" title="Pesquisar por Apoiador">
          <Card>
            <Card.Body>
              <Row>
                <Col md={8}>
                  <Form>
                    <Form.Group>
                      <Form.Label>Buscar apoiador (nome ou CPF):</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Ex: Maria ou 123.456.789-00"
                        value={buscaApoiador}
                        onChange={(e) => setBuscaApoiador(e.target.value)}
                      />
                    </Form.Group>
                  </Form>
                </Col>
              </Row>
              <Table responsive hover className="mt-4">
                <thead>
                  <tr>
                    <th>Evento</th>
                    <th>Local</th>
                    <th>Data</th>
                    <th>Descrição</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {resultadoApoiador.length === 0 ? (
                    <tr>
                      <td colSpan={5} className="text-center text-muted">
                        Nenhum evento encontrado para esse apoiador.
                      </td>
                    </tr>
                  ) : (
                    resultadoApoiador.map((item) => (
                      <tr key={item.id}>
                        <td>{item.evento?.nome}</td>
                        <td>{item.evento?.local}</td>
                        <td>
                          {item.evento?.data
                            ? new Date(item.evento.data).toLocaleDateString()
                            : ""}
                        </td>
                        <td>{item.evento?.descricao}</td>
                        <td>
                          <Button
                            size="sm"
                            variant="danger"
                            onClick={() => handleShowModal(item, "apoiador")}
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

        {/* ----------- PESQUISA POR EVENTO ----------- */}
        <Tab eventKey="evento" title="Pesquisar por Evento">
          <Card>
            <Card.Body>
              <Row>
                <Col md={8}>
                  <Form>
                    <Form.Group>
                      <Form.Label>Buscar evento (nome ou local):</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Ex: Festa ou Ginásio"
                        value={buscaEvento}
                        onChange={(e) => setBuscaEvento(e.target.value)}
                      />
                    </Form.Group>
                  </Form>
                </Col>
              </Row>
              <Table responsive hover className="mt-4">
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>E-mail</th>
                    <th>Data Nasc.</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {resultadoEvento.length === 0 ? (
                    <tr>
                      <td colSpan={5} className="text-center text-muted">
                        Nenhum apoiador inscrito neste evento.
                      </td>
                    </tr>
                  ) : (
                    resultadoEvento.map((item) => (
                      <tr key={item.id}>
                        <td>{item.apoiador?.nome}</td>
                        <td>{item.apoiador?.cpf}</td>
                        <td>{item.apoiador?.email}</td>
                        <td>
                          {item.apoiador?.dataNasc
                            ? new Date(item.apoiador.dataNasc).toLocaleDateString()
                            : ""}
                        </td>
                        <td>
                          <Button
                            size="sm"
                            variant="danger"
                            onClick={() => handleShowModal(item, "evento")}
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

      <DeleteConfirmationModal
        show={showModal}
        onHide={handleCloseModal}
        onConfirm={handleDesinscrever}
        inscricao={inscricaoSelecionada}
        tipo={filtroTipo}
      />
    </div>
  );
};

export default ListarInscricaoEvento;

// Modal para confirmar exclusão
const DeleteConfirmationModal = ({ show, onHide, onConfirm, inscricao, tipo }) => (
  <Modal show={show} onHide={onHide} centered>
    <Modal.Header closeButton>
      <Modal.Title>Confirmar Cancelamento</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      {tipo === "apoiador" && (
        <>Tem certeza que deseja cancelar a inscrição no evento <strong>{inscricao?.evento?.nome}</strong>?</>
      )}
      {tipo === "evento" && (
        <>Tem certeza que deseja remover o apoiador <strong>{inscricao?.apoiador?.nome}</strong> deste evento?</>
      )}
    </Modal.Body>
    <Modal.Footer>
      <Button variant="secondary" onClick={onHide}>
        Cancelar
      </Button>
      <Button variant="danger" onClick={onConfirm}>
        Excluir
      </Button>
    </Modal.Footer>
  </Modal>
);
