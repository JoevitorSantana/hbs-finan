import { useUsuarios } from "hooks/useUsers";
import React, { useState } from "react";
import { Button, Card, Col, Row, Table, Modal } from "react-bootstrap";
import { Link, Navigate } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";
import { ToastContainer, toast } from 'react-toastify';

const Usuarios = () => {
    const { usuarios } = useUsuarios();
    const token = localStorage.getItem("site");

    const raw = localStorage.getItem("user");
    const usuarioAtual = raw ? JSON.parse(raw) : null;
    const isAdmin = usuarioAtual?.role === "ADMIN";

    const [showModal, setShowModal] = useState(false);
    const [usuarioSelecionado, setUsuarioSelecionado] = useState(null);

    if (!isAdmin) {
        return <Navigate to="/" replace />; // redireciona para o dashboard
    }

    const handleShowModal = (usuario) => {
        setUsuarioSelecionado(usuario);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setUsuarioSelecionado(null);
    };

    const handleConfirmDelete = async () => {
        try {
            const response = await fetch(`http://localhost:8080/usuarios/excluir/${usuarioSelecionado.id}`, {
            method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                },
            });

            if (response.ok) {
                toast.success("Usuário excluído com sucesso!");
                setTimeout(() => {
                    window.location.reload();
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error(errorData.message || 'Erro ao excluir usuário.');
            }
        } catch (error) {
            console.error('Erro ao excluir usuário:', error);
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
                                <Card.Title as="h5">Usuários</Card.Title>
                                <Link to="/usuarios/novo">
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
                                        <th>Email</th>
                                        <th>Ações</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {usuarios && usuarios.map((usuario) => (
                                        <tr key={usuario.id}>
                                            <th scope="row">{usuario.id}</th>
                                            <td>{usuario.nome + " " + usuario.ultimoNome}</td>
                                            <td>{usuario.email}</td>
                                            <td style={{ justifyContent: 'center' }} >
                                                <Link to={ "/usuarios/editar/" + usuario.id }>
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
                                                    onClick={() => handleShowModal(usuario)}
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
            {/* Modal de Confirmação */}
            <DeleteConfirmationModal
                show={showModal}
                onHide={handleCloseModal}
                onConfirm={handleConfirmDelete}
                user={usuarioSelecionado}
            />
        </React.Fragment>
    );
}

export default Usuarios;

const DeleteConfirmationModal = ({ show, onHide, onConfirm, user }) => {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Confirmar Exclusão</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        Tem certeza que deseja excluir o usuário <strong>{user?.nome} {user?.ultimoNome}</strong>?
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