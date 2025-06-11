package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.model.DoacaoProduto;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoAlimenticia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doacoes")
public class DoacaoProdutoController {

    private final IDoacaoAlimenticia doacaoService;

    @Autowired
    public DoacaoProdutoController(IDoacaoAlimenticia doacaoService) {
        this.doacaoService = doacaoService;
    }

    // Criar nova doação
    @PostMapping
    public ResponseEntity<String> createDoacao(@RequestBody DoacaoProduto doacaoProduto) {
        try {
            doacaoService.save(doacaoProduto);
            return ResponseEntity.ok("Doação salva com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao salvar doação: " + e.getMessage());
        }
    }

    // Buscar doação por ID
    @GetMapping("/{id}")
    public ResponseEntity<DoacaoProduto> getDoacaoById(@PathVariable int id) {
        DoacaoProduto doacao = doacaoService.findById(id);
        if (doacao != null) {
            return ResponseEntity.ok(doacao);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar todas as doações
    @GetMapping
    public ResponseEntity<List<DoacaoProduto>> getAllDoacoes() {
        List<DoacaoProduto> doacoes = doacaoService.findAll();
        return ResponseEntity.ok(doacoes);
    }

    // Deletar doação por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoacao(@PathVariable int id) {
        try {
            doacaoService.delete(id);
            return ResponseEntity.ok("Doação deletada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao deletar doação: " + e.getMessage());
        }
    }
}
