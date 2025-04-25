package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping("/novo")
    public Funcionario save(@RequestBody Funcionario funcionario) {
        // Implementações à Fazer
        // 1 - validar campos vindos do body
        // 2 - Criar lógica de inserção
        //  Se o funcionário inserido, não for um usuário,
        //      Inserir registro de usuário depois inserir o registro de funcionário com id do usuario inserido
        //  Se o funcionário inserido já for um usuário, utilizar seu id para cadastrar o funcionário
        funcionarioService.save(funcionario);
        return null;
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

            if (oldFuncionario == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");

            if (funcionario.getCpf() != null && !funcionario.getCpf().equals(oldFuncionario.getCpf()))
                oldFuncionario.setCpf(funcionario.getCpf());

            if (funcionario.getFuncao() != null && !funcionario.getFuncao().equals(oldFuncionario.getFuncao()))
                oldFuncionario.setFuncao(funcionario.getFuncao());

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
