package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.DoacaoMonetariaCreateDTO;
import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.exceptions.EventoNotFoundException;
import com.hbs.hbsfinan.model.DoacaoMonetaria;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.service.DoacaoMonetariaService;
import com.hbs.hbsfinan.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doacao-monetaria")
public class DoacaoMonetariaController {

    @Autowired
    DoacaoMonetariaService doacaoMonetariaService;

    @PostMapping("/novo")
    public ResponseEntity save(@Valid @RequestBody DoacaoMonetariaCreateDTO doacaoMonetaria){

        try
        {
            doacaoMonetariaService.save(doacaoMonetaria);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Daacao inserida com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            System.err.println("Erro ao salvar doacao: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity <List<DoacaoMonetaria>> findAll()
    {
        try
        {
            return ResponseEntity.ok(doacaoMonetariaService.findAll());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }


    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarDoacao(@PathVariable int id, @RequestBody DoacaoMonetaria nova) {
        try {
            nova.setId(id); // garante que o ID seja o mesmo da URL
            doacaoMonetariaService.update(nova); // chama seu método update
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // isso imprime o erro no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar doação");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoacaoMonetaria>findById(@PathVariable int id){
        try
        {
            DoacaoMonetaria doacaoMonetaria = doacaoMonetariaService.findById(id);
            return ResponseEntity.ok(doacaoMonetaria);
        }
        catch (EventoNotFoundException e)
        {
            throw new EventoNotFoundException(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<DoacaoMonetaria>findByApoiador(@RequestParam("apoiador")String apoiador)//fazer outro filtro
    {
        try
        {
            DoacaoMonetaria doacaoMonetaria = doacaoMonetariaService.findByApoiador(apoiador);
            return ResponseEntity.ok(doacaoMonetaria);
        }
        catch (EventoNotFoundException e)
        {
            throw new EventoNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/caixa/{id}")
    public ResponseEntity<List<DoacaoMonetaria>>findByCaixa(@PathVariable int id)
    {
        try
        {
            List<DoacaoMonetaria> doacaoMonetaria = doacaoMonetariaService.findByCaixa(id);
            return ResponseEntity.ok(doacaoMonetaria);
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
            doacaoMonetariaService.findById(id);
            doacaoMonetariaService.delete(id);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Doacao excluida com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch (EventoNotFoundException e)
        {
            throw new EventoNotFoundException(e.getMessage());
        }
    }







}
