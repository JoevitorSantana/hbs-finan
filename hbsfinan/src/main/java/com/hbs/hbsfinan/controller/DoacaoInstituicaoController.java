package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.DoacaoInstituicaoCreateDTO;
import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.exceptions.DoacaoInstituicaoNotFoundException;
import com.hbs.hbsfinan.model.DoacaoInstituicao;
import com.hbs.hbsfinan.service.DoacaoInstituicaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doacao-instituicao")
public class DoacaoInstituicaoController {

    @Autowired
    private DoacaoInstituicaoService doacaoInstituicaoService;

    @PostMapping("/novo")
    public ResponseEntity save(@Valid @RequestBody DoacaoInstituicaoCreateDTO dto) {
        try {
            doacaoInstituicaoService.save(dto);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Doação de instituição inserida com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar doação de instituição: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<DoacaoInstituicao>> findAll() {
        try {
            return ResponseEntity.ok(doacaoInstituicaoService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable int id, @RequestBody DoacaoInstituicao nova) {
        try {
            nova.setId(id);
            doacaoInstituicaoService.update(nova);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar doação de instituição.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoacaoInstituicao> findById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(doacaoInstituicaoService.findById(id));
        } catch (DoacaoInstituicaoNotFoundException e) {
            throw new DoacaoInstituicaoNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/caixa/{id}")
    public ResponseEntity<List<DoacaoInstituicao>> findByCaixa(@PathVariable int id) {
        try {
            return ResponseEntity.ok(doacaoInstituicaoService.findByCaixa(id));
        } catch (DoacaoInstituicaoNotFoundException e) {
            throw new DoacaoInstituicaoNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        try {
            doacaoInstituicaoService.findById(id);
            doacaoInstituicaoService.delete(id);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Doação de instituição excluída com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (DoacaoInstituicaoNotFoundException e) {
            throw new DoacaoInstituicaoNotFoundException(e.getMessage());
        }
    }
}