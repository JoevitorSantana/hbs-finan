package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.MovimentacaoEstoqueRequestDTO;
import com.hbs.hbsfinan.dto.ProdutoEstoqueDTO;
import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.service.EstoqueService;
import jakarta.validation.Valid;
// Não precisa mais de @Autowired aqui se o controller não tiver outras dependências Spring
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// Não precisa de JdbcTemplate aqui se o EstoqueService não o recebe mais
// import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private Conexao dbConnFactory; // TEM QUE TER ISSO
    private EstoqueService estoqueService;

    // Construtor não precisa mais de @Autowired ou JdbcTemplate
    // se ele só está instanciando o service com a Conexao.
    public EstoqueController() {
        this.dbConnFactory = Conexao.getInstance(); // Ou SingletonDB.getConexao(), o que for o seu padrão correto
        this.estoqueService = new EstoqueService(this.dbConnFactory);
    }

    // ... (métodos @PostMapping("/movimentar") e @GetMapping("/saldos") permanecem como antes) ...
    // Vou omiti-los para brevidade, mas eles usam this.estoqueService.
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