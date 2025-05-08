package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.interfaces.IFuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class FuncionarioRepository implements IFuncionarioRepository {

    @Autowired
    private JdbcTemplate dbConn;

    private RowMapper<Funcionario> rowMapper = (rs, rowNum) -> {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(rs.getInt("id"));
        funcionario.setNome(rs.getString("nome"));
        funcionario.setEmail(rs.getString("email"));
        funcionario.setDataNascimento(rs.getDate("data_nascimento"));
        funcionario.setFone(rs.getString("fone"));
        funcionario.setEndereco(rs.getString("endereco"));
        funcionario.setSexo(rs.getString("sexo"));
        funcionario.setCpf(rs.getString("cpf"));

        return funcionario;
    };

    private int id;
    private String nome;
    private String email;
    private Date dataNascimento;
    private String fone;
    private String endereco;
    private String sexo;
    private String cpf;

    @Override
    public void save(Funcionario funcionario) {
        dbConn.update("INSERT INTO funcionario (nome,email,data_nascimento,fone,endereco,sexo,cpf) VALUES (?,?,?,?,?,?,?)",funcionario.getNome(),funcionario.getEmail(),funcionario.getDataNascimento(),funcionario.getFone(),funcionario.getEndereco(),funcionario.getSexo(), funcionario.getCpf());
    }

    @Override
    public Funcionario findById(int id) {
        return dbConn.queryForObject("SELECT * FROM funcionario WHERE id = ?", rowMapper, id);
    }

    @Override
    public List<Funcionario> findAll() {
        return dbConn.query("SELECT * FROM funcionario", rowMapper);
    }

    @Override
    public void delete(int id) {
        dbConn.update("DELETE FROM funcionario WHERE id = ?", id);
    }

    @Override
    public void update(Funcionario funcionario) {
        dbConn.update("UPDATE funcionario SET nome = ?, email = ?,data_nascimento = ?,fone = ?, endereco = ?, sexo = ?, cpf = ? WHERE id = ?", funcionario.getNome(),funcionario.getEmail(),funcionario.getDataNascimento(),funcionario.getFone(),funcionario.getEndereco(),funcionario.getSexo(), funcionario.getCpf(), funcionario.getId());
    }

    @Override
    public Funcionario findByEmail(String email) {
        try {
            return dbConn.queryForObject("SELECT * FROM funcionario WHERE email = ?", rowMapper, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
