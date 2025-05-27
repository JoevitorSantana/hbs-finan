package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.dto.UsuarioEditResponseDTO;
import com.hbs.hbsfinan.dto.UsuarioResponseDTO;
import com.hbs.hbsfinan.enums.UserRole;
import com.hbs.hbsfinan.exceptions.EmailExistenteException;
import com.hbs.hbsfinan.exceptions.ErroExclusaoException;
import com.hbs.hbsfinan.exceptions.RoleInvalidaException;
import com.hbs.hbsfinan.exceptions.UsuarioNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.repository.implementation.ParametrizacaoRepository;
import com.hbs.hbsfinan.repository.implementation.UsuarioRepository;
import com.hbs.hbsfinan.repository.interfaces.IParametrizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    // @Autowired
    private UsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private Conexao dbConnFactory;

    public UsuarioService() {}

    public UsuarioService(Conexao dbConnFactory) {
        this.dbConnFactory = dbConnFactory;
        this.usuarioRepository = new UsuarioRepository(dbConnFactory);
    }

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
        // Validar quantos usuários admin existem
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null)
            throw new UsuarioNotFoundException("Usuário não encontrado!");
        if (usuario.getRole().getRole().equalsIgnoreCase("ADMIN") && quantidadeUsuariosAdmin() < 2)
            throw new ErroExclusaoException("Não é possível excluir o último administrador!");
        if (!usuarioRepository.delete(id))
            throw new ErroExclusaoException("Erro ao excluir usuário!");
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

    private boolean validarExclusao()
    {
        if (quantidadeUsuariosAdmin() < 2)
            return false;
        return true;
    }

    private int quantidadeUsuariosAdmin() {
        int quantidade = 0;
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            if (usuario.getRole().getRole().equalsIgnoreCase("ADMIN"))
                quantidade++;
        }
        return quantidade;
    }

    public int quantidadeUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.size();
    }

    private void criarUsuarioPrimeiroAcesso() {
        UsuarioCreateDTO usuario = new UsuarioCreateDTO();
        usuario.setNome("Administrador");
        usuario.setUltimoNome("Administrador");
        usuario.setEmail("admin@admin.com");
        usuario.setSenha("admin123");
        usuario.setRole("admin");
        this.save(usuario);
    }

    public void validarUsuarioPrimeiroAcesso() {
        if (this.quantidadeUsuarios() == 0) {
            criarUsuarioPrimeiroAcesso();
        } else {
//            ParametrizacaoRepository parametroRepository = new ParametrizacaoRepository();
//            ParametrizacaoService parametrizacaoService = new ParametrizacaoService(parametroRepository);
//            if (parametrizacaoService.exists()) {
//                // buscar usuario admin e excluir
//                Usuario usuario = usuarioRepository.findByEmail("admin@admin.com");
//                if (usuario != null)
//                    usuarioRepository.delete(usuario.getId());
//            }
        }
    }
}
