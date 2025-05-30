package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.exceptions.AnotacoesNotFoundException;
import com.hbs.hbsfinan.exceptions.EventoNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Anotacoes;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.service.AnotacoesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anotacoes")
public class AnotacoesController {
    AnotacoesService anotacoesService;
    private Conexao dbConnFactory;

    public AnotacoesController(){
        this.dbConnFactory = Conexao.getInstance();
        this.anotacoesService = new AnotacoesService(dbConnFactory);
    }

    @PostMapping("/novo")
    public ResponseEntity save(@Valid @RequestBody Anotacoes anotacoes){
        try {
            anotacoesService.save(anotacoes);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Anotação inserido com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }catch (Exception e)
        {
            System.err.println("Erro ao salvar anotação: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity <List<Anotacoes>> findAll(){
        try
        {
            return ResponseEntity.ok(anotacoesService.findAll());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody Anotacoes anotacoes){
        try
        {
            Anotacoes oldAnotacao = anotacoesService.findById(id);
            if(anotacoes.getAnotacao()!=null && !anotacoes.getAnotacao().equals(oldAnotacao.getAnotacao()))
                oldAnotacao.setAnotacao(anotacoes.getAnotacao());
            if(anotacoes.getData()!=null && !anotacoes.getData().equals(oldAnotacao.getData()))
                oldAnotacao.setData(anotacoes.getData());

            anotacoesService.update(oldAnotacao);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Anotação atualizado com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch (AnotacoesNotFoundException e)
        {
            throw new AnotacoesNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anotacoes> findById(@PathVariable int id){
        try
        {
            Anotacoes anotacao = anotacoesService.findById(id);
            return ResponseEntity.ok(anotacao);
        }
        catch(AnotacoesNotFoundException e)
        {
            throw new AnotacoesNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity delete(@PathVariable int id)
    {
        try
        {
            anotacoesService.findById(id);
            anotacoesService.delete(id);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Anotação excluida com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch(AnotacoesNotFoundException e)
        {
            throw new AnotacoesNotFoundException(e.getMessage());
        }
    }
}
