package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.DoacaoMonetaria;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoMonetariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DoacaoMonetariaRepository implements IDoacaoMonetariaRepository {

    @Autowired
    private JdbcTemplate dbConn;

    private RowMapper<DoacaoMonetaria> rowMapper = (rs, rowNum) ->
    {
        DoacaoMonetaria doacaoMonetaria = new DoacaoMonetaria();
        doacaoMonetaria.setId(rs.getInt("id"));
        doacaoMonetaria.setValor(rs.getLong("valor"));
        doacaoMonetaria.setData(rs.getDate("data"));


        //ver se esta certo
        Apoiador apoiador = new Apoiador();
        apoiador.setId(rs.getLong("apo_id"));
        doacaoMonetaria.setApoiador(apoiador);

        return doacaoMonetaria;
    };



    @Override
    public void save(DoacaoMonetaria doacaoMonetaria) {
        dbConn.update("INSERT INTO doacaoMonetaria(valor,data,apoiador)values (?,?,?)", doacaoMonetaria.getValor(), doacaoMonetaria.getData(),doacaoMonetaria.getApoiador());
    }

    @Override
    public void delete(int id) {
        dbConn.update("DELETE FROM doacaoMonetaria WHERE id=?",id);
    }

    @Override
    public void update(DoacaoMonetaria doacaoMonetaria) {
        dbConn.update("UPDATE doacaoMonetaria SET valor = ?, data = ?, apoiador = ? WHERE id = ?",doacaoMonetaria.getValor(), doacaoMonetaria.getData(),doacaoMonetaria.getApoiador(),doacaoMonetaria.getId());
    }

    @Override
    public DoacaoMonetaria findById(int id) {
        try
        {
            return dbConn.queryForObject("SELECT * FROM doacaoMonetaria WHERE id = ?", rowMapper, id);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    @Override
    public DoacaoMonetaria findByApoiador(String apoiador) {
        try
        {
            return dbConn.queryForObject("SELECT * FROM doacaoMonetaria WHERE apoiador = ?", rowMapper, apoiador);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    @Override
    public List<DoacaoMonetaria> findAll() {
        return dbConn.query("SELECT * FROM evento", rowMapper);
    }
}
