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
    //private Conexao dbConn = Conexao.getInstance();
    private Conexao dbConn; // Não inicializa mais aqui

    // Construtor para receber a instância de Conexao
    public ProdutosRepository(Conexao dbConn) {
        this.dbConn = dbConn;
    }


    private RowMapper<Produtos> rowMapper = (rs, rowNum) -> {
        Produtos produtos = new Produtos();
        produtos.setId(rs.getInt("id"));
        produtos.setNome(rs.getString("nome"));
        produtos.setQtd(rs.getLong("qtd"));

        return produtos;
    };
    @Override
    public void save(Produtos produtos) {
        String sql = "INSERT INTO produtos (nome, qtd) values ('#1', '#2')";
        sql = sql.replace("#1", produtos.getNome());
        sql = sql.replace("#2", "" + produtos.getQtd());
        dbConn.update(sql);
    }


    @Override
    public Produtos findById(int id) {
        Produtos produtos = null;
        String sql = "SELECT * FROM produtos WHERE id = #1";
        sql = sql.replace("#1", "" + id);
        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                produtos = new Produtos();
                produtos.setId(rs.getInt("id"));
                produtos.setNome(rs.getString("nome"));
                produtos.setQtd(rs.getLong("qtd"));



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
                Produtos produtos1 = new Produtos();
                produtos1.setId(rs.getInt("id"));
                produtos1.setNome(rs.getString("nome"));
                produtos1.setQtd(rs.getLong("qtd"));




                produtos.add(produtos1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM produtos WHERE id =#1";
        sql=sql.replace("#1",""+id);
        return dbConn.update(sql);
    }


    @Override
    public void update(Produtos produtos) {
        String sql = "UPDATE produtos SET nome = '#1', qtd = '#2' WHERE id = #3";
        sql = sql.replace("#1", produtos.getNome());
        sql = sql.replace("#2", "" + produtos.getQtd());
        sql = sql.replace("#3", "" + produtos.getId());

        dbConn.update(sql);
    }





}
