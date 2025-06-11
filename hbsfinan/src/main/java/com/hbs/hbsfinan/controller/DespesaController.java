package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.dto.QuitacaoDTO;
import com.hbs.hbsfinan.exceptions.DespesaNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.repository.implementation.CaixaRepository;
import com.hbs.hbsfinan.repository.implementation.DespesaRepository;
import com.hbs.hbsfinan.service.CaixaService;
import com.hbs.hbsfinan.service.DespesaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;
    //private CaixaRepository caixaRepository;
    private CaixaService caixaService;

    public DespesaController() {
        Conexao conexao = Conexao.getInstance();
        DespesaRepository despesaRepository = new DespesaRepository(conexao);
        CaixaRepository caixaRepository = new CaixaRepository(conexao);
        this.caixaService = new CaixaService(conexao);  // Inicializa aqui
        this.despesaService = new DespesaService(despesaRepository, caixaRepository);
    }

    @PostMapping("/caixa/{idCaixa}")
    public ResponseEntity<?> save(@PathVariable int idCaixa, @Valid @RequestBody DespesaCreateDTO dto) {
        try {
            Despesa despesaCriada = despesaService.criarDespesa(dto, idCaixa);
            return ResponseEntity.status(HttpStatus.CREATED).body(despesaCriada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Erro ao criar despesa: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erro inesperado ao criar despesa"));
        }
    }

    @PutMapping("/quitar/{id}")
    public ResponseEntity<?> quitarDespesa(@PathVariable int id, @Valid @RequestBody QuitacaoDTO dto) {
        try {
            Despesa despesaAtualizada = despesaService.quitarDespesa(id, dto);
            return ResponseEntity.ok(despesaAtualizada);
        } catch (DespesaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Erro ao quitar despesa: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erro inesperado ao quitar despesa"));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarTodas() {
        try {
            List<Despesa> despesas = despesaService.findAll();
            return ResponseEntity.ok(despesas);
        } catch (Exception e) {
            e.printStackTrace(); // Adicione isso temporariamente
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Erro ao listar despesas"));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        try {
            Despesa despesa = despesaService.findById(id);
            return ResponseEntity.ok(despesa);
        } catch (DespesaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erro ao buscar despesa"));
        }
    }


    @PutMapping("/editar/{id}")
    public ResponseEntity<?> atualizarDespesa(@PathVariable int id, @Valid @RequestBody DespesaCreateDTO dto) {
        try {
            despesaService.atualizarDespesaComImpactoNoCaixa(id, dto);
            return ResponseEntity.ok(Collections.singletonMap("message", "Despesa atualizada com sucesso!"));
        } catch (DespesaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }


    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirDespesa(@PathVariable int id) {
        try {
            despesaService.findById(id); // verifica se existe
            despesaService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Despesa deletada com sucesso!"));
        } catch (DespesaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erro ao deletar despesa"));
        }
    }


}
