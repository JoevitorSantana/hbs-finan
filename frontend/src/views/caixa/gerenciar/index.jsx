import React, { useEffect, useState } from "react";
import { Button, Card, Col, Row, Table, Modal, Form } from "react-bootstrap";
import { ToastContainer, toast } from "react-toastify";
import { GiPayMoney, GiReceiveMoney } from "react-icons/gi";
import { FaMinus, FaPlus } from "react-icons/fa";
import { color } from "d3";


function toLocalDateTimeString() {
    const date = new Date();
    const pad = n => String(n).padStart(2, '0');

    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}

const GerenciarCaixa = () => {
    const idCaixa = window.location.pathname.split('/').pop();

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

    const token = localStorage.getItem("site");

    useEffect(() => {
        carregarCaixa();
        carregarMovimentacoes();
        // carregarApoiadores();
        calcularValorTotalCaixa();
    }, []);

    const calcularValorTotalCaixa = () => {
        let acc = 0;
        movimentacoes?.map((mov) => {
            if (mov.tipo == 'DM')
                acc += mov.valor;
            else 
                acc -+ mov.valor;
        });

        setValorAtual(acc);
    }

    const carregarCaixa = async () => {
        const response = await fetch(`http://localhost:8080/caixa/${idCaixa}`, {
            headers: {
                Authorization: "Bearer " + token,
            },
        });
        const data = await response.json();
        setCaixa(data);
    };

    const carregarMovimentacoes = async () => {
        const response = await fetch(`http://localhost:8080/doacao-monetaria/caixa/${idCaixa}`, {
            headers: {
                Authorization: "Bearer " + token,
            },
        });
        const data = await response.json();
        let movMonetarias = [];
        data?.map((doacao) => {
            let monetaria = {
                id: doacao.id,
                data: doacao.data,
                id_ap: '-',
                valor: doacao.valor,
                tipo: 'DM'
            };

            movMonetarias.push(monetaria);
        });

        setMovimentacoes(movMonetarias);
        calcularValorTotalCaixa();
    };

    const carregarApoiadores = async () => {
        const response = await fetch("http://localhost:8080/apoiadores/listar", {
            headers: {
                Authorization: "Bearer " + token,
            },
        });
        const data = await response.json();
        setApoiadores(data);
    };

    const registrarDoacao = async () => {
        try {
            const payload =
                tipoDoacao === "MONETARIA"
                    ? {
                        valor: parseFloat(valorDoacao),
                        idApoiador: apoiadorSelecionado || 1,
                        data: toLocalDateTimeString(),
                        idCaixa: caixa.id,
                    }
                    : {
                        tipo: "INSTITUICAO",
                        valor: parseFloat(valorDoacao),
                        nomeInstituicao,
                        cnpjInstituicao,
                        caixaId: caixa.id,
                    };

            if (tipoDoacao == "MONETARIA") {
                console.log(payload);
                const response = await fetch("http://localhost:8080/doacao-monetaria/novo", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + token,
                    },
                    body: JSON.stringify(payload),
                });

                if (response.ok) {
                    toast.success("Doação registrada com sucesso!");
                    setShowModal(false);
                    resetarCamposDoacao();
                    carregarCaixa();
                    carregarMovimentacoes();
                    calcularValorTotalCaixa();
                } else {
                    const error = await response.json();
                    toast.error(error.message || "Erro ao registrar doação.");
                }
            }
        } catch (err) {
            console.error(err);
            toast.error("Erro ao registrar doação.");
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
            const response = await fetch(`http://localhost:8080/caixas/finalizar/${caixa.id}`, {
                method: "PUT",
                headers: {
                    Authorization: "Bearer " + token,
                },
            });

            if (response.ok) {
                toast.success("Caixa finalizado com sucesso!");
                carregarCaixa();
                carregarMovimentacoes();
            } else {
                toast.error("Erro ao finalizar caixa.");
            }
        } catch (err) {
            console.error(err);
            toast.error("Erro ao finalizar caixa.");
        }
    };

    return (
        <>
            <ToastContainer />
            <Row>
                <Col>
                    <Card>
                        <Card.Header>
                            <Card.Title>Caixa Dia { caixa && new Date(caixa.dataAberturaCaixa).toLocaleString("pt-BR", {day: "2-digit", month: "2-digit", year: "numeric"})}</Card.Title>
                        </Card.Header>
                        <Card.Body>
                            <h5>Valor atual em caixa: <strong>{new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL'}).format(valorAtual)}</strong></h5>
                            <Button variant="success" onClick={() => setShowModal(true)}>Registrar Doação</Button>{' '}
                            <Button variant="danger" onClick={finalizarCaixa}>Finalizar Caixa</Button>
                            <hr />
                            <h5>Movimentações do Dia</h5>
                            <Table hover responsive>
                                <thead>
                                <tr>
                                    <th>Tipo</th>
                                    <th>Valor</th>
                                    <th>Apoiador/Instituição</th>
                                    <th>Data</th>
                                </tr>
                                </thead>
                                <tbody>
                                {movimentacoes && movimentacoes.map((mov, idx) => (
                                    <tr key={idx}>
                                        <td>{mov.tipo == 'DM' ? <GiReceiveMoney title="Doação Monetária" color="green" size={30}/> : <GiPayMoney title="Doação Instituição" color="red" size={30} /> }</td>
                                        <td style={{fontWeight: 'bolder', color: (mov.tipo == 'DM' ? 'green' : 'red')}} >{mov.tipo == 'DM' ? <FaPlus /> : <FaMinus /> } R$ {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL'}).format(mov.valor)}</td>
                                        <td>{/*mov.descricao*/}</td>
                                        <td>{new Date(mov.data).toLocaleString('pt-BR',  {
                                            day: '2-digit',
                                            month: '2-digit',
                                            year: 'numeric'
                                        })}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </Table>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Modal show={showModal} onHide={() => setShowModal(false)} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Registrar Doação</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tipo de Doação</Form.Label>
                            <Form.Select
                                value={tipoDoacao}
                                onChange={(e) => setTipoDoacao(e.target.value)}
                            >
                                <option value="MONETARIA">Monetária</option>
                                <option value="INSTITUICAO">Instituição</option>
                            </Form.Select>
                        </Form.Group>

                        {tipoDoacao === "MONETARIA" && (
                            <>
                                <Form.Group className="mb-3">
                                    <Form.Label>Valor</Form.Label>
                                    <Form.Control
                                        type="number"
                                        min="0"
                                        step="0.01"
                                        value={valorDoacao}
                                        onChange={(e) => setValorDoacao(e.target.value)}
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Apoiador (opcional)</Form.Label>
                                    <Form.Select
                                        value={apoiadorSelecionado}
                                        onChange={(e) => setApoiadorSelecionado(e.target.value)}
                                    >
                                        <option value="">Nenhum</option>
                                        {apoiadores.map((apoiador) => (
                                            <option key={apoiador.id} value={apoiador.id}>
                                                {apoiador.nome}
                                            </option>
                                        ))}
                                    </Form.Select>
                                </Form.Group>
                            </>
                        )}

                        {tipoDoacao === "INSTITUICAO" && (
                            <>
                                <Form.Group className="mb-3">
                                    <Form.Label>Nome da Instituição</Form.Label>
                                    <Form.Control
                                        type="text"
                                        value={nomeInstituicao}
                                        onChange={(e) => setNomeInstituicao(e.target.value)}
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>CNPJ da Instituição</Form.Label>
                                    <Form.Control
                                        type="text"
                                        value={cnpjInstituicao}
                                        onChange={(e) => setCnpjInstituicao(e.target.value)}
                                        placeholder="00.000.000/0000-00"
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Valor</Form.Label>
                                    <Form.Control
                                        type="number"
                                        min="0"
                                        step="0.01"
                                        value={valorDoacao}
                                        onChange={(e) => setValorDoacao(e.target.value)}
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
                    <Button variant="primary" onClick={registrarDoacao}>
                        Registrar
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default GerenciarCaixa;
