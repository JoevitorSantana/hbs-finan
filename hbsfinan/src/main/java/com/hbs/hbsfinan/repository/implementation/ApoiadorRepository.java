package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.interfaces.IApoiadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class ApoiadorRepository implements IApoiadorRepository {
    @Autowired
    private JdbcTemplate dbConn;


    //conferir no banco
    private RowMapper<Apoiador> rowMapper = (rs, rowNum) -> {
       Apoiador apoiador = new Apoiador();
        apoiador.setId(rs.getInt("id"));
        apoiador.setCpf(rs.getString("cpf"));
        apoiador.setId(rs.getInt("id"));
        apoiador.setCpf(rs.getString("cpf"));
        apoiador.setId(rs.getInt("id"));
        apoiador.setCpf(rs.getString("cpf"));
        apoiador.setId(rs.getInt("id"));
        apoiador.setCpf(rs.getString("cpf"));
        return apoiador;
    };



    @Override
    public void save(Apoiador apoiador) {
        dbConn.update("INSERT INTO apoiador (id, nome, endereco, sexo, telefone, cpf, email , data_nascimento) VALUES (?,?,?,?,?,?,?,?)", apoiador.getId(), apoiador.getNome(), apoiador.getEndereco(),apoiador.getSexo(), apoiador.getFone(), apoiador.getCpf(),apoiador.getEmail(),apoiador.getCpf());
    }

    @Override
    public Apoiador findById(int id) {
        return dbConn.queryForObject("SELECT * FROM apoiador WHERE id = ?", rowMapper, id);
    }

    @Override
    public List<Apoiador> findAll() {
        return dbConn.query("SELECT * FROM apoiador", rowMapper);
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Apoiador apoiador) {
        dbConn.update("UPDATE apoiador SET nome = ?, endereco = ?, sexo = ?, telefone=?, cpf=? WHERE id = ?",apoiador.getNome(), apoiador.getEndereco(),apoiador.getSexo(), apoiador.getFone(), apoiador.getCpf(),apoiador.getEmail(),apoiador.getCpf(),apoiador.getId());
    }

    @Override
    public Apoiador findByEmail(String email) {
        try {
            return dbConn.queryForObject("SELECT * FROM apoiador WHERE email = ?", rowMapper, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
