package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Grupo;
import com.hbs.hbsfinan.repository.interfaces.IGrupoRepository;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.infra.db.SingletonDB;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
//
@Repository
public class GrupoRepository implements IGrupoRepository {

    private Conexao dbConn;

    public GrupoRepository(Conexao dbConn) {this.dbConn = dbConn;}

    @Override
    public void save (Grupo grupo)
    {
        String sql = "INSERT INTO grupo(nome)values ('#1')";
        sql = sql.replace("#1", grupo.getNome());
        dbConn.update(sql);
    }

    @Override
    public List<Grupo> findAll()
    {
        List<Grupo> grupos = new ArrayList<>();
        String sql = "SELECT * FROM grupo";
        try
        {
            ResultSet rs = dbConn.query(sql);
            while (rs.next())
            {
                Grupo g = new Grupo();
                g.setId(rs.getInt("id"));
                g.setNome(rs.getString("nome"));
                grupos.add(g);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return grupos;
    }

    @Override
    public Grupo findByNome(String nome) {
        Grupo grupo = null;
        String sql = "SELECT * FROM grupo WHERE nome = '#1'";
        sql=sql.replace("#1",nome);
        try
        {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                grupo = new Grupo();
                grupo.setId(rs.getInt("id"));
                grupo.setNome(rs.getString("nome"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return grupo;
    }

    @Override
    public Grupo findById(int id) {
        Grupo grupo = null;
        String sql = "SELECT * FROM grupo WHERE id = #1";
        sql=sql.replace("#1",""+id);
        try
        {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                grupo = new Grupo();
                grupo.setId(rs.getInt("id"));
                grupo.setNome(rs.getString("nome"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return grupo;
    }

    @Override
    public void update(Grupo grupo) {
        String sql = "UPDATE grupo SET nome = '#1' WHERE id = #2";
        sql = sql.replace("#1", grupo.getNome());
        sql = sql.replace("#2","" +grupo.getId());

        dbConn.update(sql);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM grupo WHERE id = #1";
        sql=sql.replace("#1",""+id);

        return dbConn.update(sql);
    }
}