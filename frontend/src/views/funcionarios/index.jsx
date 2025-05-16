
import React from "react";
import { Button, Card, Col, Row, Table } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";
import { useFuncionarios } from "hooks/useFuncionario";


const Funcionarios = () => {
    const { funcionarios } = useFuncionarios();
    const token = localStorage.getItem("site");

    const handleDeleteUsuario = async (id) => {
        const confirm = window.confirm("Tem certeza que deseja excluir este funcionário?");
        if (!confirm) return;

        try {
            await fetch(`http://localhost:8080/funcionarios/excluir/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type' : 'application/json',
                    'Authorization' : 'Bearer ' + token
                }
            });
            alert("Funcionário excluído com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error("Erro ao excluir Funcionário:", error);
            alert("Erro ao excluir Funcionário.");
        }
    };

    return (
        <React.Fragment>
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <div style={{ width: '100%', display: 'flex', justifyContent: 'space-between' }}>
                                <Card.Title as="h5">Funcionários</Card.Title>
                                <Link to="/funcionarios/novo">
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
                                    <th>Telefone</th>
                                    <th>Endereço</th>
                                    <th>Data Nascimento</th>
                                    <th>Sexo</th>
                                    <th>CPF</th>
                                    
                                    
                                </tr>
                            </thead>
                            <tbody>
                                {funcionarios && funcionarios.map((funcionario) => (
                                    <tr key={funcionario.id}>
                                        <th scope="row">{funcionario.id}</th>
                                        <td>{funcionario.nome}</td>
                                        <td>{funcionario.email}</td>
                                        <td>{funcionario.fone}</td>
                                        <td>{funcionario.endereco}</td>
                                        <td>{funcionario.data_nascimento}</td>
                                        <td>{funcionario.sexo}</td>
                                        <td>{funcionario.cpf}</td>
                                        
                                        <td>
                                            <Link to={`/funcionarios/editar/${funcionario.id}`}>
                                                <Button size="sm" className="label theme-bg text-white f-12">
                                                    <FaEdit />
                                                </Button>
                                            </Link>
                                            <Button
                                                size="sm"
                                                className="label theme-bg2 text-white f-12"
                                                onClick={() => handleDeleteUsuario(funcionario.id)}
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

export default Funcionarios;