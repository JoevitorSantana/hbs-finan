package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao; // Importe Conexao
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.model.MovimentacaoEstoque;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.enums.TipoMovimentacao;
import com.hbs.hbsfinan.repository.interfaces.IMovimentacaoEstoqueRepository;
import org.springframework.stereotype.Repository; // Adicione esta anotação

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository // <-- ADICIONE ESTA ANOTAÇÃO para que o Spring o reconheça como um bean
public class MovimentacaoEstoqueRepository implements IMovimentacaoEstoqueRepository {

    // Obtém a Conexao usando o metodo singleton estático, assim como o ProdutosRepository
    private Conexao dbConn = Conexao.getInstance();

    // REMOVA qualquer construtor que esperava receber Conexao.
    // O Spring usará o construtor padrão (implícito ou explícito sem argumentos).
    // public MovimentacaoEstoqueRepository(Conexao dbConn) { ... } // <-- REMOVER ESTE TIPO DE CONSTRUTOR

    public MovimentacaoEstoqueRepository() {
        // Construtor padrão.
        // Verifica se dbConn foi inicializado corretamente. Se getInstance() falhar, ele lançará exceção.
        if (this.dbConn == null) {
            throw new RuntimeException("Falha ao inicializar MovimentacaoEstoqueRepository: instância de Conexao é nula.");
        }
    }


    @Override
    public void save(MovimentacaoEstoque movimentacao) {
        // Lógica do save usando this.dbConn (como definido antes)
        // Exemplo:
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

    // ... Implementação dos outros métodos (update, delete, findById, findAll, mapRowToMovimentacaoEstoque)
    // usando this.dbConn, como na versão anterior que te enviei para esta classe.
    // Certifique-se que todos usam this.dbConn.
    // Vou omiti-los aqui para brevidade, mas eles devem estar presentes e corretos.
    @Override
    public void update(MovimentacaoEstoque movimentacao) {
        String sql = "UPDATE movimentacao_estoque SET produto_id = ?, funcionario_id = ?, quantidade_movimentada = ?, " +
                "tipo = ?, data_hora_movimentacao = ?, observacao = ? WHERE id = ?";
        // Adapte para usar replace, ou idealmente, se Conexao suportar PreparedStatement no futuro:
        // this.dbConn.update(sql,
        //     movimentacao.getProduto().getId(), movimentacao.getFuncionario().getId(), /* ... etc ... */);

        // Versão com replace (CUIDADO com SQL INJECTION):
        sql = "UPDATE movimentacao_estoque SET produto_id = #PRODUTO_ID, funcionario_id = #FUNCIONARIO_ID, " +
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
        String sql = "DELETE FROM movimentacao_estoque WHERE id = #ID";
        sql = sql.replace("#ID", String.valueOf(id));
        return this.dbConn.update(sql);
    }

    @Override
    public MovimentacaoEstoque findById(long id) {
        MovimentacaoEstoque movimentacao = null;
        String sql = "SELECT * FROM movimentacao_estoque WHERE id = #ID";
        sql = sql.replace("#ID", String.valueOf(id));
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
        List<MovimentacaoEstoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao_estoque ORDER BY data_hora_movimentacao DESC";
        try (ResultSet rs = this.dbConn.query(sql)) {
            if (rs != null) {
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