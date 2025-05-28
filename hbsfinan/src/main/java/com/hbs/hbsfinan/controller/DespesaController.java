package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.dto.QuitacaoDTO;
import com.hbs.hbsfinan.exceptions.DespesaNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.repository.implementation.DespesaRepository;
import com.hbs.hbsfinan.service.DespesaService;
import com.hbs.hbsfinan.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;

    // Injete via construtor (Spring faz a injeção automaticamente)
    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @PostMapping("/caixa/{idCaixa}")
    public ResponseEntity<Despesa> save(@PathVariable int idCaixa, @RequestBody @Valid DespesaCreateDTO dto) {
        try {
            Despesa criaDespesa = despesaService.criarDespesa(dto, (int) idCaixa);
            return ResponseEntity.ok(criaDespesa);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/quitar/{id}")
    public ResponseEntity<Despesa> quitarDespesa(@PathVariable int id, @RequestBody @Valid QuitacaoDTO dto) {
        try {
            Despesa despesa = despesaService.findById(id);
            despesa.setPagamentoTotal(dto.getPagamentoTotal());
            despesa.setDataQuitacao(LocalDate.now());
            despesaService.update(despesa);
            return ResponseEntity.ok(despesa);
        } catch (DespesaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Despesa>> listarTodas() {
        return ResponseEntity.ok(despesaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despesa> buscarPorId(@PathVariable int id) {
        try {
            Despesa despesa = despesaService.findById(id);
            return ResponseEntity.ok(despesa);
        } catch (DespesaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Despesa> atualizarDespesa(@PathVariable int id, @RequestBody @Valid DespesaCreateDTO dto) {
        try {
            Despesa despesa = despesaService.findById(id);
            despesa.setDataLancamento(dto.getDataLancamento());
            despesa.setDataVencimento(dto.getDataVencimento());
            despesa.setDesc(dto.getDesc());
            despesa.setValor(dto.getValor());
            despesaService.update(despesa);
            return ResponseEntity.ok(despesa);
        } catch (DespesaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDespesa(@PathVariable int id) {
        try {
            despesaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (DespesaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
