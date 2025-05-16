import { useProdutos } from "hooks/useProdutos";
import React from "react";
import { Button, Card, Col, Row, Table } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaEdit, FaRegTrashAlt } from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";

const Produtos = () => {
    const { produtos } = useProdutos();
    const token = localStorage.getItem("site");

    const handleDeleteProduto = async (id) => {
        const confirm = window.confirm("Tem certeza que deseja excluir este produto?");
        if (!confirm) return;

        try {
            await fetch(`http://localhost:8080/produtos/excluir/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type' : 'application/json',
                    'Authorization' : 'Bearer ' + token
                }
            });
            alert("Produto excluído com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error("Erro ao excluir produto:", error);
            alert("Erro ao excluir produto.");
        }
    };

    return (
        <React.Fragment>
            <Row>
                <Col sm={12}>
                    <Card>
                        <Card.Header>
                            <div style={{ width: '100%', display: 'flex', justifyContent: 'space-between' }}>
                                <Card.Title as="h5">Produtos</Card.Title>
                                <Link to="/produtos/novo">
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
                                        <th>Quantidade</th>
                                        <th>Ações</th>

                                    </tr>
                                </thead>
                                <tbody>
                                    {produtos && produtos.map((produto) => (
                                        <tr key={produto.id}>
                                            <th scope="row">{produto.id}</th>
                                            <td>{produto.nome}</td>
                                            <td>{produto.qtd}</td>
                                            <td style={{ justifyContent: 'center' }} >
                                                <Link to={ "/produtos/editar/" + produto.id }>
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
                                                    onClick={() => handleDeleteProduto(produto.id)}
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

export default Produtos;