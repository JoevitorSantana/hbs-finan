package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.enums.UserRole;
import com.hbs.hbsfinan.exceptions.UsuarioNotFoundException;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.repository.interfaces.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioRepository implements IUsuarioRepository {

    @Autowired
    private JdbcTemplate dbConn;

    private RowMapper<Usuario> rowMapper = (rs, rowNum) -> {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setUltimoNome(rs.getString("ultimo_nome"));
        usuario.setRole(UserRole.valueOf(rs.getString("role")));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        return usuario;
    };

    @Override
    public void save(UsuarioCreateDTO usuario) {
        dbConn.update("INSERT INTO usuario (nome, ultimo_nome, email, senha, role) VALUES (?,?,?,?,?)", usuario.getNome(), usuario.getUltimoNome(), usuario.getEmail(), usuario.getSenha(), usuario.getRole().toUpperCase());
    }

    @Override
    public void update(Usuario usuario) {
        dbConn.update("UPDATE usuario SET nome = ?, ultimo_nome = ?, email = ?, senha = ?, role = ? WHERE id = ?", usuario.getNome(), usuario.getUltimoNome(), usuario.getEmail(), usuario.getSenha(), usuario.getRole().getRole().toUpperCase(), usuario.getId());
    }

    @Override
    public void delete(int id) {
        dbConn.update("DELETE FROM usuario WHERE id = ?", id);
    }

    @Override
    public Usuario findById(int id) {
        try {
            return dbConn.queryForObject("SELECT * FROM usuario WHERE id = ?", rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Usuario findByEmail(String email) {
        try {
            return dbConn.queryForObject("SELECT * FROM usuario WHERE email = ?", rowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Usuario> findAll() {
        return dbConn.query("SELECT * FROM usuario", rowMapper);
    }
}
