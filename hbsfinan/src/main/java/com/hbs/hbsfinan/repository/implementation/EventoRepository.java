package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.interfaces.IEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        funcionario.setId(rs.getInt("id"));
        evento.setFuncionario(funcionario);

        return evento;
    };

    @Override
    public void save(Evento evento) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Evento evento) {

    }

    @Override
    public Evento findById(int id) {
        return null;
    }

    @Override
    public Evento findByNome(String nome) {
        return null;
    }

    @Override
    public List<Evento> findAll() {
        return List.of();
    }
}
