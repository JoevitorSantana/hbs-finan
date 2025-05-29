package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.MovimentacaoEstoqueRequestDTO;
import com.hbs.hbsfinan.dto.ProdutoEstoqueDTO;
import com.hbs.hbsfinan.dto.RestResponseMessage;
// Remova a importação de Conexao se não for mais usada aqui
// import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// Remova a importação de JdbcTemplate se não for mais usada aqui
// import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    // private Conexao dbConnFactory; // <-- NÃO PRECISA MAIS
    private final EstoqueService estoqueService; // Final, pois será injetado no construtor

    @Autowired // Injeta o bean EstoqueService
    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
        // REMOVA a instanciação manual do service e do dbConnFactory daqui
    }

    // ... (métodos @PostMapping("/movimentar") e @GetMapping("/saldos") permanecem como antes) ...
    // Vou omiti-los aqui para brevidade, mas eles usam this.estoqueService.
    @PostMapping("/movimentar")
    public ResponseEntity<RestResponseMessage> registrarNovaMovimentacao(@Valid @RequestBody MovimentacaoEstoqueRequestDTO dto) {
        try {
            estoqueService.registrarMovimentacao(dto);
            RestResponseMessage mensagemSucesso = new RestResponseMessage(
                    HttpStatus.CREATED,
                    "Movimentação de estoque registrada com sucesso!"
            );
            return new ResponseEntity<>(mensagemSucesso, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            RestResponseMessage mensagemErro = new RestResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(mensagemErro, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            e.printStackTrace();
            RestResponseMessage mensagemErro = new RestResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(mensagemErro, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            RestResponseMessage mensagemErro = new RestResponseMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocorreu um erro inesperado ao processar a movimentação de estoque."
            );
            return new ResponseEntity<>(mensagemErro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/saldos")
    public ResponseEntity<?> visualizarSaldosEstoque() {
        try {
            List<ProdutoEstoqueDTO> saldos = estoqueService.listarProdutosComEstoque();
            if (saldos == null || saldos.isEmpty()) {
                return new ResponseEntity<>(saldos, HttpStatus.OK);
            }
            return ResponseEntity.ok(saldos);
        } catch (Exception e) {
            e.printStackTrace();
            RestResponseMessage mensagemErro = new RestResponseMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocorreu um erro inesperado ao buscar os saldos do estoque."
            );
            return new ResponseEntity<>(mensagemErro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}