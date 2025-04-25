package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.repository.implementation.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public void save(Usuario usuario) throws Exception {
        try {
            // criptografar a senha
            if (usuarioRepository.findByEmail(usuario.getEmail()) != null) throw new Exception("Usuário já existe!");

            String senhaCriptografada = encoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);
            // usuario.setSenha(securityConfiguration.passwordEncoder().encode(usuario.getSenha()));
            usuarioRepository.save(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public void delete(int id) {
        usuarioRepository.delete(id);
    }

    public void update(Usuario usuario) {
        usuarioRepository.update(usuario);
    }

    public Usuario findById(int id) {
        return usuarioRepository.findById(id);
    }
}
