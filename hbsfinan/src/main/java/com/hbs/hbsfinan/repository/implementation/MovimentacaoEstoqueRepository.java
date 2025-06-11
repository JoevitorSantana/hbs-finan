package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.model.MovimentacaoEstoque;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.enums.TipoMovimentacao;
import com.hbs.hbsfinan.repository.interfaces.IMovimentacaoEstoqueRepository;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovimentacaoEstoqueRepository implements IMovimentacaoEstoqueRepository {
    private Conexao dbConn;

    public MovimentacaoEstoqueRepository(Conexao dbConn) {
        if (dbConn == null) {
            throw new IllegalArgumentException("Instância de Conexao não pode ser nula para MovimentacaoEstoqueRepository.");
        }
        this.dbConn = dbConn;
    }

    @Override
    public void save(MovimentacaoEstoque movimentacao) {
        if (this.dbConn == null) {
            throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.save. A instância de Conexao não foi corretamente injetada.");
        }
        String sql = "INSERT INTO movimentacao_estoque (produto_id, funcionario_id, quantidade_movimentada, tipo, data_hora_movimentacao, observacao) " +
                "VALUES (#1, #2, #3, '#4', '#5', #6)";
        sql = sql.replace("#1", String.valueOf(movimentacao.getProduto().getId()));
        sql = sql.replace("#2", String.valueOf(movimentacao.getFuncionario().getId()));
        sql = sql.replace("#3", String.valueOf(movimentacao.getQuantidadeMovimentada()));
        sql = sql.replace("#4", movimentacao.getTipo().name());
        sql = sql.replace("#5", Timestamp.valueOf(movimentacao.getDataHoraMovimentacao()).toString());
        String observacao = movimentacao.getObservacao();
        if (observacao == null || observacao.trim().isEmpty()) {
            sql = sql.replace("#6", "NULL");
        } else {
            observacao = observacao.replace("'", "''");
            sql = sql.replace("#6", "'" + observacao + "'");
        }
        try {
            this.dbConn.update(sql);
        } catch (Exception e) {
            System.err.println("Erro ao salvar movimentação de estoque: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar movimentação de estoque no banco de dados.", e);
        }
    }

    @Override
    public void update(MovimentacaoEstoque movimentacao) {
        if (this.dbConn == null)
            throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.update");
        String sql = "UPDATE movimentacao_estoque SET produto_id = #1, funcionario_id = #2, " +
                "quantidade_movimentada = #3, tipo = '#4', data_hora_movimentacao = '#5', " +
                "observacao = #6 WHERE id = #7";
        sql = sql.replace("#1", String.valueOf(movimentacao.getProduto().getId()));
        sql = sql.replace("#2", String.valueOf(movimentacao.getFuncionario().getId()));
        sql = sql.replace("#3", String.valueOf(movimentacao.getQuantidadeMovimentada()));
        sql = sql.replace("#4", movimentacao.getTipo().name());
        sql = sql.replace("#5", Timestamp.valueOf(movimentacao.getDataHoraMovimentacao()).toString());
        String observacao = movimentacao.getObservacao();
        if (observacao == null || observacao.trim().isEmpty()) {
            sql = sql.replace("#6", "NULL");
        } else {
            observacao = observacao.replace("'", "''");
            sql = sql.replace("#6", "'" + observacao + "'");
        }
        sql = sql.replace("#7", String.valueOf(movimentacao.getId()));
        try {
            this.dbConn.update(sql);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar movimentação de estoque: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar movimentação de estoque no banco de dados.", e);
        }
    }

    @Override
    public boolean delete(long id) {
        if (this.dbConn == null) throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.delete");
        String sql = "DELETE FROM movimentacao_estoque WHERE id = #1";
        sql = sql.replace("#1", String.valueOf(id));

        try {
            return this.dbConn.update(sql);
        } catch (Exception e) {
            System.err.println("Erro ao deletar movimentação de estoque: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar movimentação de estoque no banco de dados.", e);
        }
    }

    @Override
    public MovimentacaoEstoque findById(long id) {
        if (this.dbConn == null) {
            System.err.println("MovimentacaoEstoqueRepository.findById: dbConn é nulo!");
            throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.findById.");
        }
        MovimentacaoEstoque movimentacao = null;
        String sql = "SELECT me.id AS mov_id, me.quantidade_movimentada, me.tipo, me.data_hora_movimentacao, me.observacao, " +
                "p.id AS prod_id, p.nome AS prod_nome, p.qtd AS prod_qtd, p.data_validade AS prod_data_validade, " +
                "f.id AS func_id, f.nome AS func_nome, f.email AS func_email " +
                "FROM movimentacao_estoque me " +
                "JOIN produtos p ON p.id = me.produto_id " +
                "JOIN funcionario f ON f.id = me.funcionario_id " +
                "WHERE me.id = " + id;

        try (ResultSet rs = this.dbConn.query(sql)) {
            if (rs != null && rs.next()) {
                movimentacao = mapRowToMovimentacaoEstoque(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar movimentação de estoque por ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar movimentação de estoque por ID.", e);
        }
        return movimentacao;
    }

    @Override
    public List<MovimentacaoEstoque> findAll() {
        if (this.dbConn == null) {
            System.err.println("MovimentacaoEstoqueRepository.findAll: dbConn é nulo!");
            throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.findAll.");
        }
        List<MovimentacaoEstoque> lista = new ArrayList<>();
        String sql = "SELECT me.id AS mov_id, me.quantidade_movimentada, me.tipo, me.data_hora_movimentacao, me.observacao, " +
                "p.id AS prod_id, p.nome AS prod_nome, p.qtd AS prod_qtd, p.data_validade AS prod_data_validade, " +
                "f.id AS func_id, f.nome AS func_nome, f.email AS func_email " +
                "FROM movimentacao_estoque me " +
                "JOIN produtos p ON p.id = me.produto_id " +
                "JOIN funcionario f ON f.id = me.funcionario_id " +
                "ORDER BY me.data_hora_movimentacao DESC";

        try (ResultSet rs = this.dbConn.query(sql)) {
            if (rs != null) {
                while (rs.next()) {
                    lista.add(mapRowToMovimentacaoEstoque(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as movimentações de estoque: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todas as movimentações de estoque.", e);
        }
        return lista;
    }

    private MovimentacaoEstoque mapRowToMovimentacaoEstoque(ResultSet rs) throws SQLException {
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setId(rs.getLong("mov_id"));
        mov.setQuantidadeMovimentada(rs.getLong("quantidade_movimentada"));
        mov.setTipo(TipoMovimentacao.valueOf(rs.getString("tipo")));

        Timestamp dataHoraTimestamp = rs.getTimestamp("data_hora_movimentacao");
        if (dataHoraTimestamp != null) {
            mov.setDataHoraMovimentacao(dataHoraTimestamp.toLocalDateTime());
        }

        mov.setObservacao(rs.getString("observacao"));
        Produtos produto = new Produtos();
        produto.setId(rs.getInt("prod_id"));
        produto.setNome(rs.getString("prod_nome"));
        produto.setQtd(rs.getLong("prod_qtd"));
        Timestamp dataValidadeTimestamp = rs.getTimestamp("prod_data_validade");
        if (dataValidadeTimestamp != null) {
            produto.setDataValidade(dataValidadeTimestamp.toLocalDateTime().toLocalDate());
        } else {
            produto.setDataValidade(null);
        }
        mov.setProduto(produto);
        Funcionario funcionario = new Funcionario();
        funcionario.setId(rs.getInt("func_id"));
        funcionario.setNome(rs.getString("func_nome"));
        funcionario.setEmail(rs.getString("func_email"));
        mov.setFuncionario(funcionario);
        return mov;
    }
}