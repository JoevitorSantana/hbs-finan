package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.repository.implementation.DespesaRepository;
import com.hbs.hbsfinan.service.DespesaService;
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

    @Autowired
    private DespesaService despesaService;

    @PostMapping("/caixa/{idCaixa}")
    public ResponseEntity<Despesa> save(@PathVariable Long idCaixa, @RequestBody @Valid DespesaCreateDTO dto) {
        try {
            Despesa criaDespesa = despesaService.CriarDespesa(dto, idCaixa);
            return ResponseEntity.ok(criaDespesa);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DTO para quitação
    public static class QuitacaoDTO {
        private double pagamentoTotal;
        public double getPagamentoTotal() { return pagamentoTotal; }
        public void setPagamentoTotal(double pagamentoTotal) { this.pagamentoTotal = pagamentoTotal; }
    }

    @PutMapping("/quitar/{id}")
    public ResponseEntity<Despesa> quitarDespesa(@PathVariable int id, @RequestBody @Valid QuitacaoDTO dto) {
        Despesa despesa = despesaService.findById(id);
        if (despesa == null) {
            return ResponseEntity.notFound().build();
        }
        despesa.setPagamentoTotal(dto.getPagamentoTotal());
        despesa.setDataQuitacao(LocalDate.now());
        despesaService.update(despesa);
        return ResponseEntity.ok(despesa);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Despesa>> listarTodas() {
        return ResponseEntity.ok(despesaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despesa> buscarPorId(@PathVariable int id) {
        Despesa despesa = despesaService.findById(id);
        if (despesa == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(despesa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Despesa> atualizarDespesa(@PathVariable int id, @RequestBody @Valid DespesaCreateDTO dto) {
        Despesa despesa = despesaService.findById(id);
        if (despesa == null) {
            return ResponseEntity.notFound().build();
        }
        // Atualiza os campos necessários
        despesa.setDataLancamento(dto.getDataLancamento());
        despesa.setDataVencimento(dto.getDataVencimento());
        despesa.setDesc(dto.getDesc());
        despesa.setValor(dto.getValor());
        // pagamentoTotal e dataQuitacao só via quitação
        despesaService.update(despesa);
        return ResponseEntity.ok(despesa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDespesa(@PathVariable int id) {
        Despesa despesa = despesaService.findById(id);
        if (despesa == null) {
            return ResponseEntity.notFound().build();
        }
        despesaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
