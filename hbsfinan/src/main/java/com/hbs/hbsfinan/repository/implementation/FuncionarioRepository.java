package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.dto.FuncionarioCreateDTO;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.interfaces.IFuncionarioRepository;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FuncionarioRepository implements IFuncionarioRepository {

    private Conexao dbConn;

    public FuncionarioRepository(Conexao dbConn) {
        this.dbConn = dbConn;
    }

    @Override
    public void save(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome,email,fone,endereco,data_nascimento,sexo,cpf) VALUES ('#1','#2','#3','#4','#5','#6','#7')";
        sql = sql.replace("#1", funcionario.getNome());
        sql = sql.replace("#2", funcionario.getEmail());
        sql = sql.replace("#3", funcionario.getFone());
        sql = sql.replace("#4", funcionario.getEndereco());
        sql = sql.replace("#5", funcionario.getDataNascimento().toString());
        sql = sql.replace("#6", funcionario.getSexo());
        sql = sql.replace("#7", funcionario.getCpf());

        dbConn.update(sql);
    }

    @Override
    public Funcionario findById(int id) {
        Funcionario funcionario = null;
        String sql = "SELECT * FROM funcionario WHERE id = " + id;

        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                funcionario = mapResultSetToFuncionario(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return funcionario;
    }

    @Override
    public List<Funcionario> findAll() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";

        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                lista.add(mapResultSetToFuncionario(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM funcionario WHERE id = " + id;
        dbConn.update(sql);
    }

    @Override
    public void update(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nome = '#1', email = '#2', fone = '#3', endereco = '#4', data_nascimento = '#5', sexo = '#6', cpf = '#7' WHERE id = #8";
        sql = sql.replace("#1", funcionario.getNome());
        sql = sql.replace("#2", funcionario.getEmail());
        sql = sql.replace("#3", funcionario.getFone());
        sql = sql.replace("#4", funcionario.getEndereco());
        sql = sql.replace("#5", funcionario.getDataNascimento().toString());
        sql = sql.replace("#6", funcionario.getSexo());
        sql = sql.replace("#7", funcionario.getCpf());
        sql = sql.replace("#8", String.valueOf(funcionario.getId()));

        dbConn.update(sql);
    }

    @Override
    public Funcionario findByEmail(String email) {
        Funcionario funcionario = null;
        String sql = "SELECT * FROM funcionario WHERE email = '" + email + "'";

        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                funcionario = mapResultSetToFuncionario(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return funcionario;
    }

    @Override
    public Funcionario findByCpf(String cpf) {
        Funcionario funcionario = null;
        String sql = "SELECT * FROM funcionario WHERE cpf = '" + cpf + "'";

        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                funcionario = mapResultSetToFuncionario(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return funcionario;
    }

    private Funcionario mapResultSetToFuncionario(ResultSet rs) throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(rs.getInt("id"));
        funcionario.setNome(rs.getString("nome"));
        funcionario.setEmail(rs.getString("email"));
        funcionario.setFone(rs.getString("fone"));
        funcionario.setEndereco(rs.getString("endereco"));
        funcionario.setDataNascimento(rs.getDate("data_nascimento"));
        funcionario.setSexo(rs.getString("sexo"));
        funcionario.setCpf(rs.getString("cpf"));
        return funcionario;
    }
}
