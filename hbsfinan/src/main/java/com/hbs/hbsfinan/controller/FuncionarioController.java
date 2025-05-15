package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.FuncionarioCreateDTO;
import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.repository.interfaces.IUsuarioRepository;
import com.hbs.hbsfinan.service.FuncionarioService;
import com.hbs.hbsfinan.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {


    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    //@Autowired
    //private BCryptPasswordEncoder encoder; // para criptografar senha

//    @PostMapping("/novo")
//    public ResponseEntity<?> save(@Valid @RequestBody FuncionarioCreateDTO funcionariodto) {
//
//        Usuario usuario = usuarioRepository.findByEmail(funcionariodto.getEmail());
//        if (usuario != null) {
//            return ResponseEntity.badRequest().body("Email já cadastrado para outro usuário.");
//        }
//        // Cria DTO para salvar Usuario
//        UsuarioCreateDTO usuarioDTO = new UsuarioCreateDTO();
//        usuarioDTO.setNome(funcionariodto.getNome());
//        usuarioDTO.setUltimoNome(""); // se quiser setar, ou deixar vazio
//        usuarioDTO.setEmail(funcionariodto.getEmail());
//        //usuarioDTO.setSenha(encoder.encode("123456"));  // senha padrão já criptografada
//        usuarioDTO.setRole("USER");
//
//        // Salva usuário diretamente no repositório
//        usuarioRepository.save(usuarioDTO);
//
//        // Recupera o usuário salvo para associar ao funcionário
//        Usuario usuarioSalvo = usuarioRepository.findByEmail(funcionariodto.getEmail());
//
//        // Cria e salva funcionário associando o usuário
//        FuncionarioCreateDTO funcionario = new FuncionarioCreateDTO();
//        funcionario.setNome(funcionariodto.getNome());
//        funcionario.setEmail(funcionariodto.getEmail());
//        funcionario.setFone(funcionariodto.getFone());
//        funcionario.setEndereco(funcionariodto.getEndereco());
//        funcionario.setDataNascimento(funcionariodto.getDataNascimento());
//        funcionario.setSexo(funcionariodto.getSexo());
//        funcionario.setCpf(funcionariodto.getCpf());
//        //funcionario.setUsuario(usuarioSalvo);
//
//        funcionarioService.save(funcionario);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body("Funcionário criado com sucesso.");
//
//
//    }

    @PostMapping("/novo")
    public ResponseEntity<?> save(@Valid @RequestBody FuncionarioCreateDTO funcionariodto) {
        try {
            funcionarioService.save(funcionariodto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Funcionário criado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar funcionário: " + e.getMessage());
        }
    }


    // Implementações à Fazer
    // 1 - validar campos vindos do body
    // 2 - Criar lógica de inserção
    //  Se o funcionário inserido, não for um usuário,
    //      Inserir registro de usuário depois inserir o registro de funcionário com id do usuario inserido
    //  Se o funcionário inserido já for um usuário, utilizar seu id para cadastrar o funcionário


    @GetMapping("/listar")
    public ResponseEntity<List<Funcionario>> findAll() {
        try
        {
            return ResponseEntity.ok(funcionarioService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Funcionario funcionario) {
        try {
            Funcionario oldFuncionario = funcionarioService.findById(id);

            if (funcionario.getNome() != null)
                oldFuncionario.setNome(funcionario.getNome());

            if (funcionario.getEmail() != null)
                oldFuncionario.setEmail(funcionario.getEmail());

            if (funcionario.getFone() != null)
                oldFuncionario.setFone(funcionario.getFone());

            if (funcionario.getEndereco() != null)
                oldFuncionario.setEndereco(funcionario.getEndereco());

            if (funcionario.getSexo() != null)
                oldFuncionario.setSexo(funcionario.getSexo());

            if (oldFuncionario == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");

            if (funcionario.getCpf() != null && !funcionario.getCpf().equals(oldFuncionario.getCpf()))
                oldFuncionario.setCpf(funcionario.getCpf());

            if (funcionario.getDataNascimento() != null && !funcionario.getDataNascimento().equals(oldFuncionario.getDataNascimento()))
                oldFuncionario.setDataNascimento(funcionario.getDataNascimento());

            funcionarioService.update(oldFuncionario);
            return ResponseEntity.ok("Editado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> findById(@PathVariable int id) {
        Funcionario funcionario = funcionarioService.findById(id);
        return ResponseEntity.ok(funcionario);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        // refatorar validação
        if (funcionarioService.findById(id) != null) {
            funcionarioService.delete(id);
            return ResponseEntity.ok("Deletado com sucesso!");
        }
        return ResponseEntity.badRequest().build();
    }
}
