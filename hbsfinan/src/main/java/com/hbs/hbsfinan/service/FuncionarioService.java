
package com.hbs.hbsfinan.service;
import com.hbs.hbsfinan.dto.FuncionarioCreateDTO;
import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.enums.UserRole;
import com.hbs.hbsfinan.exceptions.EmailExistenteException;
import com.hbs.hbsfinan.exceptions.FuncionarioNotFoundException;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.repository.implementation.FuncionarioRepository;
import com.hbs.hbsfinan.repository.implementation.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public FuncionarioService(FuncionarioRepository funcionarioRepository, UsuarioRepository usuarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void save(FuncionarioCreateDTO dto) {
        // Verificação básica de CPF duplicado
        if (funcionarioRepository.findByCpf(dto.getCpf()) != null) {
            throw new RuntimeException("CPF já está cadastrado");
        }

        Usuario usuario = null;

        // Verificar se já existe usuário no DTO
        if (dto.getUsuario() != null) {
            // Se usuário no DTO, verificar se email já existe
            if (usuarioRepository.findByEmail(dto.getUsuario().getEmail()) != null) {
                throw new EmailExistenteException("Email já está em uso!");
            }
            UsuarioCreateDTO usuarioDTO = dto.getUsuario();
            usuario = new Usuario();
            usuario.setNome(usuarioDTO.getNome());
            usuario.setUltimoNome(usuarioDTO.getUltimoNome());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setSenha(encoder.encode(usuarioDTO.getSenha())); // criptografia da senha
            usuario.setRole(UserRole.valueOf(usuarioDTO.getRole().toUpperCase()));

            boolean usuarioSalvo = usuarioRepository.save(usuario);
            if (!usuarioSalvo) {
                throw new RuntimeException("Erro ao salvar usuário");
            }
        } else {
            // Se não vier dados de usuário no DTO, criar usuário automático com dados padrão

            // Por exemplo, usar o email do funcionário como email do usuário,
            // gerar uma senha padrão (exemplo "123456" — mas idealmente gerar uma senha aleatória e avisar o funcionário)
            String email = dto.getEmail();
            if (usuarioRepository.findByEmail(email) != null) {
                throw new RuntimeException("Email do funcionário já está vinculado a outro usuário");
            }
            usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setUltimoNome(""); // Ou deixar vazio
            usuario.setEmail(email);
            usuario.setSenha(encoder.encode("senhaPadrao123")); // Troque por senha segura ou gere aleatória
            usuario.setRole(UserRole.USER); // Exemplo de role padrão

            boolean usuarioSalvo = usuarioRepository.save(usuario);
            if (!usuarioSalvo) {
                throw new RuntimeException("Erro ao salvar usuário");
            }
        }

        // Criação do funcionário
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(dto.getNome());
        funcionario.setCpf(dto.getCpf());
        funcionario.setEmail(dto.getEmail());
        funcionario.setFone(dto.getFone());
        funcionario.setEndereco(dto.getEndereco());
        funcionario.setSexo(dto.getSexo());
        funcionario.setDataNascimento(dto.getDataNascimento());

        // Associando usuário ao funcionário
        funcionario.setUsuario(usuario);

        // Salvando funcionário no banco
        boolean funcionarioSalvo = funcionarioRepository.save(funcionario);
        if (!funcionarioSalvo) {
            throw new RuntimeException("Erro ao salvar funcionário");
        }
    }



    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public Funcionario findById(int id) {
        Funcionario f = funcionarioRepository.findById(id);
        if (f == null) {
            throw new  FuncionarioNotFoundException("Funcionário não encontrado.");
        }
        return f;
    }

    public void update(Funcionario funcionario) {
        boolean updated = funcionarioRepository.update(funcionario);
        if (!updated) {
            throw new RuntimeException("Erro ao atualizar funcionário");
        }
    }

    public void delete(int id) {
        boolean deleted = funcionarioRepository.delete(id);
        if (!deleted) {
            throw new RuntimeException("Erro ao deletar funcionário");
        }
    }
}

