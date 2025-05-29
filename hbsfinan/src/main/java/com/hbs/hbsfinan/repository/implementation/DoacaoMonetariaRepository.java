package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.dto.DoacaoMonetariaCreateDTO;
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

    //@Autowired
    private Conexao dbConn;
    public DoacaoMonetariaRepository(Conexao dbConn){this.dbConn = dbConn;}


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
    public void save(DoacaoMonetariaCreateDTO doacaoMonetaria) {
        String sql = "INSERT INTO doacao_monetaria (valor, data, id_ap, id_caixa) values(#1, '#2', #3, #4)";
        sql = sql.replace("#1", "" + doacaoMonetaria.getValor());
        sql = sql.replace("#2", doacaoMonetaria.getData().toString());
        sql = sql.replace("#3", "" + doacaoMonetaria.getIdApoiador());
        sql = sql.replace("#4", "" + doacaoMonetaria.getIdCaixa());
        dbConn.update(sql);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM doacao_monetaria WHERE id =#1";
        sql=sql.replace("#1",""+id);
        return dbConn.update(sql);
    }

    @Override
    public void update(DoacaoMonetaria doacaoMonetaria) {
        String sql = "UPDATE doacao_monetaria SET valor = '#1', data = '#2', id_ap = '#3' WHERE id = #4";
        sql = sql.replace("#1", "" + doacaoMonetaria.getValor());
        sql = sql.replace("#2", "" + doacaoMonetaria.getData());
        sql = sql.replace("#3", "" + doacaoMonetaria.getApoiadorId());
        sql = sql.replace("#4", "" + doacaoMonetaria.getId());

        dbConn.update(sql);
    }

    @Override
    public DoacaoMonetaria findById(int id) {
        DoacaoMonetaria doacaoMonetaria = null;
        String sql = "SELECT * FROM doacao_monetaria WHERE id = #1";
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
    public List<DoacaoMonetaria> findByCaixa(int id) {
        List<DoacaoMonetaria> listDoacaoMonetaria = new ArrayList<>();
        String sql = "SELECT * FROM doacao_monetaria WHERE id_caixa = #1";
        sql = sql.replace("#1", "" + id);
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
                listDoacaoMonetaria.add(doacaoMonetaria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDoacaoMonetaria;
    }

    @Override
    public DoacaoMonetaria findByApoiador(String apoiador) {
        return null;
    }


    @Override
    public List<DoacaoMonetaria> findAll() {
        List<DoacaoMonetaria> doacaoMonetarias = new ArrayList<>();
        String sql = "SELECT * FROM doacao_monetaria";
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
