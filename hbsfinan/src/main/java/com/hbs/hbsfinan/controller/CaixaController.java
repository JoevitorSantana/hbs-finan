package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.CaixaCreateDTO;
import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.exceptions.CaixaNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.service.CaixaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/caixa")
public class CaixaController {

    private final CaixaService caixaService;

    public CaixaController() {
        Conexao dbConnFactory = Conexao.getInstance();
        this.caixaService = new CaixaService(dbConnFactory);
    }

    @PostMapping("/novo")
    public ResponseEntity<RestResponseMessage> save(@Valid @RequestBody CaixaCreateDTO dto) {
        caixaService.save(dto);
        RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Caixa cadastrado com sucesso!");
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Caixa>> findAll() {
        try {
            return ResponseEntity.ok(caixaService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caixa> findById(@PathVariable int id) {
        try {
            Caixa caixa = caixaService.findById(id);
            return ResponseEntity.ok(caixa);
        } catch (CaixaNotFoundException e) {
            throw new CaixaNotFoundException(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<RestResponseMessage> update(@PathVariable int id, @RequestBody Caixa dto) {
        try {
            Caixa oldCaixa = caixaService.findById(id);

            if (dto.getValorInicial() != oldCaixa.getValorInicial())
                oldCaixa.setValorInicial(dto.getValorInicial());

            if (dto.getValorFinal() != oldCaixa.getValorFinal())
                oldCaixa.setValorFinal(dto.getValorFinal());

            if (dto.getDataAberturaCaixa() != null && !dto.getDataAberturaCaixa().equals(oldCaixa.getDataAberturaCaixa()))
                oldCaixa.setDataAberturaCaixa(dto.getDataAberturaCaixa());

            if (dto.getDataFechamentoCaixa() != null && !dto.getDataFechamentoCaixa().equals(oldCaixa.getDataFechamentoCaixa()))
                oldCaixa.setDataFechamentoCaixa(dto.getDataFechamentoCaixa());

            if (dto.getFuncionario() != null && !dto.getFuncionario().equals(oldCaixa.getFuncionario()))
                oldCaixa.setFuncionario(dto.getFuncionario());

            caixaService.update(oldCaixa);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Caixa atualizado com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (CaixaNotFoundException e) {
            throw new CaixaNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<RestResponseMessage> delete(@PathVariable int id) {
        caixaService.findById(id); // valida existência
        caixaService.delete(id);
        RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Caixa excluído com sucesso!");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}