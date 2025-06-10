package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.infra.db.SingletonDB;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.model.Grupo;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.repository.interfaces.IProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutosRepository implements IProdutosRepository {

    //@Autowired
    //private Conexao dbConn = SingletonDB.getConexao();

    private Conexao dbConn;

    public ProdutosRepository(Conexao dbConn) {
        this.dbConn = dbConn;
    }

    @Override
    public void save(Produtos produtos) {
        String sql = "INSERT INTO produtos (nome, qtd, data_validade) VALUES ('#1', #2, #3)";
        sql = sql.replace("#1", produtos.getNome());
        sql = sql.replace("#2", String.valueOf(produtos.getQtd()));
        sql = sql.replace("#3",
                produtos.getDataValidade() != null
                        ? "'" + produtos.getDataValidade().toString() + "'"
                        : "NULL"
        );
        dbConn.update(sql);
    }

    @Override
    public Produtos findById(int id) {
        Produtos produtos = null;
        String sql = "SELECT * FROM produtos WHERE id = " + id;
        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                produtos = new Produtos();
                produtos.setId(rs.getInt("id"));
                produtos.setNome(rs.getString("nome"));
                produtos.setQtd(rs.getLong("qtd"));

                if (rs.getDate("data_validade") != null) {
                    produtos.setDataValidade(rs.getDate("data_validade").toLocalDate());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;
    }

    @Override
    public List<Produtos> findAll() {
        List<Produtos> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                Produtos p = new Produtos();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setQtd(rs.getLong("qtd"));

                if (rs.getDate("data_validade") != null) {
                    p.setDataValidade(rs.getDate("data_validade").toLocalDate());
                }

                produtos.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM produtos WHERE id = " + id;
        return dbConn.update(sql);
    }

    @Override
    public void update(Produtos produtos) {
        String sql = "UPDATE produtos SET nome = '#1', qtd = #2, data_validade = #3 WHERE id = #4";
        sql = sql.replace("#1", produtos.getNome());
        sql = sql.replace("#2", String.valueOf(produtos.getQtd()));
        sql = sql.replace("#3",
                produtos.getDataValidade() != null
                        ? "'" + produtos.getDataValidade().toString() + "'"
                        : "NULL"
        );
        sql = sql.replace("#4", String.valueOf(produtos.getId()));
        System.out.println("Nome: " + produtos.getNome());
        System.out.println("Quantidade: " + produtos.getQtd());
        System.out.println("Data validade: " + produtos.getDataValidade());
        System.out.println("SQL gerado: " + sql);

        dbConn.update(sql);
    }




}