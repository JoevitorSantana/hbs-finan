package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.DoacaoProduto;
import com.hbs.hbsfinan.model.ItemDoacao;
import com.hbs.hbsfinan.model.ItemDoacaoId;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.repository.interfaces.IItemDoacao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemDoacaoRepository implements IItemDoacao {

    private final Conexao dbConn;

    public ItemDoacaoRepository() {
        this.dbConn = Conexao.getInstance();
        if (this.dbConn == null || !this.dbConn.getEstadoConexao()) {
            throw new RuntimeException("Não foi possível obter a conexão com o banco de dados.");
        }
    }

    @Override
    public void save(ItemDoacao itemDoacao) {
        String sql = "INSERT INTO item_doacao (doacao_produto_id, produto_id, quantidade, data_validade) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, itemDoacao.getDoacaoProduto().getId());
            ps.setLong(2, itemDoacao.getProduto().getId());
            ps.setLong(3, itemDoacao.getQuantidade());

            if (itemDoacao.getDataValidade() != null) {
                ps.setDate(4, Date.valueOf(itemDoacao.getDataValidade()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar item da doação", e);
        }
    }

    @Override
    public List<ItemDoacao> findByDoacaoProdutoId(int doacaoProdutoId) {
        List<ItemDoacao> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_doacao WHERE doacao_produto_id = ?";

        try (Connection conn = dbConn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, doacaoProdutoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemDoacao item = new ItemDoacao();

                // Setando produto
                Produtos produto = new Produtos();
                produto.setId(rs.getInt("produto_id"));
                item.setProduto(produto);

                // Setando doacaoProduto (somente o id)
                DoacaoProduto doacao = new DoacaoProduto();
                doacao.setId(rs.getLong("doacao_produto_id"));
                item.setDoacaoProduto(doacao);

                // Setando a chave composta
                ItemDoacaoId id = new ItemDoacaoId(doacao.getId(), produto.getId());
                item.setId(id);

                item.setQuantidade(rs.getLong("quantidade"));

                Date dtValidade = rs.getDate("data_validade");
                if (dtValidade != null) {
                    item.setDataValidade(dtValidade.toLocalDate());
                }

                itens.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens da doação", e);
        }

        return itens;
    }

    @Override
    public void deleteByDoacaoProdutoId(int doacaoProdutoId) {
        String sql = "DELETE FROM item_doacao WHERE doacao_produto_id = ?";

        try (Connection conn = dbConn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, doacaoProdutoId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar itens da doação", e);
        }
    }
}
