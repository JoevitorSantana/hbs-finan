import React, { useState, useEffect } from "react";
import { Card, Table, Button, Form, Row, Col, Spinner } from "react-bootstrap";
import { ToastContainer, toast } from "react-toastify";

const isCPF = valor => {
  const numeros = valor.replace(/\D/g, "");
  return numeros.length === 11;
};

const InscricaoEventoView = () => {
  const [apoiadores, setApoiadores] = useState([]);
  const [busca, setBusca] = useState("");
  const [apoiadoresFiltrados, setApoiadoresFiltrados] = useState([]);
  const [apoiadorSelecionado, setApoiadorSelecionado] = useState(null);

  const [eventos, setEventos] = useState([]);
  const [eventosInscritos, setEventosInscritos] = useState([]);
  const [selecionados, setSelecionados] = useState([]);
  const [loadingApoiador, setLoadingApoiador] = useState(true);
  const [loadingEventos, setLoadingEventos] = useState(false);
  const [inscrevendo, setInscrevendo] = useState(false);

  // Carregar todos os apoiadores
  useEffect(() => {
    const token = localStorage.getItem("site");
    fetch("http://localhost:8080/apoiador/listar", {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
      },
    })
      .then(res => res.ok ? res.json() : [])
      .then(data => {
        setApoiadores(Array.isArray(data) ? data : []);
        setApoiadoresFiltrados(Array.isArray(data) ? data : []);
      })
      .catch(() => {
        setApoiadores([]);
        setApoiadoresFiltrados([]);
      })
      .finally(() => setLoadingApoiador(false));
  }, []);

  // Filtro de busca (nome ou CPF)
  useEffect(() => {
    const buscaTrim = busca.trim();
    if (!buscaTrim) {
      setApoiadoresFiltrados(apoiadores);
      return;
    }
    // Busca por CPF: compara só números!
    if (isCPF(buscaTrim)) {
      const numerosBusca = buscaTrim.replace(/\D/g, "");
      setApoiadoresFiltrados(
        apoiadores.filter(apoiador =>
          apoiador.cpf && apoiador.cpf.replace(/\D/g, "").includes(numerosBusca)
        )
      );
    } else {
      const buscaLower = buscaTrim.toLowerCase();
      setApoiadoresFiltrados(
        apoiadores.filter(apoiador =>
          apoiador.nome && apoiador.nome.toLowerCase().includes(buscaLower)
        )
      );
    }
  }, [busca, apoiadores]);

  // Buscar eventos futuros ao carregar ou ao selecionar apoiador
  useEffect(() => {
    setLoadingEventos(true);
    fetch("http://localhost:8080/eventos/listar", {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem("site"),
      }
    })
      .then(res => res.json())
      .then(data => {
        const hoje = new Date();
        const futuros = data.filter(ev => {
          const dataEvento = new Date(ev.data);
          return dataEvento >= hoje;
        });
        setEventos(futuros);
      })
      .catch(() => setEventos([]))
      .finally(() => setLoadingEventos(false));
    setSelecionados([]);
  }, [apoiadorSelecionado]);

  // Buscar inscrições do apoiador selecionado
  useEffect(() => {
    if (!apoiadorSelecionado) {
      setEventosInscritos([]);
      return;
    }
    fetch("http://localhost:8080/inscricoes-evento/listar", {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem("site"),
      }
    })
      .then(res => res.json())
      .then(data => {
        const inscritos = data
          .filter(inscricao => inscricao.apoiador && inscricao.apoiador.id === apoiadorSelecionado.id)
          .map(inscricao => inscricao.evento.id);
        setEventosInscritos(inscritos);
      })
      .catch(() => setEventosInscritos([]));
  }, [apoiadorSelecionado]);

  // Manipula checkbox de eventos
  const handleSelecionar = idEvento => {
    if (selecionados.includes(idEvento)) {
      setSelecionados(selecionados.filter(id => id !== idEvento));
    } else {
      setSelecionados([...selecionados, idEvento]);
    }
  };

  // Inscrever em todos marcados
  const handleInscrever = async () => {
    if (selecionados.length === 0) {
      toast.error("Selecione pelo menos um evento!");
      return;
    }
    setInscrevendo(true);
    try {
      for (let eventoId of selecionados) {
        const res = await fetch("http://localhost:8080/inscricoes-evento/nova", {
          method: "POST",
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("site"),
          },
          body: JSON.stringify({
            apoiador: { id: apoiadorSelecionado.id },
            evento: { id: eventoId }
          })
        });
        if (!res.ok) throw new Error("Erro ao inscrever");
      }
      toast.success("Inscrição(ões) realizada(s) com sucesso!");
      setSelecionados([]);
      setTimeout(() => {
        window.location.reload();
      }, 2000);
    } catch (err) {
      toast.error("Erro ao inscrever em um ou mais eventos.");
    } finally {
      setInscrevendo(false);
    }
  };

  // Se tem algum evento já inscrito, mostra mensagem
  const algumInscrito = eventos.some(ev => eventosInscritos.includes(ev.id));

  return (
    <div>
      <ToastContainer position="top-right" autoClose={3000} />
      <Card className="mb-4">
        <Card.Body>
          <Row>
            <Col>
              <h5 style={{ fontWeight: 'bold' }}>Buscar apoiador</h5>
              <Form>
                <Row className="align-items-center">
                  <Col md={2}>
                    <Form.Label style={{ fontWeight: 'bold' }}>buscar</Form.Label>
                  </Col>
                  <Col md={10}>
                    <Form.Control
                      type="text"
                      placeholder="cpf ou nome"
                      value={busca}
                      onChange={e => setBusca(e.target.value)}
                    />
                  </Col>
                </Row>
              </Form>
            </Col>
          </Row>
          <Row>
            <Col>
              {loadingApoiador ? (
                <div className="text-center mt-3"><Spinner animation="border" /></div>
              ) : (
                <Table responsive hover className="mt-3">
                  <thead>
                    <tr>
                      <th></th>
                      <th>Nome</th>
                      <th>CPF</th>
                      <th>E-mail</th>
                      <th>Data Nasc.</th>
                      <th>Endereço</th>
                    </tr>
                  </thead>
                  <tbody>
                    {apoiadoresFiltrados.map(apoiador => (
                      <tr key={apoiador.id} style={{ background: apoiadorSelecionado && apoiadorSelecionado.id === apoiador.id ? "#e8f4ff" : undefined }}>
                        <td>
                          <Form.Check
                            type="radio"
                            name="apoiadorRadio"
                            checked={apoiadorSelecionado && apoiadorSelecionado.id === apoiador.id}
                            onChange={() => setApoiadorSelecionado(apoiador)}
                          />
                        </td>
                        <td>{apoiador.nome}</td>
                        <td>{apoiador.cpf}</td>
                        <td>{apoiador.email}</td>
                        <td>{apoiador.data_nasc ? new Date(apoiador.data_nasc).toLocaleDateString() : ""}</td>
                        <td>{apoiador.endereco}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              )}
            </Col>
          </Row>
        </Card.Body>
      </Card>

      {apoiadorSelecionado && (
        <Card>
          <Card.Header>
            <div style={{ width: '100%', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <b>Eventos disponíveis</b>
              <Button
                variant="primary"
                style={{ minWidth: 150, fontWeight: 'bold' }}
                disabled={inscrevendo}
                onClick={handleInscrever}
              >
                {inscrevendo ? "Inscrevendo..." : "INSCREVER"}
              </Button>
            </div>
          </Card.Header>
          <Card.Body>
            {loadingEventos ? (
              <div className="text-center"><Spinner animation="border" /></div>
            ) : (
              <>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th></th>
                      <th>Local</th>
                      <th>Data</th>
                      <th>Descrição</th>
                      <th>Nome</th>
                      <th>Materiais</th>
                      <th>Funcionario ID</th>
                    </tr>
                  </thead>
                  <tbody>
                    {eventos.map(evento => {
                      const inscrito = eventosInscritos.includes(evento.id);
                      return (
                        <tr key={evento.id}>
                          <td>
                            <Form.Check
                              type="checkbox"
                              checked={selecionados.includes(evento.id)}
                              onChange={() => handleSelecionar(evento.id)}
                              disabled={inscrito}
                            />
                          </td>
                          <td style={inscrito ? { color: "#d70000", fontWeight: "bold" } : {}}>
                            {evento.local}
                          </td>
                          <td style={inscrito ? { color: "#d70000" } : {}}>
                            {evento.data ? evento.data.slice(0, 10) : ""}
                          </td>
                          <td style={inscrito ? { color: "#d70000" } : {}}>
                            {evento.descricao}
                          </td>
                          <td style={inscrito ? { color: "#d70000" } : {}}>
                            {evento.nome}
                          </td>
                          <td style={inscrito ? { color: "#d70000" } : {}}>
                            {evento.materiais}
                          </td>
                          <td style={inscrito ? { color: "#d70000" } : {}}>
                            {evento.func_id}
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </Table>
                {algumInscrito && (
                  <div style={{ color: "#d70000", fontSize: 14, marginTop: 4 }}>
                    *Apoiador já inscrito
                  </div>
                )}
              </>
            )}
          </Card.Body>
        </Card>
      )}
    </div>
  );
};

export default InscricaoEventoView;
