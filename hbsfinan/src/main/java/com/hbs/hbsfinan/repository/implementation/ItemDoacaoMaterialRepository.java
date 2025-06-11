package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.*;
import com.hbs.hbsfinan.repository.interfaces.IItemDoacaoMaterial;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemDoacaoMaterialRepository implements IItemDoacaoMaterial {

    private final Conexao dbConn;

    public ItemDoacaoMaterialRepository() {
        this.dbConn = Conexao.getInstance();
        if (this.dbConn == null || !this.dbConn.getEstadoConexao()) {
            throw new RuntimeException("Não foi possível obter a conexão com o banco de dados.");
        }
    }

    @Override
    public void save(ItemDoacaoMaterial itemDoacaoMaterial) {
        if (itemDoacaoMaterial == null
                || itemDoacaoMaterial.getDoacaoMaterial() == null
                || itemDoacaoMaterial.getProduto() == null) {
            throw new IllegalArgumentException("DoacaoMaterial e Produto não podem ser nulos");
        }

        String sql = "INSERT INTO item_doacao_material (doacao_produto_id, produto_id, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = dbConn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, itemDoacaoMaterial.getDoacaoMaterial().getId());
            ps.setLong(2, itemDoacaoMaterial.getProduto().getId());
            ps.setLong(3, itemDoacaoMaterial.getQuantidade());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar item da doação", e);
        }
    }

    @Override
    public List<ItemDoacaoMaterial> findByDoacaoProdutoId(long doacaoProdutoId) {
        List<ItemDoacaoMaterial> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_doacao_material WHERE doacao_produto_id = ?";

        try (Connection conn = dbConn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, doacaoProdutoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemDoacaoMaterial item = new ItemDoacaoMaterial();

                // Setando produto com apenas id
                Produtos produto = new Produtos();
                produto.setId(rs.getInt("produto_id"));
                item.setProduto(produto);

                // Setando doacaoMaterial com apenas id
                DoacaoMaterial doacao = new DoacaoMaterial();
                doacao.setId(rs.getLong("doacao_produto_id"));
                item.setDoacaoMaterial(doacao);

                // Setando a chave composta
                ItemMaterialId id = new ItemMaterialId(doacao.getId(), produto.getId());
                item.setId(id);

                item.setQuantidade(rs.getLong("quantidade"));

                itens.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens da doação", e);
        }

        return itens;
    }

    @Override
    public void deleteByDoacaoProdutoId(long doacaoProdutoId) {
        String sql = "DELETE FROM item_doacao_material WHERE doacao_produto_id = ?";

        try (Connection conn = dbConn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, doacaoProdutoId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar itens da doação", e);
        }
    }
}
