import React, { useState } from "react";
import {Button, Card, Col, Row, Table, Modal, Form} from "react-bootstrap";
import { Link, Navigate } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoIosCash } from "react-icons/io";
import { ToastContainer, toast } from 'react-toastify';
import { useCaixa, useCaixas } from "hooks/useCaixa"; // hook personalizado que busca os caixas

const Caixas = () => {
    const { caixas } = useCaixas();
    const token = localStorage.getItem("site");

    const raw = localStorage.getItem("user");
    const usuarioAtual = raw ? JSON.parse(raw) : null;
    const isAdmin = usuarioAtual?.role === "ADMIN";

    const [showModal, setShowModal] = useState(false);
    const [showModalAbrir, setShowModalAbrir] = useState(false);
    const [caixaSelecionado, setCaixaSelecionado] = useState(null);

    if (!isAdmin) {
        return <Navigate to="/" replace />;
    }

    const handleShowModal = (caixa) => {
        setCaixaSelecionado(caixa);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setCaixaSelecionado(null);
    };

    const handleConfirmDelete = async () => {
        try {
            const response = await fetch(`http://localhost:8080/caixas/excluir/${caixaSelecionado.id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                },
            });

            if (response.ok) {
                toast.success("Caixa excluído com sucesso!");
                setTimeout(() => {
                    window.location.reload();
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error(errorData.message || 'Erro ao excluir caixa.');
            }
        } catch (error) {
            console.error('Erro ao excluir caixa:', error);
            toast.error('Erro na comunicação com o servidor.');
        } finally {
            handleCloseModal();
        }
    };

    const handleAbrirCaixa = async (dados) => {
        const token = localStorage.getItem("site");

        try {
            const response = await fetch("http://localhost:8080/caixas/abrir", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify({
                    valorInicial: dados.valorInicial,
                    dataAberturaCaixa: dados.dataAberturaCaixa,
                    funId: 1,
                })
            });

            if (response.ok) {
                const result = await response.json();
                console.log("Caixa aberto com sucesso:", result);
                // Exibir um toast ou mensagem de sucesso
            } else {
                const error = await response.json();
                console.error("Erro ao abrir caixa:", error);
                // Exibir toast de erro
            }
        } catch (err) {
            console.error("Erro na comunicação com o servidor:", err);
            // Exibir erro genérico
        }
    };

    return (
        <React.Fragment>
            <ToastContainer position="top-right" autoClose={3000} />
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <div style={{ width: '100%', display: 'flex', justifyContent: 'space-between' }}>
                                <Card.Title as="h5">Caixas</Card.Title>
                                <Link to="/caixas/novo">
                                    <Button variant="primary" onClick={() => setShowModalAbrir(true)}><IoIosCash /> Abrir</Button>
                                </Link>
                            </div>
                        </Card.Header>

                        <Card.Body>
                            <Table responsive hover>
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Data Abertura</th>
                                    <th>Valor Inicial</th>
                                    {/*<th>Responsável</th>*/}
                                    <th>Ações</th>
                                </tr>
                                </thead>
                                <tbody>
                                {caixas && caixas.map((caixa) => (
                                    <tr key={caixa.id}>
                                        <th scope="row">{caixa.id}</th>
                                        <td>{caixa.dataAberturaCaixa && new Date(caixa.dataAberturaCaixa).toLocaleString("pt-BR", {day: "2-digit", month: "2-digit", year: "numeric"})}</td>
                                        <td>R$ {caixa.valorInicial.toFixed(2)}</td>
                                        {/*<td>{caixa.nomeUsuario}</td>*/}
                                        <td>
                                            <Link to={`/caixa/gerenciar/${caixa.id}`}>
                                                <Button size="sm" className="label theme-bg text-white f-12">
                                                    <FaEdit />
                                                </Button>
                                            </Link>{' '}
                                            <Button
                                                size="sm"
                                                className="label theme-bg2 text-white f-12"
                                                onClick={() => handleShowModal(caixa)}
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

            <DeleteConfirmationModal
                show={showModal}
                onHide={handleCloseModal}
                onConfirm={handleConfirmDelete}
                caixa={caixaSelecionado}
            />

            <ModalAbrirCaixa
                show={showModalAbrir}
                onHide={() => setShowModalAbrir(false)}
                onSubmit={handleAbrirCaixa}
            />
        </React.Fragment>
    );
};

export default Caixas;

const DeleteConfirmationModal = ({ show, onHide, onConfirm, caixa }) => {
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>Confirmar Exclusão</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                Tem certeza que deseja excluir o caixa com abertura em <strong>{caixa?.dtAberturaCaixa && new Date(caixa.dtAberturaCaixa).toLocaleString()}</strong>?
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
};


const ModalAbrirCaixa = ({ show, onHide, onSubmit }) => {
    const [valorInicial, setValorInicial] = useState('');
    const [dataAberturaCaixa, setDataAberturaCaixa] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        onSubmit({
            valorInicial: parseFloat(valorInicial),
            dataAberturaCaixa: dataAberturaCaixa
        });

        // Limpar e fechar modal
        setValorInicial('');
        setDataAberturaCaixa('');
        onHide();
    };

    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>Abrir Caixa</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    <Form.Group controlId="valorInicial" className="mb-3">
                        <Form.Label>Valor Inicial</Form.Label>
                        <Form.Control
                            type="number"
                            step="0.01"
                            value={valorInicial}
                            onChange={(e) => setValorInicial(e.target.value)}
                            required
                        />
                    </Form.Group>
                    <Form.Group controlId="dataAberturaCaixa" className="mb-3">
                        <Form.Label>Data de Abertura</Form.Label>
                        <Form.Control
                            type="datetime-local"
                            value={dataAberturaCaixa}
                            onChange={(e) => setDataAberturaCaixa(e.target.value)}
                            required
                        />
                    </Form.Group>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={onHide}>
                            Cancelar
                        </Button>
                        <Button variant="primary" type="submit">
                            Abrir Caixa
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal.Body>
        </Modal>
    );
};