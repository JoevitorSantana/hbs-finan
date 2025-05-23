package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.infra.db.SingletonDB;
import com.hbs.hbsfinan.model.*;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoMonetariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoacaoMonetariaRepository implements IDoacaoMonetariaRepository {

    @Autowired
    private Conexao dbConn = SingletonDB.getConexao();

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
        String sql = "INSERT INTO doacaoMonetaria (valor, data, id_ap) values('#1', '#2', '#3')";
        sql = sql.replace("#1", "" + doacaoMonetaria.getValor());
        sql = sql.replace("#2", "" + doacaoMonetaria.getData());
        sql = sql.replace("#3", "" + doacaoMonetaria.getApoiadorId());
        dbConn.update(sql);

    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM doacaoMonetaria WHERE id =#1";
        sql=sql.replace("#1",""+id);
        return dbConn.update(sql);
    }

    @Override
    public void update(DoacaoMonetaria doacaoMonetaria) {
        String sql = "UPDATE doacaoMonetaria SET valor = '#1', data = '#2', id_ap = '#3' WHERE id = #4";
        sql = sql.replace("#1", "" + doacaoMonetaria.getValor());
        sql = sql.replace("#2", "" + doacaoMonetaria.getData());
        sql = sql.replace("#3", "" + doacaoMonetaria.getApoiadorId());
        sql = sql.replace("#4", "" + doacaoMonetaria.getId());

        dbConn.update(sql);
    }

    @Override
    public DoacaoMonetaria findById(int id) {
        DoacaoMonetaria doacaoMonetaria = null;
        String sql = "SELECT * FROM doacaoMonetaria WHERE id = #1";
        sql = sql.replace("#1", "" + id);
        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                doacaoMonetaria = new DoacaoMonetaria();
                doacaoMonetaria.setId(rs.getInt("id"));
                doacaoMonetaria.setValor(rs.getLong("valor"));
                doacaoMonetaria.setData(rs.getDate("data"));
                Apoiador apoiador = new Apoiador();
                apoiador.setId(rs.getLong("id_ap"));
                doacaoMonetaria.setApoiador(apoiador);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doacaoMonetaria;
    }

    @Override
    public DoacaoMonetaria findByApoiador(String apoiador) {
        return null;
    }


    @Override
    public List<DoacaoMonetaria> findAll() {
        List<DoacaoMonetaria> doacaoMonetarias = new ArrayList<>();
        String sql = "SELECT * FROM doacaoMonetaria";
        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                DoacaoMonetaria doacaoMonetaria = new DoacaoMonetaria();
                doacaoMonetaria.setId(rs.getInt("id"));
                doacaoMonetaria.setValor(rs.getLong("valor"));
                doacaoMonetaria.setData(rs.getDate("data"));


                Apoiador apoiador = new Apoiador();
                apoiador.setId(rs.getLong("id_ap"));
                doacaoMonetaria.setApoiador(apoiador);

                doacaoMonetarias.add(doacaoMonetaria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doacaoMonetarias;
    }
}
