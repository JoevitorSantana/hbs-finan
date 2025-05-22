package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eventos")
public class EventoController {
    @Autowired
    EventoService eventoService;

    @PostMapping("/novo")
    public ResponseEntity save(@Valid @RequestBody Evento evento){
        try
        {
            eventoService.save(evento);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Evento inserido com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            System.err.println("Erro ao salvar evento: " + e.getMessage());
            throw new RuntimeException();
        }
    }
}
