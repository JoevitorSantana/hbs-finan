import { useGrupos } from "hooks/useGrupos";
import React from "react";
import { Button, Card, Col, Row, Table } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";

const Grupos = () => {
    const { grupos } = useGrupos();
    const token = localStorage.getItem("site");

    const handleDeleteGrupo = async (id) => {
        const confirm = window.confirm("Tem certeza que deseja excluir este grupo?");
        if (!confirm) return;

        try {
            await fetch(`http://localhost:8080/grupos/excluir/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type' : 'application/json',
                    'Authorization' : 'Bearer ' + token
                }
            });
            alert("Grupo excluído com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error("Erro ao excluir grupo:", error);
            alert("Erro ao excluir grupo.");
        }
    };

    return (
        <React.Fragment>
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <div style={{ width: '100%', display: 'flex', justifyContent: 'space-between' }}>
                                <Card.Title as="h5">Grupos</Card.Title>
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
                                    {grupos && grupos.map((grupo) => (
                                        <tr key={grupo.id}>
                                            <th scope="row">{grupo.id}</th>
                                            <td>{grupo.nome}</td>
                                            <td style={{ justifyContent: 'center' }} >
                                                <Link to={ "/grupos/editar/" + grupo.id }>
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
                                                    onClick={() => handleDeleteGrupo(grupo.id)}
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

export default Grupos;