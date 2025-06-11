package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.model.DoacaoProduto;
import com.hbs.hbsfinan.model.ItemDoacao;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoAlimenticia;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoacaoProdutoRepository implements IDoacaoAlimenticia {

    private final Conexao dbConn;

    public DoacaoProdutoRepository() {
        this.dbConn = Conexao.getInstance();
        if (this.dbConn == null || !this.dbConn.getEstadoConexao()) {
            throw new RuntimeException("Não foi possível obter a conexão com o banco de dados.");
        }
    }


    @Override
    public void save(DoacaoProduto doacao) {
        // 1. Salvar doação e obter o ID gerado
        String sqlDoacao = "INSERT INTO doacao_produto (funcionario_id, data) VALUES (?, ?)";
        try (Connection conn = dbConn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlDoacao, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, doacao.getFuncionario().getId());
            if (doacao.getData() != null)
                ps.setDate(2, java.sql.Date.valueOf(doacao.getData()));
            else
                ps.setNull(2, Types.DATE);

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                Long doacaoId = keys.getLong(1);
                doacao.setId(doacaoId);

                // 2. Salvar os itens da doação
                String sqlItem = "INSERT INTO item_doacao (doacao_produto_id, produto_id, quantidade, data_validade) VALUES (?, ?, ?, ?)";
                try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                    for (ItemDoacao item : doacao.getItens()) {
                        psItem.setLong(1, doacaoId);
                        psItem.setLong(2, item.getProduto().getId());
                        psItem.setLong(3, item.getQuantidade());

                        if (item.getDataValidade() != null)
                            psItem.setDate(4, java.sql.Date.valueOf(item.getDataValidade()));
                        else
                            psItem.setNull(4, Types.DATE);

                        psItem.addBatch();
                    }
                    psItem.executeBatch();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar doação com itens", e);
        }
    }


    @Override
    public DoacaoProduto findById(int id) {
        DoacaoProduto doacao = null;
        String sql = "SELECT * FROM doacao_produto WHERE id = " + id;

        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                doacao = new DoacaoProduto();
                doacao.setId(rs.getLong("id"));

                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("funcionario_id"));
                doacao.setFuncionario(funcionario);

                if (rs.getDate("data") != null) {
                    doacao.setData(rs.getDate("data").toLocalDate());
                }

                // Carrega os itens da doação
                doacao.setItens(carregarItensDoacao(doacao.getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doacao;
    }

    @Override
    public List<DoacaoProduto> findAll() {
        List<DoacaoProduto> lista = new ArrayList<>();
        String sql = "SELECT * FROM doacao_produto";

        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                DoacaoProduto doacao = new DoacaoProduto();
                doacao.setId(rs.getLong("id"));

                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("funcionario_id"));
                doacao.setFuncionario(funcionario);

                if (rs.getDate("data") != null) {
                    doacao.setData(rs.getDate("data").toLocalDate());
                }

                // Carrega os itens da doação, com nome do produto
                doacao.setItens(carregarItensDoacao(doacao.getId()));

                lista.add(doacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    private List<ItemDoacao> carregarItensDoacao(Long doacaoProdutoId) {
        List<ItemDoacao> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_doacao WHERE doacao_produto_id = " + doacaoProdutoId;

        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                ItemDoacao item = new ItemDoacao();
                item.setQuantidade(rs.getLong("quantidade"));

                int produtoId = rs.getInt("produto_id");
                Produtos produto = new Produtos();
                produto.setId(produtoId);

                // Buscar nome do produto
                String sqlProduto = "SELECT nome FROM produtos WHERE id = " + produtoId;
                try (ResultSet rsProduto = dbConn.query(sqlProduto)) {
                    if (rsProduto.next()) {
                        produto.setNome(rsProduto.getString("nome"));
                    }
                }

                item.setProduto(produto);

                // Define o relacionamento com a doação
                DoacaoProduto doacao = new DoacaoProduto();
                doacao.setId(doacaoProdutoId);
                item.setDoacaoProduto(doacao);

                if (rs.getDate("data_validade") != null) {
                    item.setDataValidade(rs.getDate("data_validade").toLocalDate());
                }

                itens.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itens;
    }


    @Override
    public void delete(int id) {
        String sqlDeleteItens = "DELETE FROM item_doacao WHERE doacao_produto_id = ?";
        String sqlDeleteDoacao = "DELETE FROM doacao_produto WHERE id = ?";

        try (Connection conn = dbConn.getConnection()) {
            conn.setAutoCommit(false);  // começa a transação

            try (PreparedStatement psItens = conn.prepareStatement(sqlDeleteItens);
                 PreparedStatement psDoacao = conn.prepareStatement(sqlDeleteDoacao)) {

                // Deleta os itens
                psItens.setInt(1, id);
                psItens.executeUpdate();

                // Deleta a doação
                psDoacao.setInt(1, id);
                psDoacao.executeUpdate();

                conn.commit(); // confirma a transação
            } catch (SQLException e) {
                conn.rollback(); // desfaz em caso de erro
                throw e;
            } finally {
                conn.setAutoCommit(true); // volta ao padrão
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar doação", e);
        }
    }


}
