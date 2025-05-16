import { useUsuarios } from "hooks/useUsers";
import React from "react";
import { Button, Card, Col, Row, Table } from "react-bootstrap";
import { Link, Navigate } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";



const Usuarios = () => {
    const { usuarios } = useUsuarios();
    const token = localStorage.getItem("site");

    const raw = localStorage.getItem("user");
    const usuarioAtual = raw ? JSON.parse(raw) : null;
    const isAdmin = usuarioAtual?.role === "ADMIN";

    if (!isAdmin) {
    return <Navigate to="/" replace />; // redireciona para o dashboard
    }

    const handleDeleteUsuario = async (id) => {
        const confirm = window.confirm("Tem certeza que deseja excluir este usuário?");
        if (!confirm) return;

        try {
            await fetch(`http://localhost:8080/usuarios/excluir/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type' : 'application/json',
                    'Authorization' : 'Bearer ' + token
                }
            });
            alert("Usuário excluído com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error("Erro ao excluir usuário:", error);
            alert("Erro ao excluir usuário.");
        }
    };
    

    return (
        <React.Fragment>
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
                                                    onClick={() => handleDeleteUsuario(usuario.id)}
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
        </React.Fragment>
    );
}

export default Usuarios;