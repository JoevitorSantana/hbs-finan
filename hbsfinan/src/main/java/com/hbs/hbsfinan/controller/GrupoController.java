package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.model.Grupo;
import com.hbs.hbsfinan.service.GrupoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
    @Autowired
    GrupoService grupoService;

    @PostMapping("/novo")
    public ResponseEntity save(@Valid @RequestBody Grupo grupo){
        try
        {
            grupoService.save(grupo);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Grupo inserido com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            System.err.println("Erro ao salvar grupo: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Grupo>> findAll(){
        try
        {
            return ResponseEntity.ok(grupoService.findAll());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }return ResponseEntity.badRequest().build();
    }
}
