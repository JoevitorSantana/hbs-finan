package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.interfaces.IFuncionarioRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FuncionarioRepository implements IFuncionarioRepository {

    private Conexao conexao;

    public FuncionarioRepository(Conexao conexao) {
        this.conexao = conexao;
    }

    public Funcionario findByCpf(String cpf) {
        Funcionario funcionario = null;
        String sql = "SELECT * FROM funcionario WHERE cpf = ?";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                funcionario = mapResultSetToFuncionario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionario;
    }

    public Funcionario findById(int id) {
        Funcionario funcionario = null;
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                funcionario = mapResultSetToFuncionario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionario;
    }

    public List<Funcionario> findAll() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";
        try (Statement stmt = conexao.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(mapResultSetToFuncionario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean save(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome, cpf, email, fone, endereco, sexo, data_nascimento) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setString(1, funcionario.getNome());
            ps.setString(2, funcionario.getCpf());
            ps.setString(3, funcionario.getEmail());
            ps.setString(4, funcionario.getFone());
            ps.setString(5, funcionario.getEndereco());
            ps.setString(6, funcionario.getSexo());
            ps.setDate(7, funcionario.getDataNascimento() != null ? Date.valueOf(funcionario.getDataNascimento()) : null);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nome = ?, cpf = ?, email = ?, fone = ?, endereco = ?, sexo = ?, data_nascimento = ? WHERE id = ?";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setString(1, funcionario.getNome());
            ps.setString(2, funcionario.getCpf());
            ps.setString(3, funcionario.getEmail());
            ps.setString(4, funcionario.getFone());
            ps.setString(5, funcionario.getEndereco());
            ps.setString(6, funcionario.getSexo());
            ps.setDate(7, funcionario.getDataNascimento() != null ? Date.valueOf(funcionario.getDataNascimento()) : null);
            ps.setInt(8, funcionario.getId());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Funcionario mapResultSetToFuncionario(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setId(rs.getInt("id"));
        f.setNome(rs.getString("nome"));
        f.setCpf(rs.getString("cpf"));
        f.setEmail(rs.getString("email"));
        f.setFone(rs.getString("fone"));
        f.setEndereco(rs.getString("endereco"));
        f.setSexo(rs.getString("sexo"));
        Date data = rs.getDate("data_nascimento");
        if (data != null) {
            f.setDataNascimento(data.toLocalDate());
        }
        return f;
    }

}
