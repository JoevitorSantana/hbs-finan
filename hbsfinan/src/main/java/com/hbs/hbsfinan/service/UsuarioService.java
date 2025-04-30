package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.UsuarioEditResponseDTO;
import com.hbs.hbsfinan.dto.UsuarioResponseDTO;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.repository.implementation.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<UsuarioResponseDTO> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDTO> usuariosRetorno = new ArrayList<>();
        for (Usuario usuario : usuarios)
            usuariosRetorno.add(convertToDTO(usuario));
        return usuariosRetorno;
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

    public UsuarioResponseDTO convertToDTO(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getUltimoNome(), usuario.getEmail());
    }

    public UsuarioEditResponseDTO convertToUsuarioEditDTO(Usuario usuario) {
        return new UsuarioEditResponseDTO(usuario.getId(), usuario.getNome(), usuario.getUltimoNome(), usuario.getEmail(), usuario.getPassword(), usuario.getRole().getRole().toUpperCase());
    }
}
