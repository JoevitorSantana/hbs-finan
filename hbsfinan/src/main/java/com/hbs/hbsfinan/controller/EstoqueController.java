package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.service.EstoqueService;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.dto.EstoqueAtualizacaoDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private Conexao dbConnFactory;
    private EstoqueService estoqueService;

    public EstoqueController() {
        this.dbConnFactory = Conexao.getInstance();
        this.estoqueService = new EstoqueService(dbConnFactory);
    }

    /** Endpoint para atualizar a quantidade de estoque de um produto. */
    @PostMapping("/atualizar")
    public ResponseEntity<?> atualizarEstoque(
            @Valid @RequestBody EstoqueAtualizacaoDTO requestDTO // <<-- Mudança aqui!
    ) {
        Produtos produtoAtualizado;
        try {
            // A lógica if/else para o tipo agora é desnecessária aqui,
            // pois o TipoMovimentacao já é um enum no DTO e será validado pelo Spring.
            // O service já espera o enum.
            produtoAtualizado = estoqueService.atualizarQuantidadeEstoque(
                    requestDTO.getProdutoId(),
                    requestDTO.getTipo(),
                    requestDTO.getQuantidade()
            );

            return ResponseEntity.ok(produtoAtualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao processar a atualização de estoque: " + e.getMessage());
        }
    }
}