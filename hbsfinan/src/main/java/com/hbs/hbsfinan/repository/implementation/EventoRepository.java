package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.interfaces.IEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventoRepository implements IEventoRepository {
    @Autowired
    private JdbcTemplate dbConn;

    private RowMapper<Evento> rowMapper = (rs, rowNum) ->
    {
        Evento evento = new Evento();
        evento.setId(rs.getInt("id"));
        evento.setLocal(rs.getString("local"));
        evento.setData(rs.getDate("data"));
        evento.setDescricao(rs.getString("descricao"));
        evento.setNome(rs.getString("nome"));
        evento.setMateriais(rs.getString("materiais"));
        //ver se esta certo
        Funcionario funcionario = new Funcionario();
        funcionario.setId(rs.getInt("func_id"));
        evento.setFuncionario(funcionario);

        return evento;
    };

    @Override
    public void save(Evento evento) {
        dbConn.update("INSERT INTO evento(local,data,descricao,nome,materiais,func_id)values (?,?,?,?,?,?)",evento.getLocal(),evento.getData(),evento.getDescricao(),evento.getNome(),evento.getMateriais(),evento.getFuncionario().getId());
    }

    @Override
    public void delete(int id) {
        dbConn.update("DELETE FROM evento WHERE id=?",id);
    }

    @Override
    public void update(Evento evento) {
        dbConn.update("UPDATE evento SET local = ?, data = ?, descricao = ?, nome = ?, materiais = ? WHERE id = ?",evento.getLocal(),evento.getData(),evento.getDescricao(),evento.getNome(),evento.getMateriais(),evento.getId());
    }

    @Override
    public Evento findById(int id) {
        try
        {
            return dbConn.queryForObject("SELECT * FROM evento WHERE id = ?", rowMapper, id);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    @Override
    public Evento findByNome(String nome) {
        try
        {
            return dbConn.queryForObject("SELECT * FROM evento WHERE nome = ?", rowMapper, nome);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    @Override
    public List<Evento> findAll() {
        return dbConn.query("SELECT * FROM evento", rowMapper);
    }
}
