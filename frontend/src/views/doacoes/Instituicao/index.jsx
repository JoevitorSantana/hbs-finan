import { useDoacoesInstituicao } from "hooks/useDoacoesInstituicao";
import React, { useState } from "react";
import { Button, Card, Col, Row, Table, Modal, Form } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";
import { ToastContainer, toast } from 'react-toastify';

const DoacoesInstituicao = () => {
    const { doacoes } = useDoacoesInstituicao();  // novo hook
    const token = localStorage.getItem("site");

    const [showModal, setShowModal] = useState(false);
    const [doacaoSelecionada, setDoacaoSelecionada] = useState(null);
    const [searchTerm, setSearchTerm] = useState("");

    const handleShowModal = (doacao) => {
        setDoacaoSelecionada(doacao);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setDoacaoSelecionada(null);
    };

    const handleDeleteDoacao = async () => {
        try {
            const response = await fetch(`http://localhost:8080/doacao-instituicao/excluir/${doacaoSelecionada.id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                },
            });

            if (response.ok) {
                toast.success("Doação excluída com sucesso!");
                setTimeout(() => window.location.reload(), 2000);
            } else {
                const errorData = await response.json();
                toast.error(errorData.message || 'Erro ao excluir doação.');
            }
        } catch (error) {
            console.error('Erro ao excluir doação:', error);
            toast.error('Erro na comunicação com o servidor.');
        } finally {
            handleCloseModal();
        }
    };

    const doacoesFiltradas = doacoes?.filter(doacao => {
        const termo = searchTerm.toLowerCase();
        return (
            doacao.id.toString().includes(termo) ||
            doacao.data?.toLowerCase().includes(termo) ||
            doacao.valor.toString().includes(termo) ||
            doacao.instituicao?.nome?.toLowerCase().includes(termo)
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
                                <Card.Title as="h5">Doações para Instituições</Card.Title>
                                <Link to="/doacao/instituicao/novo">
                                    <Button variant="primary"><IoPersonAdd /> Nova</Button>
                                </Link>
                            </div>
                            <Form.Control
                                type="text"
                                placeholder="Buscar por instituição ou valor..."
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
                                        <th>Instituição</th>
                                        <th>Valor</th>
                                        <th>Data</th>
                                        {/* <th>Descrição</th> */}
                                        <th>Ações</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {doacoesFiltradas && doacoesFiltradas.map((doacao) => (
                                        <tr key={doacao.id}>
                                            <td>{doacao.id}</td>
                                            <td>{doacao.nome}</td>
                                            <td>R$ {parseFloat(doacao.valor).toFixed(2)}</td>
                                            <td>{doacao.data}</td>
                                            {/* <td>{doacao.descricao}</td> */}
                                            <td>
                                                <Link to={`/doacao/instituicao/editar/${doacao.id}`}>
                                                    <Button size="sm" className="label theme-bg text-white f-12 me-1">
                                                        <FaEdit />
                                                    </Button>
                                                </Link>
                                                <Button
                                                    size="sm"
                                                    className="label theme-bg2 text-white f-12"
                                                    onClick={() => handleShowModal(doacao)}
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
                onConfirm={handleDeleteDoacao}
                doacao={doacaoSelecionada}
            />
        </React.Fragment>
    );
};

export default DoacoesInstituicao;

const DeleteConfirmationModal = ({ show, onHide, onConfirm, doacao }) => (
    <Modal show={show} onHide={onHide} centered>
        <Modal.Header closeButton>
            <Modal.Title>Confirmar Exclusão</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            Tem certeza que deseja excluir a doação para <strong>{doacao?.instituicao?.nome}</strong> no valor de <strong>R$ {doacao?.valor?.toFixed(2)}</strong>?
        </Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={onHide}>Cancelar</Button>
            <Button variant="danger" onClick={onConfirm}>Excluir</Button>
        </Modal.Footer>
    </Modal>
);