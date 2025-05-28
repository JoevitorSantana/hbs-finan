import { useGrupos } from "hooks/useGrupos";
import React, { useState } from "react";
import { Button, Card, Col, Row, Table, Modal, Form } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";
import { ToastContainer, toast } from 'react-toastify';

const Grupos = () => {
    const { grupos } = useGrupos();
    const token = localStorage.getItem("site");

    const [showModal, setShowModal] = useState(false);
    const [grupoSelecionado, setGrupoSelecionado] = useState(null);
    const [filtroNome, setFiltroNome] = useState(''); // <-- Novo estado para o filtro

    const handleShowModal = (grupo) => {
        setGrupoSelecionado(grupo);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setGrupoSelecionado(null);
    };

    const handleDeleteGrupo = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/grupos/excluir/${grupoSelecionado.id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                },
            });

            if (response.ok) {
                toast.success("Grupo excluído com sucesso!");
                setTimeout(() => {
                    window.location.reload();
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error(errorData.message || 'Erro ao excluir grupo.');
            }
        } catch (error) {
            console.error('Erro ao excluir grupo:', error);
            toast.error('Erro na comunicação com o servidor.');
        } finally {
            handleCloseModal();
        }
    };

    // Filtro por nome
    const gruposFiltrados = grupos?.filter(grupo =>
        grupo.nome.toLowerCase().includes(filtroNome.toLowerCase())
    );

    return (
        <React.Fragment>
            <ToastContainer position="top-right" autoClose={3000} />
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <div style={{ width: '100%', display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: 10 }}>
                                <Card.Title as="h5" style={{ margin: 0 }}>Grupos</Card.Title>
                                <Form.Control
                                    type="text"
                                    placeholder="Buscar por nome..."
                                    value={filtroNome}
                                    onChange={(e) => setFiltroNome(e.target.value)}
                                    style={{ maxWidth: 300 }}
                                />
                                <Link to="/grupos/novo">
                                    <Button variant="primary"><IoPersonAdd /> Novo</Button>
                                </Link>
                            </div>
                        </Card.Header>
                        <Card.Body>
                            <Table responsive hover>
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Nome</th>
                                        <th>Ações</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {gruposFiltrados && gruposFiltrados.map((grupo) => (
                                        <tr key={grupo.id}>
                                            <th scope="row">{grupo.id}</th>
                                            <td>{grupo.nome}</td>
                                            <td style={{ justifyContent: 'center' }} >
                                                <Link to={"/grupos/editar/" + grupo.id}>
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
                                                    onClick={() => handleShowModal(grupo)}
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
                onConfirm={handleDeleteGrupo}
                grupo={grupoSelecionado}
            />
        </React.Fragment>
    );
};

export default Grupos;

const DeleteConfirmationModal = ({ show, onHide, onConfirm, grupo }) => {
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>Confirmar Exclusão</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                Tem certeza que deseja excluir o grupo <strong>{grupo?.nome}</strong>?
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
