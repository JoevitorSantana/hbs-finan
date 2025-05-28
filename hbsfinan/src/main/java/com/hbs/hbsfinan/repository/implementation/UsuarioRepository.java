package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.enums.UserRole;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.infra.db.SingletonDB;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.repository.interfaces.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioRepository implements IUsuarioRepository {

    private Conexao dbConn;// = SingletonDB.getConexao();

    public UsuarioRepository(Conexao dbConn) {
        this.dbConn = dbConn;
    }

    @Override
    public void save(UsuarioCreateDTO usuario) {
        String sql = "INSERT INTO usuario (nome, ultimo_nome, email, senha, role) VALUES ('#1','#2','#3','#4','#5')";
        sql=sql.replace("#1", usuario.getNome());
        sql=sql.replace("#2", usuario.getUltimoNome());
        sql=sql.replace("#3", usuario.getEmail());
        sql=sql.replace("#4", usuario.getSenha());
        sql=sql.replace("#5", usuario.getRole().toUpperCase());
        dbConn.update(sql);
    }

    @Override
    public void update(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = '#1', ultimo_nome = '#2', email = '#3', senha = '#4', role = '#5' WHERE id =#6";
        sql=sql.replace("#1", usuario.getNome());
        sql=sql.replace("#2", usuario.getUltimoNome());
        sql=sql.replace("#3", usuario.getEmail());
        sql=sql.replace("#4", usuario.getSenha());
        sql=sql.replace("#5", usuario.getRole().getRole().toUpperCase());
        sql=sql.replace("#6",""+usuario.getId());
        dbConn.update(sql);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM usuario WHERE id =#1";
        sql=sql.replace("#1",""+id);
        return dbConn.update(sql);
    }

    @Override
    public Usuario findById(int id) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE id =#1";

        sql=sql.replace("#1",""+id);

        try {
            ResultSet rs = dbConn.query(sql);

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setUltimoNome(rs.getString("ultimo_nome"));
                usuario.setRole(UserRole.valueOf(rs.getString("role")));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return usuario;
    }

    @Override
    public Usuario findByEmail(String email) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE email = '#1'";

        sql=sql.replace("#1", email);

        try {
            ResultSet rs = dbConn.query(sql);

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setUltimoNome(rs.getString("ultimo_nome"));
                usuario.setRole(UserRole.valueOf(rs.getString("role")));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return usuario;
    }

    @Override
    public List<Usuario> findAll() {
        List <Usuario> listUsuario = new ArrayList<>();
        String sql="SELECT * FROM usuario";

        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setUltimoNome(rs.getString("ultimo_nome"));
                usuario.setRole(UserRole.valueOf(rs.getString("role")));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                listUsuario.add(usuario);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listUsuario;
    }
}
