package com.hbs.hbsfinan.controller;


import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.exceptions.EmailExistenteException;
import com.hbs.hbsfinan.exceptions.RoleInvalidaException;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.service.ApoiadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apoiador")
public class ApoiadorController {
    @Autowired
    private ApoiadorService apoiadorService;



    //controller
    @PostMapping("/novo")
    public ResponseEntity save(@RequestBody ApoiadorDTO apoiadorDTO) {
        try {
            apoiadorService.save(apoiadorDTO);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Apoiador inserido com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (EmailExistenteException e){
            throw new EmailExistenteException(e.getMessage());
        } catch (RoleInvalidaException e) {
            throw new RoleInvalidaException(e.getMessage());
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
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Apoiador apoiador) {
        try {
            Apoiador oldApoiador = apoiadorService.findById(id);

            if (oldApoiador == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apoiador não encontrado.");

            if (apoiador.getCpf() != null && !apoiador.getCpf().equals(oldApoiador.getCpf()))
                oldApoiador.setCpf(apoiador.getCpf());

            if (apoiador.getDataNasc() != null && !apoiador.getDataNasc().equals(oldApoiador.getDataNasc()))
                oldApoiador.setDataNasc(apoiador.getDataNasc());

            apoiadorService.update(oldApoiador);
            return ResponseEntity.ok("Editado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apoiador> findById(@PathVariable int id) {
       Apoiador apoiador = apoiadorService.findById(id);
        return ResponseEntity.ok(apoiador);
    }


    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        // refatorar validação
        if (apoiadorService.findById(id) != null) {
            apoiadorService.delete(id);
            return ResponseEntity.ok("Deletado com sucesso!");
        }
        return ResponseEntity.badRequest().build();
    }



}
