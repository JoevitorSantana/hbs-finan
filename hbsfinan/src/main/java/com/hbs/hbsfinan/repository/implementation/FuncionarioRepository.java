package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.interfaces.IFuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FuncionarioRepository implements IFuncionarioRepository {

    @Autowired
    private JdbcTemplate dbConn;

    private RowMapper<Funcionario> rowMapper = (rs, rowNum) -> {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(rs.getInt("id"));
        funcionario.setCpf(rs.getString("cpf"));
        funcionario.setFuncao(rs.getString("funcao"));
        funcionario.setDataNascimento(rs.getDate("data_nascimento"));
        return funcionario;
    };

    @Override
    public void save(Funcionario funcionario) {
        dbConn.update("INSERT INTO funcionario (cpf, funcao, data_nascimento) VALUES (?,?,?)", funcionario.getCpf(), funcionario.getFuncao(), funcionario.getDataNascimento());
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
        dbConn.update("UPDATE funcionario SET cpf = ?, funcao = ?, data_nascimento = ? WHERE id = ?", funcionario.getCpf(), funcionario.getFuncao(), funcionario.getDataNascimento(), funcionario.getId());
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
