package com.hbs.hbsfinan.controller;


import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.exceptions.EmailExistenteException;
import com.hbs.hbsfinan.exceptions.RoleInvalidaException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.service.ApoiadorService;
import com.hbs.hbsfinan.service.GrupoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apoiador")
public class ApoiadorController {


    private Conexao dbConnFactory;

    ApoiadorService apoiadorService;


    public ApoiadorController(){
        this.dbConnFactory = Conexao.getInstance();
        this.apoiadorService = new ApoiadorService(dbConnFactory);
    }


    //controller
    @PostMapping("/novo")
    public ResponseEntity save(@RequestBody Apoiador apoiador) {
        try {
            apoiadorService.save(apoiador);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Apoiador inserido com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (Exception e)
        {
            System.err.println("Erro ao salvar apoiador: " + e.getMessage());
            throw new RuntimeException();
        }
    }


    @GetMapping("/listar")
    public ResponseEntity<List<Apoiador>> findAll() {
        try
        {
            return ResponseEntity.ok(apoiadorService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }


    @PutMapping("/editar/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Apoiador apoiador) {
        try {
            Apoiador oldApoiador = apoiadorService.findById(id);

            if (oldApoiador == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apoiador não encontrado.");

            if (apoiador.getCpf() != null && !apoiador.getCpf().equals(oldApoiador.getCpf()))
                oldApoiador.setCpf(apoiador.getCpf());

            if (apoiador.getDataNasc() != null && !apoiador.getDataNasc().equals(oldApoiador.getDataNasc()))
                oldApoiador.setDataNasc(apoiador.getDataNasc());

            if(apoiador.getEmail() != null && !apoiador.getEmail().equals(oldApoiador.getEmail()))
                oldApoiador.setEmail(apoiador.getEmail());

            if(apoiador.getFone() != null && !apoiador.getFone().equals(oldApoiador.getFone()))
                oldApoiador.setFone(apoiador.getFone());

            if (apoiador.getNome()!= null && !apoiador.getNome().equals(oldApoiador.getNome()))
                oldApoiador.setNome(apoiador.getNome());

            if (apoiador.getSexo()!= null && !apoiador.getSexo().equals(oldApoiador.getSexo()))
                oldApoiador.setSexo(apoiador.getSexo());

            if (apoiador.getDataNasc()!= null && !apoiador.getSexo().equals(oldApoiador.getDataNasc()))
                oldApoiador.setDataNasc(apoiador.getDataNasc());

            if (apoiador.getEndereco() != null && !apoiador.getEndereco().equals(oldApoiador.getEndereco()))
                oldApoiador.setEndereco(apoiador.getEndereco());

            apoiadorService.update(oldApoiador);
            return ResponseEntity.ok("Editado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apoiador> findById(@PathVariable Long id) {
       Apoiador apoiador = apoiadorService.findById(id);
        return ResponseEntity.ok(apoiador);
    }


    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (apoiadorService.findById(id) != null) {
            apoiadorService.delete(id);
            return ResponseEntity.ok("Deletado com sucesso!");
        }
        return ResponseEntity.badRequest().build();
    }



}
