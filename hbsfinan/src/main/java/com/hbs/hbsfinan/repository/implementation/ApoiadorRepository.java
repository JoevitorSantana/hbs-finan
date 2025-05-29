package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.SingletonDB;
import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.Grupo;
import com.hbs.hbsfinan.repository.interfaces.IApoiadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ApoiadorRepository implements IApoiadorRepository {

    private Conexao dbConn;

    public ApoiadorRepository(Conexao dbConn) {
        if (dbConn == null || dbConn.getConnection() == null) {
            System.out.println("Tentando conectar ao banco...");
            this.dbConn = Conexao.getInstance();
        } else {
            this.dbConn = dbConn;
        }
    }



    @Override
    public void save(Apoiador apoiadorDTO) {
        String sql = "INSERT INTO apoiador (nome, endereco, sexo, fone, cpf, email, data_nasc, id_grupo) VALUES ('#1', '#2', '#3', '#4', '#5', '#6', '#7', '#8')";
        sql = sql.replace("#1", apoiadorDTO.getNome())
                .replace("#2", apoiadorDTO.getEndereco())
                .replace("#3", apoiadorDTO.getSexo())
                .replace("#4", apoiadorDTO.getFone())
                .replace("#5", apoiadorDTO.getCpf())
                .replace("#6", apoiadorDTO.getEmail())
                .replace("#7", apoiadorDTO.getDataNasc().toString())
                .replace("#8", String.valueOf( apoiadorDTO.getGrupo().getId()));
        dbConn.update(sql);
    }


    @Override
    public List<Apoiador> findAll() {
        List<Apoiador> apoiadores = new ArrayList<>();
        String sql = "SELECT * FROM apoiador";
        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                Apoiador a = new Apoiador();
                a.setId(rs.getLong("id"));
                a.setNome(rs.getString("nome"));
                a.setEndereco(rs.getString("endereco"));
                a.setSexo(rs.getString("sexo"));
                a.setFone(rs.getString("fone"));
                a.setCpf(rs.getString("cpf"));
                a.setEmail(rs.getString("email"));
                a.setDataNasc(rs.getDate("data_nasc").toLocalDate());
                apoiadores.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apoiadores;
    }

    @Override
    public Apoiador findById(Long id) {
        Apoiador apoiador = null;
        String sql = "SELECT * FROM apoiador WHERE id = #1";
        sql = sql.replace("#1", "" + id);
        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                apoiador = new Apoiador();
                apoiador.setId(rs.getLong("id"));
                apoiador.setNome(rs.getString("nome"));
                apoiador.setEndereco(rs.getString("endereco"));
                apoiador.setSexo(rs.getString("sexo"));
                apoiador.setFone(rs.getString("fone"));
                apoiador.setCpf(rs.getString("cpf"));
                apoiador.setEmail(rs.getString("email"));
                apoiador.setDataNasc(rs.getDate("data_nasc").toLocalDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apoiador;
    }

    @Override
    public boolean delete(Long id) {
       String sql = "DELETE FROM apoiador WHERE id = #1";
       sql=sql.replace("#1",""+id);

       return dbConn.update(sql);
    }


    @Override
    public void update(Apoiador apoiador) {

        String sql = "UPDATE apoiador SET nome = '#1', endereco = '#2', sexo = '#3', fone = '#4', cpf = '#5', email = '#6', data_nasc = '#7' WHERE id = '#8'";
        sql = sql.replace("#1",   apoiador.getNome());
        sql = sql.replace("#2",apoiador.getEndereco());
        sql = sql.replace("#3",apoiador.getSexo());
        sql = sql.replace("#4",apoiador.getFone());
        sql = sql.replace("#5", apoiador.getCpf());
        sql = sql.replace("#6",apoiador.getEmail());
        sql = sql.replace("#7",apoiador.getDataNasc().toString());
        sql = sql.replace("#8","" +apoiador.getId());

        dbConn.update(sql);
    }

    @Override
    public Apoiador findByEmail(String email) {
        Apoiador apoiador = null;
        String sql = "SELECT * FROM apoiador WHERE email = '#1'";
        sql = sql.replace("#1", email);
        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                apoiador = new Apoiador();
                apoiador.setId(rs.getLong("id"));
                apoiador.setNome(rs.getString("nome"));
                apoiador.setEndereco(rs.getString("endereco"));
                apoiador.setSexo(rs.getString("sexo"));
                apoiador.setFone(rs.getString("fone"));
                apoiador.setCpf(rs.getString("cpf"));
                apoiador.setEmail(rs.getString("email"));
                apoiador.setDataNasc(rs.getDate("data_nasc").toLocalDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apoiador;
    }



}
