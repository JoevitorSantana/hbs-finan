package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.exceptions.ParametrizacaoJaCadastradaException;
import com.hbs.hbsfinan.exceptions.ParametrizacaoNaoEncontradaException;
import com.hbs.hbsfinan.model.Parametrizacao;
import com.hbs.hbsfinan.service.ParametrizacaoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;

/**
 * Controller REST para gerenciar a parametrização da empresa.
 */
@RestController
@RequestMapping("/api/parametrizacao")
@Validated
public class ParametrizacaoController {

    private final ParametrizacaoService service;

    public ParametrizacaoController(ParametrizacaoService service) {
        this.service = service;
    }

    /**
     * Retorna a parametrização existente.
     * @return 200 OK com o objeto, ou 404 se não existir.
     */
    @GetMapping
    public ResponseEntity<Parametrizacao> get() {
        Parametrizacao p = service.get();
        return ResponseEntity.ok(p);
    }

    /**
     * Cria a parametrização. Só permite se não existir.
     * @param dto dados da parametrização.
     * @param uriBuilder para montar cabeçalho Location.
     * @return 201 Created com header Location, ou 409 Conflict.
     */
    @PostMapping
    public ResponseEntity<Parametrizacao> create(
            @Valid @RequestBody Parametrizacao dto /*,
            UriComponentsBuilder uriBuilder*/) {
        try {
            Parametrizacao criada = service.create(dto);
            return ResponseEntity.ok(criada);
            //HttpHeaders headers = new HttpHeaders();
            // headers.setLocation(uriBuilder.path("/api/parametrizacao").build().toUri());
            // return new ResponseEntity<>(criada, headers, HttpStatus.CREATED);
        } catch (ParametrizacaoJaCadastradaException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Atualiza a parametrização existente.
     * @param dto dados atualizados.
     * @return 200 OK com o objeto alterado, ou 404 Not Found.
     */
    @PutMapping
    public ResponseEntity<Parametrizacao> update(@Valid @RequestBody Parametrizacao dto) {
        try {
            Parametrizacao atualizada = service.update(dto);
            return ResponseEntity.ok(atualizada);
        } catch (ParametrizacaoNaoEncontradaException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
