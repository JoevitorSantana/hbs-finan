package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.model.DoacaoMaterial;
import com.hbs.hbsfinan.model.DoacaoProduto;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoAlimenticia;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoMaterial;
import com.hbs.hbsfinan.service.DoacaoMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/doacoesmaterial")
public class DoacaoMaterialController {


        private final DoacaoMaterialService doacaoService;

        @Autowired
        public DoacaoMaterialController(DoacaoMaterialService doacaoService) {
            this.doacaoService = doacaoService;
        }

        // Criar nova doação
        @PostMapping
        public ResponseEntity<String> createDoacao(@RequestBody DoacaoMaterial doacaoProduto) {
            try {
                doacaoService.salvar(doacaoProduto);
                return ResponseEntity.ok("Doação salva com sucesso.");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Erro ao salvar doação: " + e.getMessage());
            }
        }

        // Buscar doação por ID
        @GetMapping("/{id}")
        public ResponseEntity<DoacaoMaterial> getDoacaoById(@PathVariable int id) {
            DoacaoMaterial doacao = doacaoService.buscarPorId(id);
            if (doacao != null) {
                return ResponseEntity.ok(doacao);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        // Listar todas as doações
        @GetMapping
        public ResponseEntity<List<DoacaoMaterial>> getAllDoacoes() {
            List<DoacaoMaterial> doacoes = doacaoService.listarTodos();
            return ResponseEntity.ok(doacoes);
        }

        // Deletar doação por ID
        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteDoacao(@PathVariable int id) {
            try {
                doacaoService.deletar(id);
                return ResponseEntity.ok("Doação deletada com sucesso.");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Erro ao deletar doação: " + e.getMessage());
            }
        }
}

