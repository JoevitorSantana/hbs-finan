package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.repository.interfaces.IApoiadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApoiadorRepository implements IApoiadorRepository {

    @Autowired
    private JdbcTemplate dbConn;


    //conferir no banco
    private RowMapper<Apoiador> rowMapper = (rs, rowNum) -> {
       Apoiador apoiador = new Apoiador();
        apoiador.setId(rs.getLong("id"));
        apoiador.setNome(rs.getString("nome"));
        apoiador.setCpf(rs.getString("cpf"));
        apoiador.setEmail(rs.getString("email"));
        apoiador.setFone(rs.getString("fone"));
        apoiador.setDataNasc(rs.getDate("data_nasc"));
        apoiador.setSexo(rs.getString("sexo"));
        apoiador.setEndereco(rs.getString("endereco"));
        return apoiador;
    };



    @Override
    public void save(ApoiadorDTO apoiadorDTO) {
        dbConn.update("INSERT INTO apoiador (nome, endereco, sexo, fone, cpf, email , data_nasc) VALUES (?,?,?,?,?,?,?)",apoiadorDTO.getNome(), apoiadorDTO.getEndereco(),apoiadorDTO.getSexo(), apoiadorDTO.getFone(), apoiadorDTO.getCpf(),apoiadorDTO.getEmail(),apoiadorDTO.getDataNasc());
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
        dbConn.update("DELETE FROM apoiador WHERE id = ?", id);
    }

    @Override
    public void update(Apoiador apoiador) {
        dbConn.update(
                "UPDATE apoiador SET nome = ?, endereco = ?, sexo = ?, fone = ?, cpf = ?, email = ?, data_nasc = ? WHERE id = ?",
                apoiador.getNome(),
                apoiador.getEndereco(),
                apoiador.getSexo(),
                apoiador.getFone(),
                apoiador.getCpf(),
                apoiador.getEmail(),
                apoiador.getDataNasc(),
                apoiador.getId()
        );
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
