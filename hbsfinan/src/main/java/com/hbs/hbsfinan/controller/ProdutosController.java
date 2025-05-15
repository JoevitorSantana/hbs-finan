package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.dto.UsuarioResponseDTO;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.service.ProdutosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/produtos")
public class ProdutosController {

    @Autowired
    ProdutosService produtosService;

    @PostMapping("/novo")
    public ResponseEntity save(@Valid @RequestBody Produtos produtos){
        try
        {
            produtosService.save(produtos);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Grupo inserudo com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            System.err.println("Erro ao salvar produto: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Produtos>> findAll() {
        try {
            return ResponseEntity.ok(produtosService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        } return ResponseEntity.badRequest().build();
    }
}
