package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.exceptions.InscricaoEventoNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.infra.db.SingletonDB;
import com.hbs.hbsfinan.model.InscricaoEvento;
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
        this.dbConnFactory = SingletonDB.getConexao();
        this.inscricaoEventoService = new InscricaoEventoService(dbConnFactory);
    }

    @PostMapping("/nova")
    public ResponseEntity save(@Valid @RequestBody InscricaoEvento inscricaoEvento) {
        try {
            inscricaoEventoService.save(inscricaoEvento);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Inscrição realizada com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (Exception e){
            // Pode personalizar conforme suas exceções customizadas
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<InscricaoEvento>> findAll() {
        try {
            return ResponseEntity.ok(inscricaoEventoService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody InscricaoEvento inscricaoEvento) {
        try {
            InscricaoEvento oldInscricao = inscricaoEventoService.findById(id);

            // Atualiza apenas se vier valor novo (você pode expandir para mais campos)
            if (inscricaoEvento.getApoiador() != null)
                oldInscricao.setApoiador(inscricaoEvento.getApoiador());
            if (inscricaoEvento.getEvento() != null)
                oldInscricao.setEvento(inscricaoEvento.getEvento());

            inscricaoEventoService.update(oldInscricao);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Inscrição atualizada com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (InscricaoEventoNotFoundException e) {
            throw new InscricaoEventoNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscricaoEvento> findById(@PathVariable Long id) {
        try {
            InscricaoEvento inscricaoEvento = inscricaoEventoService.findById(id);
            return ResponseEntity.ok(inscricaoEvento);
        } catch (InscricaoEventoNotFoundException e) {
            throw new InscricaoEventoNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        inscricaoEventoService.findById(id); // já lança exceção se não existe
        inscricaoEventoService.delete(id);
        RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Inscrição excluída com sucesso!");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

