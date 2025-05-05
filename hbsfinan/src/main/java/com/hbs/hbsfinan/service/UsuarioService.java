package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.dto.UsuarioEditResponseDTO;
import com.hbs.hbsfinan.dto.UsuarioResponseDTO;
import com.hbs.hbsfinan.enums.UserRole;
import com.hbs.hbsfinan.exceptions.EmailExistenteException;
import com.hbs.hbsfinan.exceptions.RoleInvalidaException;
import com.hbs.hbsfinan.exceptions.UsuarioNotFoundException;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.repository.implementation.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public void save(UsuarioCreateDTO usuario) {
            // validar role
            if (!usuario.getRole().equalsIgnoreCase(UserRole.ADMIN.toString()) && !usuario.getRole().equalsIgnoreCase(UserRole.USER.toString())) {
                throw new RoleInvalidaException("Insira uma role válida!");
            }

            // criptografar a senha
            if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
                throw new EmailExistenteException("O email " + usuario.getEmail() + " já está em uso!");
            }

            String senhaCriptografada = encoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);

            usuarioRepository.save(usuario);
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
        Usuario usuario = usuarioRepository.findById(id);

        if (usuario == null) throw new UsuarioNotFoundException("Usuário não encontrado!");

        return usuario;
    }

    public Usuario findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) throw new UsuarioNotFoundException("Usuário não encontrado!");

        return usuario;
    }

    public UsuarioResponseDTO convertToDTO(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getUltimoNome(), usuario.getEmail());
    }

    public UsuarioEditResponseDTO convertToUsuarioEditDTO(Usuario usuario) {
        return new UsuarioEditResponseDTO(usuario.getId(), usuario.getNome(), usuario.getUltimoNome(), usuario.getEmail(), usuario.getPassword(), usuario.getRole().getRole().toUpperCase());
    }
}
