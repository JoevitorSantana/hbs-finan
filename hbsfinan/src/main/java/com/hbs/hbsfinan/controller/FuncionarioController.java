package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.FuncionarioCreateDTO;
import com.hbs.hbsfinan.exceptions.FuncionarioNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.implementation.FuncionarioRepository;
import com.hbs.hbsfinan.repository.implementation.UsuarioRepository;
import com.hbs.hbsfinan.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController() {
        Conexao conexao = Conexao.getInstance();
        FuncionarioRepository funcionarioRepository = new FuncionarioRepository(conexao);
        UsuarioRepository usuarioRepository = new UsuarioRepository(conexao);
        this.funcionarioService = new FuncionarioService(funcionarioRepository, usuarioRepository);
    }


    @PostMapping("/novo")
    public ResponseEntity<?> save(@Valid @RequestBody FuncionarioCreateDTO funcionariodto) {
        try {
            funcionarioService.save(funcionariodto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Funcionário criado com sucesso.");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("CPF já está cadastrado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("message", "CPF já está cadastrado"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erro ao criar funcionário: " + e.getMessage()));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Funcionario>> findAll() {
        try {
            List<Funcionario> funcionarios = funcionarioService.findAll();
            return ResponseEntity.ok(funcionarios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        try {
            Funcionario f = funcionarioService.findById(id);
            return ResponseEntity.ok(f);
        } catch (FuncionarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erro ao buscar funcionário"));
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Funcionario funcionario) {
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
            if (funcionario.getCpf() != null && !funcionario.getCpf().equals(oldFuncionario.getCpf()))
                oldFuncionario.setCpf(funcionario.getCpf());
            if (funcionario.getDataNascimento() != null && !funcionario.getDataNascimento().equals(oldFuncionario.getDataNascimento()))
                oldFuncionario.setDataNascimento(funcionario.getDataNascimento());

            funcionarioService.update(oldFuncionario);
            return ResponseEntity.ok(Collections.singletonMap("message", "Editado com sucesso!"));
        } catch (FuncionarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Erro ao editar funcionário"));
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            funcionarioService.findById(id); // Garante que existe, senão lança exceção
            funcionarioService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Deletado com sucesso!"));
        } catch (FuncionarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erro ao deletar funcionário"));
        }
    }

}