package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao; // Importe sua classe Conexao
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.model.MovimentacaoEstoque;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.enums.TipoMovimentacao;
import com.hbs.hbsfinan.repository.interfaces.IMovimentacaoEstoqueRepository;
import org.springframework.stereotype.Repository; // Opcional se instanciado manualmente, mas bom para consistência

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
                "VALUES (#PRODUTO_ID, #FUNCIONARIO_ID, #QTD_MOV, '#TIPO', '#DATA_HORA', '#OBSERVACAO')";
        sql = sql.replace("#PRODUTO_ID", String.valueOf(movimentacao.getProduto().getId()));
        sql = sql.replace("#FUNCIONARIO_ID", String.valueOf(movimentacao.getFuncionario().getId()));
        sql = sql.replace("#QTD_MOV", String.valueOf(movimentacao.getQuantidadeMovimentada()));
        sql = sql.replace("#TIPO", movimentacao.getTipo().name());
        sql = sql.replace("#DATA_HORA", Timestamp.valueOf(movimentacao.getDataHoraMovimentacao()).toString());

        String observacao = movimentacao.getObservacao();
        if (observacao == null || observacao.trim().isEmpty()) {
            sql = sql.replace("'#OBSERVACAO'", "NULL");
        } else {
            observacao = observacao.replace("'", "''");
            sql = sql.replace("#OBSERVACAO", observacao);
        }
        this.dbConn.update(sql);
    }

    @Override
    public void update(MovimentacaoEstoque movimentacao) {
        if (this.dbConn == null)
            throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.update");

        String sql = "UPDATE movimentacao_estoque SET produto_id = #PRODUTO_ID, funcionario_id = #FUNCIONARIO_ID, " +
                "quantidade_movimentada = #QTD_MOV, tipo = '#TIPO', data_hora_movimentacao = '#DATA_HORA', " +
                "observacao = '#OBSERVACAO' WHERE id = #ID";
        sql = sql.replace("#PRODUTO_ID", String.valueOf(movimentacao.getProduto().getId()));
        sql = sql.replace("#FUNCIONARIO_ID", String.valueOf(movimentacao.getFuncionario().getId()));
        sql = sql.replace("#QTD_MOV", String.valueOf(movimentacao.getQuantidadeMovimentada()));
        sql = sql.replace("#TIPO", movimentacao.getTipo().name());
        sql = sql.replace("#DATA_HORA", Timestamp.valueOf(movimentacao.getDataHoraMovimentacao()).toString());

        String observacao = movimentacao.getObservacao();
        if (observacao == null || observacao.trim().isEmpty()) {
            sql = sql.replace("'#OBSERVACAO'", "NULL");
        } else {
            observacao = observacao.replace("'", "''");
            sql = sql.replace("#OBSERVACAO", observacao);
        }
        sql = sql.replace("#ID", String.valueOf(movimentacao.getId()));
        this.dbConn.update(sql);
    }

    @Override
    public boolean delete(long id) {
        if (this.dbConn == null) throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.delete");
        String sql = "DELETE FROM movimentacao_estoque WHERE id = #ID";
        sql = sql.replace("#ID", String.valueOf(id));
        return this.dbConn.update(sql); // Assumindo que dbConn.update() retorna boolean
    }

    @Override
    public MovimentacaoEstoque findById(long id) {
        if (this.dbConn == null) {
            System.err.println("MovimentacaoEstoqueRepository.findById: dbConn é nulo!");
            return null;
        }
        MovimentacaoEstoque movimentacao = null;
        String sql = "SELECT * FROM movimentacao_estoque WHERE id = #ID";
        sql = sql.replace("#ID", String.valueOf(id)); // SQL Injection aqui se id fosse uma string controlada pelo usuário

        try (ResultSet rs = this.dbConn.query(sql)) {
            if (rs != null && rs.next()) {
                movimentacao = mapRowToMovimentacaoEstoque(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movimentacao;
    }

    @Override
    public List<MovimentacaoEstoque> findAll() {
        if (this.dbConn == null) {
            System.err.println("MovimentacaoEstoqueRepository.findAll: dbConn é nulo!");
            return new ArrayList<>();
        }
        List<MovimentacaoEstoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao_estoque ORDER BY data_hora_movimentacao DESC";

        try (ResultSet rs = this.dbConn.query(sql)) {
            if (rs != null) { // Boa prática checar se o ResultSet não é nulo
                while (rs.next()) {
                    lista.add(mapRowToMovimentacaoEstoque(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private MovimentacaoEstoque mapRowToMovimentacaoEstoque(ResultSet rs) throws SQLException {
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setId(rs.getLong("id"));

        Produtos produto = new Produtos();
        produto.setId(rs.getInt("produto_id"));
        mov.setProduto(produto);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(rs.getInt("funcionario_id"));
        mov.setFuncionario(funcionario);

        mov.setQuantidadeMovimentada(rs.getLong("quantidade_movimentada"));
        mov.setTipo(TipoMovimentacao.valueOf(rs.getString("tipo")));

        Timestamp dataHoraTimestamp = rs.getTimestamp("data_hora_movimentacao");
        if (dataHoraTimestamp != null) {
            mov.setDataHoraMovimentacao(dataHoraTimestamp.toLocalDateTime());
        }

        mov.setObservacao(rs.getString("observacao"));
        return mov;
    }
}