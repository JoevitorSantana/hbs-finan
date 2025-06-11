package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.MovimentacaoEstoqueRequestDTO;
import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.exceptions.ProdutoNotFoundException;
import com.hbs.hbsfinan.exceptions.FuncionarioNotFoundException;
import com.hbs.hbsfinan.exceptions.EstoqueInsuficienteException;
import com.hbs.hbsfinan.exceptions.MovimentacaoEstoqueNotFoundException;
import com.hbs.hbsfinan.exceptions.DoacaoInvalidaException;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.MovimentacaoEstoque;
import com.hbs.hbsfinan.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private Conexao dbConnFactory;
    private EstoqueService estoqueService;

    public EstoqueController() {
        this.dbConnFactory = Conexao.getInstance();
        if (this.dbConnFactory == null || !this.dbConnFactory.getEstadoConexao()) {
            throw new RuntimeException("Falha na inicialização da Conexao no EstoqueController.");
        }
        this.estoqueService = new EstoqueService(this.dbConnFactory);
    }

    @PostMapping("/movimentar")
    public ResponseEntity<RestResponseMessage> registrarMovimentacoes(@Valid @RequestBody List<MovimentacaoEstoqueRequestDTO> movimentacoes) {
        try {
            List<MovimentacaoEstoque> movimentacoesSalvas = estoqueService.registrarMovimentacoes(movimentacoes);

            String msg = movimentacoesSalvas.size() + " movimentação(ões) de estoque registrada(s) com sucesso.";
            if (!movimentacoesSalvas.isEmpty()) {
                msg += " IDs dos produtos: " + movimentacoesSalvas.stream().map(m -> m.getProduto().getId()).toList();
            }

            RestResponseMessage mensagemSucesso = new RestResponseMessage(
                    HttpStatus.CREATED,
                    msg
            );
            return new ResponseEntity<>(mensagemSucesso, HttpStatus.CREATED);

        } catch (ProdutoNotFoundException | FuncionarioNotFoundException e) {
            RestResponseMessage mensagemErro = new RestResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(mensagemErro, HttpStatus.NOT_FOUND);
        } catch (EstoqueInsuficienteException | IllegalArgumentException | DoacaoInvalidaException e) {
            RestResponseMessage mensagemErro = new RestResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(mensagemErro, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            RestResponseMessage mensagemErro = new RestResponseMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocorreu um erro inesperado ao processar as movimentações de estoque: " + e.getMessage()
            );
            return new ResponseEntity<>(mensagemErro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<MovimentacaoEstoque>> visualizarMovimentacoesEstoque() {
        try {
            List<MovimentacaoEstoque> movimentacoes = estoqueService.listarTodasMovimentacoes();
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar as movimentações do estoque.", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoque> buscarMovimentacaoPorId(@PathVariable Long id) {
        try {
            MovimentacaoEstoque movimentacao = estoqueService.findMovimentacaoById(id);
            return ResponseEntity.ok(movimentacao);
        } catch (MovimentacaoEstoqueNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar a movimentação de estoque por ID.", e);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<RestResponseMessage> excluirMovimentacao(@PathVariable Long id) {
        try {
            estoqueService.excluirMovimentacao(id);
            RestResponseMessage mensagemSucesso = new RestResponseMessage(
                    HttpStatus.OK,
                    "Movimentação de estoque excluída com sucesso!"
            );
            return new ResponseEntity<>(mensagemSucesso, HttpStatus.OK);
        } catch (MovimentacaoEstoqueNotFoundException e) {
            RestResponseMessage mensagemErro = new RestResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(mensagemErro, HttpStatus.NOT_FOUND);
        } catch (EstoqueInsuficienteException e) {
            RestResponseMessage mensagemErro = new RestResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(mensagemErro, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            RestResponseMessage mensagemErro = new RestResponseMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocorreu um erro inesperado ao excluir a movimentação de estoque: " + e.getMessage()
            );
            return new ResponseEntity<>(mensagemErro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}