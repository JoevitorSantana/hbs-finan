package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.repository.interfaces.IProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProdutosRepository implements IProdutosRepository {

    @Autowired
    private JdbcTemplate dbConn;

    private RowMapper<Produtos> rowMapper = (rs, rowNum) -> {
        Produtos produtos = new Produtos();
        produtos.setId(rs.getInt("id"));
        produtos.setNome(rs.getString("nome"));
        produtos.setQtd(rs.getLong("qtd"));

        return produtos;
    };
    @Override
    public void save(Produtos produtos) {
        dbConn.update("INSERT INTO produtos (nome,qtd) VALUES (?,?)",produtos.getNome(),produtos.getQtd());
    }

    @Override
    public Produtos findById(int id) {
        return dbConn.queryForObject("SELECT * FROM produtos WHERE id = ?", rowMapper, id);
    }

    @Override
    public List<Produtos> findAll() {
        return dbConn.query("SELECT * FROM produtos", rowMapper);
    }

    @Override
    public void delete(int id) {
        dbConn.update("DELETE FROM produtos WHERE id = ?", id);
    }

    @Override
    public void update(Produtos produtos) {
        dbConn.update("UPDATE produtos SET nome = ?, qtd = ? WHERE id = ?", produtos.getNome(),produtos.getQtd(), produtos.getId());
    }



}
