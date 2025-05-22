package com.hbs.hbsfinan.controller;
//
import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.exceptions.EventoNotFoundException;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/listar")
    public ResponseEntity <List<Evento>> findAll()
    {
        try
        {
            return ResponseEntity.ok(eventoService.findAll());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody Evento evento )//Pq a data esta 1 dia a menos?
    {
        try
        {
            Evento oldEvento = eventoService.findById(id);
            if(evento.getLocal()!=null && !evento.getLocal().equals(oldEvento.getLocal()))
                oldEvento.setLocal(evento.getLocal());
            if(evento.getData()!=null && !evento.getData().equals(oldEvento.getData()))
                oldEvento.setData(evento.getData());
            if(evento.getDescricao()!=null && !evento.getDescricao().equals(oldEvento.getDescricao()))
                oldEvento.setDescricao(evento.getDescricao());
            if(evento.getNome()!=null && !evento.getNome().equals(oldEvento.getNome()))
                oldEvento.setNome(evento.getNome());
            if(evento.getMateriais()!=null && !evento.getMateriais().equals(oldEvento.getMateriais()))
                oldEvento.setMateriais(evento.getMateriais());

            eventoService.update(oldEvento);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Evento atualizado com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch (EventoNotFoundException e)
        {
            throw new EventoNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento>findById(@PathVariable int id){
        try
        {
            Evento evento = eventoService.findById(id);
            return ResponseEntity.ok(evento);
        }
        catch (EventoNotFoundException e)
        {
            throw new EventoNotFoundException(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Evento>findByNome(@RequestParam("nome")String nome)//fazer outro filtro
    {
        try
        {
            Evento evento = eventoService.findByNome(nome);
            return ResponseEntity.ok(evento);
        }
        catch (EventoNotFoundException e)
        {
            throw new EventoNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity delete(@PathVariable int id)
    {
        try
        {
            eventoService.findById(id);
            eventoService.delete(id);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Evento excluido com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch (EventoNotFoundException e)
        {
            throw new EventoNotFoundException(e.getMessage());
        }
    }
}
