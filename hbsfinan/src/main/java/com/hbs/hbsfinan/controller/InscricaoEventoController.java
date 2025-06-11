package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.exceptions.InscricaoEventoNotFoundException;
import com.hbs.hbsfinan.exceptions.ApoiadorNotFoundException; // Importar exceções de validação
import com.hbs.hbsfinan.exceptions.EventoNotFoundException;   // Importar exceções de validação
import com.hbs.hbsfinan.infra.db.Conexao;
// import com.hbs.hbsfinan.infra.db.SingletonDB; // Não é mais necessário, já que usamos Conexao.getInstance()
import com.hbs.hbsfinan.model.InscricaoEvento;
import com.hbs.hbsfinan.repository.implementation.ApoiadorRepository; // Necessário para instanciar a Service
import com.hbs.hbsfinan.repository.implementation.EventoRepository;     // Necessário para instanciar a Service
import com.hbs.hbsfinan.repository.implementation.InscricaoEventoRepository; // Necessário para instanciar a Service
import com.hbs.hbsfinan.service.InscricaoEventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscricoes-evento")
public class InscricaoEventoController {
    private Conexao dbConnFactory;
    private InscricaoEventoService inscricaoEventoService;

    public InscricaoEventoController() {
        this.dbConnFactory = Conexao.getInstance();
        if (dbConnFactory == null || !dbConnFactory.getEstadoConexao()) {
            throw new RuntimeException("Falha na inicialização da Conexao no InscricaoEventoController.");
        }
        this.inscricaoEventoService = new InscricaoEventoService(dbConnFactory);
    }

    @PostMapping("/nova")
    public ResponseEntity<RestResponseMessage> save(@Valid @RequestBody InscricaoEvento inscricaoEvento) {
        try {
            inscricaoEventoService.save(inscricaoEvento);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Inscrição realizada com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (ApoiadorNotFoundException e) {
            RestResponseMessage message = new RestResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (EventoNotFoundException e) {
            RestResponseMessage message = new RestResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (Exception e){
            e.printStackTrace();
            RestResponseMessage message = new RestResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao realizar inscrição: " + e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<InscricaoEvento>> findAll() {
        try {
            return ResponseEntity.ok(inscricaoEventoService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<RestResponseMessage> update(@PathVariable Long id, @RequestBody InscricaoEvento inscricaoEvento) {
        try {
            inscricaoEvento.setId(id);
            inscricaoEventoService.update(inscricaoEvento);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Inscrição atualizada com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (InscricaoEventoNotFoundException e) {
            RestResponseMessage message = new RestResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (ApoiadorNotFoundException e) {
            RestResponseMessage message = new RestResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (EventoNotFoundException e) {
            RestResponseMessage message = new RestResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            RestResponseMessage message = new RestResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar inscrição: " + e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscricaoEvento> findById(@PathVariable Long id) {
        try {
            InscricaoEvento inscricaoEvento = inscricaoEventoService.findById(id);
            return ResponseEntity.ok(inscricaoEvento);
        } catch (InscricaoEventoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<RestResponseMessage> delete(@PathVariable Long id) {
        try {
            inscricaoEventoService.delete(id);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Inscrição excluída com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (InscricaoEventoNotFoundException e) {
            RestResponseMessage message = new RestResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            RestResponseMessage message = new RestResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir inscrição: " + e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}