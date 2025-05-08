package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.model.Usuario;

import java.util.List;

public interface IUsuarioRepository {
    void save(UsuarioCreateDTO usuario);
    void update(Usuario usuario);
    void delete(int id );
    Usuario findById(int id);
    Usuario findByEmail(String email);
    List<Usuario> findAll();
}
