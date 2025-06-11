import React, { useState } from "react";
import { Button, Card, Col, Row, Table, Form } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaRegTrashAlt } from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import { IoPersonAdd } from "react-icons/io5";
import { useDoacoes, formatarData } from "hooks/useDoacaoPM";

const DoacoesMateriais = () => {
  const { doacoes } = useDoacoes();
  const token = localStorage.getItem("site");

  const [filtroData, setFiltroData] = useState("");
  const [filtroFuncionarioId, setFiltroFuncionarioId] = useState("");

  const handleDeleteDoacao = (id) => {
    toast.info(
      ({ closeToast }) => (
        <div>
          <p>Tem certeza que deseja excluir esta doação?</p>
          <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
            <Button
              size="sm"
              variant="danger"
              onClick={async () => {
                try {
                  const response = await fetch(`http://localhost:8080/doacao/excluir/${id}`, {
                    method: 'DELETE',
                    headers: {
                      'Content-Type': 'application/json',
                      'Authorization': 'Bearer ' + token
                    }
                  });

                  if (response.ok) {
                    toast.success("Doação excluída com sucesso!");
                    setTimeout(() => window.location.reload(), 1500);
                  } else {
                    toast.error("Falha ao excluir a doação.");
                  }
                } catch (error) {
                  console.error("Erro ao excluir doação:", error);
                  toast.error("Erro ao excluir doação.");
                }
                closeToast();
              }}
            >
              Confirmar
            </Button>
            <Button size="sm" variant="secondary" onClick={closeToast}>
              Cancelar
            </Button>
          </div>
        </div>
      ),
      { autoClose: false }
    );
  };

  const doacoesFiltradas = doacoes.filter((doacao) => {
    const dataFormatada = formatarData(doacao.dataHoraMovimentacao || '');
    const funcionarioId = doacao.funcionario?.id ? String(doacao.funcionario.id) : '';

    const filtroDataValido = filtroData === "" || dataFormatada.includes(filtroData);
    const filtroFuncionarioValido = filtroFuncionarioId === "" || funcionarioId.includes(filtroFuncionarioId);

    return filtroDataValido && filtroFuncionarioValido;
  });

  return (
    <>
      <Row>
        <Col sm={12}>
          <Card>
            <Card.Header>
              <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: '10px' }}>
                <Card.Title as="h5">Doações de Materiais</Card.Title>
                <Link to="/doacao/material/novo">
                  <Button variant="primary"><IoPersonAdd /> Nova Doação</Button>
                </Link>
              </div>
              <div style={{ marginTop: '10px', display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
                <Form.Control
                  type="text"
                  placeholder="Filtrar por data (ex: 10/06/2025)"
                  value={filtroData}
                  onChange={(e) => setFiltroData(e.target.value)}
                  style={{ maxWidth: "250px" }}
                />
                <Form.Control
                  type="text"
                  placeholder="Filtrar por ID do Funcionário"
                  value={filtroFuncionarioId}
                  onChange={(e) => setFiltroFuncionarioId(e.target.value)}
                  style={{ maxWidth: "250px" }}
                />
              </div>
            </Card.Header>
            <Card.Body>
              <ToastContainer
                position="top-right"
                autoClose={3000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                pauseOnFocusLoss
                draggable
                pauseOnHover
              />
              <Table responsive hover>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>ID Funcionário</th>
                    <th>Materiais</th>
                    <th>Quantidade</th>
                    <th>Data</th>
                    <th>Observação</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {doacoesFiltradas.length > 0 ? (
                    doacoesFiltradas.map((doacao) => {
                      // Montar string de materiais com nome e quantidade
                      const materiaisTexto = Array.isArray(doacao.produtos) && doacao.produtos.length > 0
                        ? doacao.produtos
                            .map(p => `${p.nome || p.id} (${p.quantidade || '-'})`)
                            .join(', ')
                        : doacao.produto
                          ? `${doacao.produto.nome || doacao.produto.id} (${doacao.quantidadeMovimentada || '-'})`
                          : '—';

                      return (
                        <tr key={doacao.id}>
                          <th scope="row">{doacao.id}</th>
                          <td>{doacao.funcionario?.id || '—'}</td>
                          <td>{materiaisTexto}</td>
                          <td>{doacao.quantidadeMovimentada || 0}</td>
                          <td>{formatarData(doacao.dataHoraMovimentacao || '')}</td>
                          <td>{doacao.observacao || '—'}</td>
                          <td>
                            <Button
                              size="sm"
                              className="label theme-bg2 text-white f-12"
                              title="Excluir"
                              onClick={() => handleDeleteDoacao(doacao.id)}
                            >
                              <FaRegTrashAlt />
                            </Button>
                          </td>
                        </tr>
                      );
                    })
                  ) : (
                    <tr>
                      <td colSpan={7} className="text-center">Nenhuma doação encontrada.</td>
                    </tr>
                  )}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default DoacoesMateriais;
