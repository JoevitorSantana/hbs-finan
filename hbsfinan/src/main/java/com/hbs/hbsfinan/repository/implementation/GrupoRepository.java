package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Grupo;
import com.hbs.hbsfinan.repository.interfaces.IGrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
//
@Repository
public class GrupoRepository implements IGrupoRepository {

    @Autowired
    private JdbcTemplate dbConn;
    private RowMapper<Grupo> rowMapper = (rs, rowNum) ->
    {
     Grupo grupo = new Grupo();
     grupo.setId(rs.getInt("id"));
     grupo.setNome(rs.getString("nome"));
     return grupo;
    };

    @Override
    public void save (Grupo grupo)
    {
        dbConn.update("INSERT INTO grupo(nome)values (?)",grupo.getNome());//testar sem o ID e ver se vai
    }

    @Override
    public List<Grupo> findAll() {
        return dbConn.query("SELECT * FROM grupo", rowMapper);
    }

    @Override
    public Grupo findByNome(String nome) {
        try
        {
            return dbConn.queryForObject("SELECT * FROM grupo WHERE nome = ?", rowMapper, nome);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    @Override
    public Grupo findById(int id) {
        try
        {
            return dbConn.queryForObject("SELECT * FROM grupo WHERE id = ?", rowMapper, id);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    @Override
    public void update(Grupo grupo) {
        dbConn.update("UPDATE grupo SET nome = ? WHERE id = ?", grupo.getNome(),grupo.getId());
    }

    @Override
    public void delete(int id) {
        dbConn.update("DELETE FROM grupo WHERE id = ?", id);
    }
}
