//package com.hbs.hbsfinan.repository.implementation;
//
//import com.hbs.hbsfinan.infra.db.Conexao;
//// import com.hbs.hbsfinan.model.Funcionario; // Removido
//import com.hbs.hbsfinan.model.MovimentacaoEstoque;
//import com.hbs.hbsfinan.model.Produtos;
//import com.hbs.hbsfinan.enums.TipoMovimentacao;
//import com.hbs.hbsfinan.repository.interfaces.IMovimentacaoEstoqueRepository;
//import org.springframework.stereotype.Repository;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//// import java.sql.Timestamp; // Removido
//// import java.time.LocalDateTime; // Removido
//import java.time.LocalDate; // Mantido, pois Produtos ainda pode ter data de validade
//import java.util.ArrayList;
//import java.util.List;
//
//@Repository
//public class MovimentacaoEstoqueRepository implements IMovimentacaoEstoqueRepository {
//    private Conexao dbConn;
//
//    public MovimentacaoEstoqueRepository(Conexao dbConn) {
//        if (dbConn == null) {
//            throw new IllegalArgumentException("Instância de Conexao não pode ser nula para MovimentacaoEstoqueRepository.");
//        }
//        this.dbConn = dbConn;
//    }
//
//    @Override
//    public void save(MovimentacaoEstoque movimentacao) {
//        if (this.dbConn == null) {
//            throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.save. A instância de Conexao não foi corretamente injetada.");
//        }
//        // Removidos funcionario_id, data_hora_movimentacao, observacao
//        String sql = "INSERT INTO movimentacao_estoque (produto_id, quantidade_movimentada, tipo) " +
//                "VALUES (#1, #2, '#3')";
//        sql = sql.replace("#1", String.valueOf(movimentacao.getProduto().getId()));
//        sql = sql.replace("#2", String.valueOf(movimentacao.getQuantidadeMovimentada()));
//        sql = sql.replace("#3", movimentacao.getTipo().name());
//
//        try {
//            this.dbConn.update(sql);
//        } catch (Exception e) {
//            System.err.println("Erro ao salvar movimentação de estoque: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("Erro ao salvar movimentação de estoque no banco de dados.", e);
//        }
//    }
//
//    @Override
//    public void update(MovimentacaoEstoque movimentacao) {
//        if (this.dbConn == null)
//            throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.update");
//        // Removidos funcionario_id, data_hora_movimentacao, observacao
//        String sql = "UPDATE movimentacao_estoque SET produto_id = #1, " +
//                "quantidade_movimentada = #2, tipo = '#3' WHERE id = #4";
//        sql = sql.replace("#1", String.valueOf(movimentacao.getProduto().getId()));
//        sql = sql.replace("#2", String.valueOf(movimentacao.getQuantidadeMovimentada()));
//        sql = sql.replace("#3", movimentacao.getTipo().name());
//        sql = sql.replace("#4", String.valueOf(movimentacao.getId()));
//        try {
//            this.dbConn.update(sql);
//        } catch (Exception e) {
//            System.err.println("Erro ao atualizar movimentação de estoque: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("Erro ao atualizar movimentação de estoque no banco de dados.", e);
//        }
//    }
//
//    @Override
//    public boolean delete(long id) {
//        if (this.dbConn == null) throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.delete");
//        String sql = "DELETE FROM movimentacao_estoque WHERE id = #1";
//        sql = sql.replace("#1", String.valueOf(id));
//
//        try {
//            return this.dbConn.update(sql);
//        } catch (Exception e) {
//            System.err.println("Erro ao deletar movimentação de estoque: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("Erro ao deletar movimentação de estoque no banco de dados.", e);
//        }
//    }
//
//    @Override
//    public MovimentacaoEstoque findById(long id) {
//        if (this.dbConn == null) {
//            System.err.println("MovimentacaoEstoqueRepository.findById: dbConn é nulo!");
//            throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.findById.");
//        }
//        MovimentacaoEstoque movimentacao = null;
//        // Removidos data_hora_movimentacao, observacao e join com funcionario
//        String sql = "SELECT me.id AS mov_id, me.quantidade_movimentada, me.tipo, " +
//                "p.id AS prod_id, p.nome AS prod_nome, p.qtd AS prod_qtd, p.data_validade AS prod_data_validade " +
//                "FROM movimentacao_estoque me " +
//                "JOIN produtos p ON p.id = me.produto_id " +
//                "WHERE me.id = " + id;
//
//        try (ResultSet rs = this.dbConn.query(sql)) {
//            if (rs != null && rs.next()) {
//                movimentacao = mapRowToMovimentacaoEstoque(rs);
//            }
//        } catch (SQLException e) {
//            System.err.println("Erro ao buscar movimentação de estoque por ID: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("Erro ao buscar movimentação de estoque por ID.", e);
//        }
//        return movimentacao;
//    }
//
//    @Override
//    public List<MovimentacaoEstoque> findAll() {
//        if (this.dbConn == null) {
//            System.err.println("MovimentacaoEstoqueRepository.findAll: dbConn é nulo!");
//            throw new RuntimeException("Conexao dbConn é nula em MovimentacaoEstoqueRepository.findAll.");
//        }
//        List<MovimentacaoEstoque> lista = new ArrayList<>();
//        // Removidos data_hora_movimentacao, observacao e join com funcionario
//        String sql = "SELECT me.id AS mov_id, me.quantidade_movimentada, me.tipo, " +
//                "p.id AS prod_id, p.nome AS prod_nome, p.qtd AS prod_qtd, p.data_validade AS prod_data_validade " +
//                "FROM movimentacao_estoque me " +
//                "JOIN produtos p ON p.id = me.produto_id " +
//                "ORDER BY me.id DESC"; // Ordem por ID, já que não temos data/hora
//
//        try (ResultSet rs = this.dbConn.query(sql)) {
//            if (rs != null) {
//                while (rs.next()) {
//                    lista.add(mapRowToMovimentacaoEstoque(rs));
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Erro ao buscar todas as movimentações de estoque: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("Erro ao buscar todas as movimentações de estoque.", e);
//        }
//        return lista;
//    }
//
//    private MovimentacaoEstoque mapRowToMovimentacaoEstoque(ResultSet rs) throws SQLException {
//        MovimentacaoEstoque mov = new MovimentacaoEstoque();
//        mov.setId(rs.getLong("mov_id"));
//        mov.setQuantidadeMovimentada(rs.getLong("quantidade_movimentada"));
//        mov.setTipo(TipoMovimentacao.valueOf(rs.getString("tipo")));
//
//        // Campos removidos conforme sua preferência:
//        // Timestamp dataHoraTimestamp = rs.getTimestamp("data_hora_movimentacao");
//        // if (dataHoraTimestamp != null) {
//        //     mov.setDataHoraMovimentacao(dataHoraTimestamp.toLocalDateTime());
//        // }
//        // mov.setObservacao(rs.getString("observacao"));
//
//        Produtos produto = new Produtos();
//        produto.setId(rs.getInt("prod_id"));
//        produto.setNome(rs.getString("prod_nome"));
//        produto.setQtd(rs.getLong("prod_qtd"));
//        java.sql.Timestamp dataValidadeTimestamp = rs.getTimestamp("prod_data_validade"); // Usando java.sql.Timestamp
//        if (dataValidadeTimestamp != null) {
//            produto.setDataValidade(dataValidadeTimestamp.toLocalDateTime().toLocalDate());
//        } else {
//            produto.setDataValidade(null);
//        }
//        mov.setProduto(produto);
//
//        // Funcionario removido conforme sua preferência:
//        // Funcionario funcionario = new Funcionario();
//        // funcionario.setId(rs.getInt("func_id"));
//        // funcionario.setNome(rs.getString("func_nome"));
//        // funcionario.setEmail(rs.getString("func_email"));
//        // mov.setFuncionario(funcionario);
//
//        return mov;
//    }
//}