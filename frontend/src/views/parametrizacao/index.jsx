import { useParametros } from "hooks/useParametros";
import React from "react";
import { Button, Card, Col, Row, Table } from "react-bootstrap";
import { Link, Navigate } from "react-router-dom";
import { FaEdit } from "react-icons/fa";

const Parametrizacao = () => {
  const { parametros } = useParametros();
  
  const raw = localStorage.getItem("user");
  const usuarioAtual = raw ? JSON.parse(raw) : null;
  const isAdmin = usuarioAtual?.role === "ADMIN";

  if (!isAdmin) {
    return <Navigate to="/" replace />; // redireciona para o dashboard
  }

  return (
    <Row>
      <Col sm={12}>
        <Card>
          <Card.Header>
            <div style={{width: "100%", display: "flex", justifyContent: "space-between"}}>
              <Card.Title as="h5">Parametrização</Card.Title>
                  <Link to="/parametrizacao/editar">
                      <Button variant="primary"><FaEdit /> Editar</Button>
                  </Link>
            </div>
          </Card.Header>

          <Card.Body>
            {parametros && (
              <Table responsive hover>
                <tbody>
                  <tr>
                    <th>ID</th>
                    <td>{parametros.id}</td>
                  </tr>

                  <tr>
                    <th>Nome da Empresa</th>
                    <td>{parametros.nomeEmpresa}</td>
                  </tr>

                  <tr>
                    <th>Razão Social</th>
                    <td>{parametros.razaoSocial}</td>
                  </tr>

                  <tr>
                    <th>Endereço</th>
                    <td>
                      {parametros.enderecoRua}, {parametros.enderecoBairro},
                      {" "}
                      {parametros.enderecoCidade} - {parametros.enderecoEstado}
                      {" "}
                      ({parametros.enderecoCep})
                    </td>
                  </tr>

                  <tr>
                    <th>Email</th>
                    <td>{parametros.email}</td>
                  </tr>

                  <tr>
                    <th>Telefone</th>
                    <td>{parametros.telefone}</td>
                  </tr>

                  <tr>
                    <th>Celular</th>
                    <td>{parametros.celular}</td>
                  </tr>

                  <tr>
                    <th>Nome do Proprietário</th>
                    <td>{parametros.nomeProprietario}</td>
                  </tr>

                  <tr>
                    <th>CNPJ</th>
                    <td>{parametros.cnpj}</td>
                  </tr>

                  <tr>
                    <th>Logo Pequena (URL)</th>
                    <td>{parametros.logoPequenaUrl}</td>
                  </tr>

                  <tr>
                    <th>Logo Grande (URL)</th>
                    <td>{parametros.logoGrandeUrl}</td>
                  </tr>
                </tbody>
              </Table>
            )}
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};

export default Parametrizacao;



