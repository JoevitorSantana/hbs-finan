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
