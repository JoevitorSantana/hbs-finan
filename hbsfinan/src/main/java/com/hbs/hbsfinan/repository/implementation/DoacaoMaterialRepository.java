package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.DoacaoMaterial;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.model.ItemDoacaoMaterial;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoMaterial;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoacaoMaterialRepository implements IDoacaoMaterial {

    private final Conexao dbConn;

    public DoacaoMaterialRepository() {
        this.dbConn = Conexao.getInstance();
        if (this.dbConn == null || !this.dbConn.getEstadoConexao()) {
            throw new RuntimeException("Não foi possível obter a conexão com o banco de dados.");
        }
    }

    @Override
    public void save(DoacaoMaterial doacao) {
        System.out.println("Salvando doação para funcionário: " + doacao.getFuncionario().getId());
        System.out.println("Itens na doação: " + (doacao.getItens() == null ? 0 : doacao.getItens().size()));

        String sqlDoacao = "INSERT INTO doacao_material (funcionario_id, data) VALUES (?, ?)";
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

                // Inserir os itens da doação material
                String sqlItem = "INSERT INTO item_doacao_material (doacao_material_id, produto_id, quantidade) VALUES (?, ?, ?)";
                try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                    for (ItemDoacaoMaterial item : doacao.getItens()) {
                        System.out.println("Salvando item: produto " + item.getProduto().getId() + ", quantidade " + item.getQuantidade());
                        psItem.setLong(1, doacaoId);
                        psItem.setLong(2, item.getProduto().getId());
                        psItem.setLong(3, item.getQuantidade());
                        psItem.addBatch();
                    }
                    psItem.executeBatch();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar doação com itens", e);
        }
    }

    @Override
    public DoacaoMaterial findById(int id) {
        DoacaoMaterial doacao = null;
        String sql = "SELECT * FROM doacao_material WHERE id = " + id;

        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                doacao = new DoacaoMaterial();
                doacao.setId(rs.getLong("id"));

                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("funcionario_id"));
                doacao.setFuncionario(funcionario);

                if (rs.getDate("data") != null) {
                    doacao.setData(rs.getDate("data").toLocalDate());
                }

                // Carrega os itens corretamente da doação material
                doacao.setItens(carregarItensDoacao(doacao.getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doacao;
    }

    @Override
    public List<DoacaoMaterial> findAll() {
        List<DoacaoMaterial> lista = new ArrayList<>();
        String sql = "SELECT * FROM doacao_material";

        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                DoacaoMaterial doacao = new DoacaoMaterial();
                doacao.setId(rs.getLong("id"));

                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("funcionario_id"));
                doacao.setFuncionario(funcionario);

                if (rs.getDate("data") != null) {
                    doacao.setData(rs.getDate("data").toLocalDate());
                }

                // Aqui você busca os itens com nome do material
                doacao.setItens(carregarItensDoacao(doacao.getId()));

                lista.add(doacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    private List<ItemDoacaoMaterial> carregarItensDoacao(Long doacaoMaterialId) {
        List<ItemDoacaoMaterial> itens = new ArrayList<>();

        String sql = "SELECT idm.*, p.nome AS nome_produto " +
                "FROM item_doacao_material idm " +
                "JOIN produtos p ON idm.produto_id = p.id " +
                "WHERE idm.doacao_material_id = " + doacaoMaterialId;

        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                ItemDoacaoMaterial item = new ItemDoacaoMaterial();
                item.setQuantidade(rs.getLong("quantidade"));

                Produtos produto = new Produtos();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("nome_produto")); // obtido via JOIN

                item.setProduto(produto);

                DoacaoMaterial doacaoMaterial = new DoacaoMaterial();
                doacaoMaterial.setId(doacaoMaterialId);
                item.setDoacaoMaterial(doacaoMaterial);

                itens.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itens;
    }



    @Override
    public void delete(int id) {
        String sql = "DELETE FROM doacao_material WHERE id = ?";

        try (Connection conn = dbConn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar doação", e);
        }
    }
}
