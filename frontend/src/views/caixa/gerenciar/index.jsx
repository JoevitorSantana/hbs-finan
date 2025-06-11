import React, { useEffect, useState } from "react";
import { Button, Card, Col, Row, Table, Modal, Form } from "react-bootstrap";
import { ToastContainer, toast } from "react-toastify";
import { GiPayMoney, GiReceiveMoney } from "react-icons/gi";
import { FaMinus, FaPlus } from "react-icons/fa";
import { color } from "d3";


function toLocalDateTimeString() {
  const date = new Date();
  const pad = (n) => String(n).padStart(2, "0");
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(
    date.getDate()
  )}T${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}

const GerenciarCaixa = () => {
  const idCaixa = window.location.pathname.split("/").pop();

  const [caixa, setCaixa] = useState(null);
  const [movimentacoes, setMovimentacoes] = useState([]);
  const [apoiadores, setApoiadores] = useState([]);

  const [showModal, setShowModal] = useState(false);
  const [valorDoacao, setValorDoacao] = useState("");
  const [apoiadorSelecionado, setApoiadorSelecionado] = useState("");
  const [tipoDoacao, setTipoDoacao] = useState("MONETARIA");
  const [nomeInstituicao, setNomeInstituicao] = useState("");
  const [cnpjInstituicao, setCnpjInstituicao] = useState("");

  const [valorAtual, setValorAtual] = useState(0);
  const [filtroMin, setFiltroMin] = useState("");
  const [filtroMax, setFiltroMax] = useState("");

  const token = localStorage.getItem("site");

  useEffect(() => {
    const carregarTudo = async () => {
      await carregarCaixa();
      await carregarApoiadores();
      await carregarMovimentacoes();
    };
    carregarTudo();
  }, []);

  const carregarCaixa = async () => {
    try {
      const response = await fetch(`http://localhost:8080/caixa/${idCaixa}`, {
        headers: { Authorization: "Bearer " + token },
      });
      if (!response.ok) throw new Error("Erro ao carregar caixa");
      const data = await response.json();
      setCaixa(data);
    } catch {
      toast.error("Erro ao carregar dados do caixa.");
    }
  };

  const carregarMovimentacoes = async () => {
    try {
      const respMonetaria = await fetch(
        `http://localhost:8080/doacao-monetaria/caixa/${idCaixa}`,
        { headers: { Authorization: "Bearer " + token } }
      );
      const dataMonetaria = await respMonetaria.json();

      const movMonetarias = dataMonetaria?.map((d) => {
        // const apoiadorEncontrado = apoiadores.find((a) => a.id === d.apoiador.id);
        return {
          id: d.id,
          data: d.data,
          apoiador: d.apoiador ? d.apoiador.nome : "Apoiador desconhecido",
          valor: parseFloat(d.valor),
          tipo: "DM",
        };
      }) || [];

      const respInstituicao = await fetch(
        `http://localhost:8080/doacao-instituicao/caixa/${idCaixa}`,
        { headers: { Authorization: "Bearer " + token } }
      );
      const dataInstituicao = await respInstituicao.json();

      const movInstituicao = dataInstituicao?.map((d) => ({
        id: d.id,
        data: d.data,
        nome: d.nome || "Instituição desconhecida",
        valor: parseFloat(d.valor),
        tipo: "DI",
      })) || [];

      const todasMovimentacoes = [...movMonetarias, ...movInstituicao];
      todasMovimentacoes.sort((a, b) => new Date(b.data) - new Date(a.data));

      setMovimentacoes(todasMovimentacoes);
      calcularValorTotalCaixa(todasMovimentacoes);
    } catch {
      toast.error("Erro ao carregar movimentações");
    }
  };

  const carregarApoiadores = async () => {
    try {
      const response = await fetch("http://localhost:8080/apoiador/listar", {
        headers: { Authorization: "Bearer " + token },
      });
      if (!response.ok) throw new Error("Erro ao carregar apoiadores");
      const data = await response.json();
      setApoiadores(data);
    } catch {
      toast.error("Erro ao carregar lista de apoiadores");
    }
  };

  const calcularValorTotalCaixa = (movs) => {
    const totalMovimentacoes = movs.reduce((acc, mov) => {
      if (mov.tipo === "DM") return acc + (mov.valor || 0);
      else if (mov.tipo === "DI") return acc - (mov.valor || 0);
      return acc;
    }, 0);

    const totalComInicial = (caixa?.valorInicial || 0) + totalMovimentacoes;
    setValorAtual(totalComInicial);
  };

  const registrarDoacao = async () => {
    if (caixa?.dataFechamentoCaixa) {
      toast.error("Caixa já finalizado. Não é possível registrar novas doações.");
      return;
    }

    try {
      if (!valorDoacao || parseFloat(valorDoacao) <= 0) {
        toast.warning("Informe um valor válido para a doação");
        return;
      }

      let payload;
      let url;

      if (tipoDoacao === "MONETARIA") {
        if (!apoiadorSelecionado) {
          toast.warning("Selecione um apoiador para doação monetária");
          return;
        }
        payload = {
          valor: parseFloat(valorDoacao),
          idApoiador: apoiadorSelecionado,
          data: toLocalDateTimeString(),
          idCaixa: caixa.id,
        };
        url = "http://localhost:8080/doacao-monetaria/novo";
      } else {
        if (!nomeInstituicao || !cnpjInstituicao) {
          toast.warning("Informe nome e CNPJ da instituição");
          return;
        }
        payload = {
          tipo: "INSTITUICAO",
          valor: parseFloat(valorDoacao),
          nome: nomeInstituicao,
          cnpj: cnpjInstituicao,
          data: toLocalDateTimeString(),
          idCaixa: caixa.id,
        };
        url = "http://localhost:8080/doacao-instituicao/novo";
      }

      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const err = await response.json();
        throw new Error(err.message || "Erro ao registrar doação.");
      }

      toast.success("Doação registrada com sucesso!");
      setShowModal(false);
      resetarCamposDoacao();
      carregarCaixa();
      carregarMovimentacoes();
    } catch (error) {
      toast.error(error.message || "Erro ao registrar doação.");
    }
  };

  const resetarCamposDoacao = () => {
    setTipoDoacao("MONETARIA");
    setValorDoacao("");
    setApoiadorSelecionado("");
    setNomeInstituicao("");
    setCnpjInstituicao("");
  };

  const finalizarCaixa = async () => {
    try {
      if (!caixa) return;

      const fechamento = {
        ...caixa,
        dataFechamentoCaixa: toLocalDateTimeString(),
        valorFinal: valorAtual + (caixa.valorInicial || 0),
      };

      const response = await fetch(`http://localhost:8080/caixa/editar/${caixa.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
        body: JSON.stringify(fechamento),
      });

      if (!response.ok) throw new Error("Erro ao finalizar caixa.");

      toast.success("Caixa finalizado com sucesso!");
      carregarCaixa();
      carregarMovimentacoes();
    } catch (error) {
      toast.error(error.message || "Erro ao finalizar caixa.");
    }
  };

  const movimentacoesFiltradas = movimentacoes.filter((mov) => {
    const val = mov.valor || 0;
    const min = filtroMin !== "" ? parseFloat(filtroMin) : null;
    const max = filtroMax !== "" ? parseFloat(filtroMax) : null;
    if (min !== null && val < min) return false;
    if (max !== null && val > max) return false;
    return true;
  });

  return (
    <>
      <ToastContainer />
      <Row>
        <Col md={3}>
          <Card>
            <Card.Body>
              <Card.Title>Valor Inicial</Card.Title>
              <Card.Text>
                <GiPayMoney size={40} /> R$ {caixa?.valorInicial?.toFixed(2) || "0.00"}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card>
            <Card.Body>
              <Card.Title>Valor Atual</Card.Title>
              <Card.Text>
                <GiReceiveMoney size={40} /> R$ {valorAtual.toFixed(2)}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="mt-3">
        <Col>
          <Button
            variant="success"
            className="mb-3"
            onClick={() => setShowModal(true)}
            disabled={!!caixa?.dataFechamentoCaixa}
          >
            Registrar Doação
          </Button>

          {!!caixa?.dataFechamentoCaixa ? (
            <Card bg="warning" text="dark" className="mb-3">
              <Card.Body>Caixa finalizado em {new Date(caixa.dataFechamentoCaixa).toLocaleString()}</Card.Body>
            </Card>
          ) : (
            <Button variant="danger" className="mb-3" onClick={finalizarCaixa}>
              Finalizar Caixa
            </Button>
          )}

          <Form>
            <Row className="mb-3">
              <Col md={6}>
                <Form.Label>Filtrar valor mínimo</Form.Label>
                <Form.Control
                  type="number"
                  value={filtroMin}
                  onChange={(e) => setFiltroMin(e.target.value)}
                  placeholder="Ex: 10.00"
                  min="0"
                />
              </Col>
              <Col md={6}>
                <Form.Label>Filtrar valor máximo</Form.Label>
                <Form.Control
                  type="number"
                  value={filtroMax}
                  onChange={(e) => setFiltroMax(e.target.value)}
                  placeholder="Ex: 100.00"
                  min="0"
                />
              </Col>
            </Row>
          </Form>

          <Table striped bordered hover>
            <thead>
              <tr>
                <th>ID</th>
                <th>Data</th>
                <th>Apoiador / Instituição</th>
                <th>Valor</th>
                <th>Tipo</th>
              </tr>
            </thead>
            <tbody>
              {movimentacoesFiltradas.length === 0 && (
                <tr>
                  <td colSpan={5} className="text-center">
                    Nenhuma movimentação encontrada
                  </td>
                </tr>
              )}
              {movimentacoesFiltradas.map((mov) => (
                <tr key={mov.id}>
                  <td>{mov.id}</td>
                  <td>{new Date(mov.data).toLocaleString()}</td>
                  <td>{mov.tipo === "DM" ? mov.apoiador : mov.nome}</td>
                  <td>R$ {mov.valor.toFixed(2)}</td>
                  <td>{mov.tipo === "DM" ? "Doação Monetária" : "Doação Instituição"}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>

      {/* Modal Registrar Doação */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Registrar Doação</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="tipoDoacao" className="mb-3">
              <Form.Label>Tipo de Doação</Form.Label>
              <Form.Select
                value={tipoDoacao}
                onChange={(e) => setTipoDoacao(e.target.value)}
                disabled={!!caixa?.dataFechamentoCaixa}
              >
                <option value="MONETARIA">Monetária</option>
                <option value="INSTITUICAO">Instituição</option>
              </Form.Select>
            </Form.Group>

            <Form.Group controlId="valorDoacao" className="mb-3">
              <Form.Label>Valor da Doação</Form.Label>
              <Form.Control
                type="number"
                min="0.01"
                step="0.01"
                value={valorDoacao}
                onChange={(e) => setValorDoacao(e.target.value)}
                disabled={!!caixa?.dataFechamentoCaixa}
              />
            </Form.Group>

            {tipoDoacao === "MONETARIA" && (
              <Form.Group controlId="apoiadorSelecionado" className="mb-3">
                <Form.Label>Apoiador</Form.Label>
                <Form.Select
                  value={apoiadorSelecionado}
                  onChange={(e) => setApoiadorSelecionado(e.target.value)}
                  disabled={!!caixa?.dataFechamentoCaixa}
                >
                  <option value="">Selecione um apoiador</option>
                  {apoiadores.map((a) => (
                    <option key={a.id} value={a.id}>
                      {a.nome}
                    </option>
                  ))}
                </Form.Select>
              </Form.Group>
            )}

            {tipoDoacao === "INSTITUICAO" && (
              <>
                <Form.Group controlId="nomeInstituicao" className="mb-3">
                  <Form.Label>Nome da Instituição</Form.Label>
                  <Form.Control
                    type="text"
                    value={nomeInstituicao}
                    onChange={(e) => setNomeInstituicao(e.target.value)}
                    disabled={!!caixa?.dataFechamentoCaixa}
                  />
                </Form.Group>
                <Form.Group controlId="cnpjInstituicao" className="mb-3">
                  <Form.Label>CNPJ da Instituição</Form.Label>
                  <Form.Control
                    type="text"
                    value={cnpjInstituicao}
                    onChange={(e) => setCnpjInstituicao(e.target.value)}
                    disabled={!!caixa?.dataFechamentoCaixa}
                  />
                </Form.Group>
              </>
            )}
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancelar
          </Button>
          <Button
            variant="primary"
            onClick={registrarDoacao}
            disabled={!!caixa?.dataFechamentoCaixa}
          >
            Registrar
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default GerenciarCaixa;