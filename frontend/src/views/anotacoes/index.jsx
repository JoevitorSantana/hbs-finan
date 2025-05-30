import { useAnotacoes } from "hooks/useAnotacoes";
import { useEventos } from "hooks/useEventos";
import React, { useState } from "react";
import { Button, Card, Col, Row, Table, Modal, Form } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";
import { ToastContainer, toast } from 'react-toastify';

const Anotacoes = () => {
    const { anotacoes } = useAnotacoes();
    const { eventos } = useEventos();
    const token = localStorage.getItem("site");

    const [showModal, setShowModal] = useState(false);
    const [anotacaoSelecionada, setAnotacaoSelecionada] = useState(null);
    const [searchTerm, setSearchTerm] = useState("");

    const handleShowModal = (anotacao) => {
        setAnotacaoSelecionada(anotacao);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setAnotacaoSelecionada(null);
    };

    const handleDeleteAnotacao = async () => {
        try {
            const response = await fetch(`http://localhost:8080/anotacoes/excluir/${anotacaoSelecionada.id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                },
            });

            if (response.ok) {
                toast.success("Anotação excluída com sucesso!");
                setTimeout(() => {
                    window.location.reload();
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error(errorData.message || 'Erro ao excluir anotação.');
            }
        } catch (error) {
            console.error('Erro ao excluir anotação:', error);
            toast.error('Erro na comunicação com o servidor.');
        } finally {
            handleCloseModal();
        }
    };

    // 🔗 Função que retorna o nome do evento baseado no even_id
    const getNomeEvento = (even_id) => {
        const evento = eventos?.find(e => e.id === even_id);
        return evento ? evento.nome : `ID ${even_id}`;
    };

    const anotacoesFiltradas = anotacoes?.filter(anotacao => {
        const termo = searchTerm.toLowerCase();
        return (
            anotacao.anotacao?.toLowerCase().includes(termo) ||
            anotacao.data?.toLowerCase().includes(termo) ||
            getNomeEvento(anotacao.even_id)?.toLowerCase().includes(termo)
        );
    });

    return (
        <React.Fragment>
            <ToastContainer position="top-right" autoClose={3000} />
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <div style={{ width: '100%', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <Card.Title as="h5">Anotações</Card.Title>
                                <Link to="/anotacoes/novo">
                                    <Button variant="primary"><IoPersonAdd /> Nova</Button>
                                </Link>
                            </div>
                            <Form.Control
                                type="text"
                                placeholder="Buscar por anotação, data ou nome do evento..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className="mt-3"
                            />
                        </Card.Header>
                        <Card.Body>
                            <Table responsive hover>
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Anotação</th>
                                        <th>Data</th>
                                        <th>Evento</th>
                                        <th>Ações</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {anotacoesFiltradas && anotacoesFiltradas.map((anotacao) => (
                                        <tr key={anotacao.id}>
                                            <th scope="row">{anotacao.id}</th>
                                            <td>{anotacao.anotacao}</td>
                                            <td>{anotacao.data}</td>
                                            <td>{getNomeEvento(anotacao.even_id)}</td>
                                            <td>
                                                <Link to={`/anotacoes/editar/${anotacao.id}`}>
                                                    <Button
                                                        size="sm"
                                                        className="label theme-bg text-white f-12 me-1"
                                                    >
                                                        <FaEdit />
                                                    </Button>
                                                </Link>
                                                <Button
                                                    size="sm"
                                                    className="label theme-bg2 text-white f-12"
                                                    onClick={() => handleShowModal(anotacao)}
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
                onConfirm={handleDeleteAnotacao}
                anotacao={anotacaoSelecionada}
            />
        </React.Fragment>
    );
};

export default Anotacoes;

const DeleteConfirmationModal = ({ show, onHide, onConfirm, anotacao }) => (
    <Modal show={show} onHide={onHide} centered>
        <Modal.Header closeButton>
            <Modal.Title>Confirmar Exclusão</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            Tem certeza que deseja excluir a anotação <strong>{anotacao?.anotacao}</strong>?
        </Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={onHide}>Cancelar</Button>
            <Button variant="danger" onClick={onConfirm}>Excluir</Button>
        </Modal.Footer>
    </Modal>
);
