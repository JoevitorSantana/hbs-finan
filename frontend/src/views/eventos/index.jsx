import { useEventos } from "hooks/useEventos";
import React, { useState } from "react";
import { Button, Card, Col, Row, Table, Modal } from "react-bootstrap";
import { Link, Navigate } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";
import { ToastContainer, toast } from 'react-toastify';

const Eventos = () => {
    const { eventos } = useEventos();
    const token = localStorage.getItem("site");

    const [showModal, setShowModal] = useState(false);
    const [eventoSelecionado, setEventoSelecionado] = useState(null);
    
    const handleShowModal = (evento) => {
        setEventoSelecionado(evento);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setEventoSelecionado(null);
    };

    const handleDeleteEvento = async (id) => {
     try {
        const response = await fetch(`http://localhost:8080/eventos/excluir/${eventoSelecionado.id}`, {
        method: 'DELETE',
            headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                },
            });
     
            if (response.ok) {
                toast.success("Evento excluído com sucesso!");
                setTimeout(() => {
                    window.location.reload();
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error(errorData.message || 'Erro ao excluir evento.');
            }
        } catch (error) {
            console.error('Erro ao excluir evento:', error);
            toast.error('Erro na comunicação com o servidor.');
        } finally {
            handleCloseModal();
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
                                <Card.Title as="h5">Eventos</Card.Title>
                                <Link to="/eventos/novo">
                                    <Button variant="primary"><IoPersonAdd /> Novo</Button>
                                </Link>
                            </div>
                        </Card.Header>
                        <Card.Body>
                            <Table responsive hover>
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Local</th>
                                        <th>Data</th>
                                        <th>Descrição</th>
                                        <th>Nome</th>
                                        <th>Materiais</th>
                                        <th>Funcionario ID</th>
                                        <th>Ações</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {eventos && eventos.map((evento) => (
                                        <tr key={evento.id}>
                                            <th scope="row">{evento.id}</th>
                                            <td>{evento.local}</td>
                                            <td>{evento.data}</td>
                                            <td>{evento.descricao}</td>
                                            <td>{evento.nome}</td>
                                            <td>{evento.materiais}</td>
                                            <td>{evento.func_id}</td>
                                            <td style={{ justifyContent: 'center' }} >
                                                <Link to={ "/eventos/editar/" + evento.id }>
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
                                                    onClick={() => handleShowModal (evento)}
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
                onConfirm={handleDeleteEvento}
                evento={eventoSelecionado}
            />
        </React.Fragment>
    );
}

export default Eventos;

const DeleteConfirmationModal = ({ show, onHide, onConfirm, evento }) => {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Confirmar Exclusão</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        Tem certeza que deseja excluir o evento <strong>{evento?.nome}</strong>?
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