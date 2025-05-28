package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.infra.db.SingletonDB;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.interfaces.IEventoRepository;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventoRepository implements IEventoRepository {

    private Conexao dbConn;

    public EventoRepository(Conexao dbConn) {this.dbConn = dbConn;}

    @Override
    public void save(Evento evento) {
        String sql = "INSERT INTO evento(local, data, descricao, nome, materiais, func_id) VALUES ('#1', '#2', '#3', '#4', '#5', #6)";
        sql = sql.replace("#1", evento.getLocal());
        sql = sql.replace("#2", evento.getData().toString());
        sql = sql.replace("#3", evento.getDescricao());
        sql = sql.replace("#4", evento.getNome());
        sql = sql.replace("#5", evento.getMateriais());
        sql = sql.replace("#6", "" + evento.getFuncionario().getId());

        dbConn.update(sql);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM evento WHERE id = #1";
        sql = sql.replace("#1", "" + id);
        return dbConn.update(sql);
    }

    @Override
    public void update(Evento evento) {
        String sql = "UPDATE evento SET local = '#1', data = '#2', descricao = '#3', nome = '#4', materiais = '#5' WHERE id = #6";
        sql = sql.replace("#1", evento.getLocal());
        sql = sql.replace("#2", evento.getData().toString());
        sql = sql.replace("#3", evento.getDescricao());
        sql = sql.replace("#4", evento.getNome());
        sql = sql.replace("#5", evento.getMateriais());
        sql = sql.replace("#6", "" + evento.getId());

        dbConn.update(sql);
    }

    @Override
    public Evento findById(int id) {
        Evento evento = null;
        String sql = "SELECT * FROM evento WHERE id = #1";
        sql = sql.replace("#1", "" + id);
        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                evento = new Evento();
                evento.setId(rs.getInt("id"));
                evento.setLocal(rs.getString("local"));
                evento.setData(rs.getDate("data"));
                evento.setDescricao(rs.getString("descricao"));
                evento.setNome(rs.getString("nome"));
                evento.setMateriais(rs.getString("materiais"));

                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("func_id"));
                evento.setFuncionario(funcionario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return evento;
    }

    @Override
    public Evento findByNome(String nome) {
        Evento evento = null;
        String sql = "SELECT * FROM evento WHERE nome = '#1'";
        sql = sql.replace("#1", nome);
        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                evento = new Evento();
                evento.setId(rs.getInt("id"));
                evento.setLocal(rs.getString("local"));
                evento.setData(rs.getDate("data"));
                evento.setDescricao(rs.getString("descricao"));
                evento.setNome(rs.getString("nome"));
                evento.setMateriais(rs.getString("materiais"));

                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("func_id"));
                evento.setFuncionario(funcionario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return evento;
    }

    @Override
    public List<Evento> findAll() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM evento";
        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getInt("id"));
                evento.setLocal(rs.getString("local"));
                evento.setData(rs.getDate("data"));
                evento.setDescricao(rs.getString("descricao"));
                evento.setNome(rs.getString("nome"));
                evento.setMateriais(rs.getString("materiais"));

                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("func_id"));
                evento.setFuncionario(funcionario);

                eventos.add(evento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventos;
    }
}
