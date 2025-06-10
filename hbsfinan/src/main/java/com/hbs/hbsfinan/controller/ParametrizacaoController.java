package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.exceptions.ParametrizacaoJaCadastradaException;
import com.hbs.hbsfinan.exceptions.ParametrizacaoNaoEncontradaException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Parametrizacao;
import com.hbs.hbsfinan.repository.implementation.ParametrizacaoRepository;
import com.hbs.hbsfinan.service.ParametrizacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/parametrizacao")
public class ParametrizacaoController {
    private Conexao dbConnFactory;
    private ParametrizacaoService service;


    public ParametrizacaoController() {
        System.out.println("DEBUG: Construtor ParametrizacaoController chamado."); // <-- MENSAGEM 1

        this.dbConnFactory = Conexao.getInstance();
        System.out.println("DEBUG: Conexao instanciada: " + (dbConnFactory != null ? dbConnFactory.getClass().getName() : "null")); // <-- MENSAGEM 2
        System.out.println("DEBUG: Estado da Conexao: " + (dbConnFactory != null ? dbConnFactory.getEstadoConexao() : "N/A - instância nula")); // <-- MENSAGEM 3
        System.out.println("DEBUG: Mensagem de erro da Conexao (se houver): " + (dbConnFactory != null ? dbConnFactory.getMensagemErro() : "N/A - instância nula")); // <-- MENSAGEM 4


        // *** BLOCk DE VERIFICAÇÃO E TRATAMENTO DE ERRO NA CONEXÃO DENTRO DO CONSTRUTOR DO CONTROLLER ***
        if (dbConnFactory == null || !dbConnFactory.getEstadoConexao()) {
            String errorMessage = (dbConnFactory != null) ? dbConnFactory.getMensagemErro() : "Conexao.getInstance() retornou null.";
            // Lançar uma RuntimeException aqui impedirá que o Controller seja inicializado pelo Spring
            // se a conexão falhar neste ponto.
            throw new RuntimeException("Falha na inicialização da conexão no ParametrizacaoController: " + errorMessage);
        }
        // ************************************************************************************************


        ParametrizacaoRepository parametrizacaoRepository = new ParametrizacaoRepository(dbConnFactory);
        System.out.println("DEBUG: ParametrizacaoRepository instanciado."); // <-- MENSAGEM 5

        this.service = new ParametrizacaoService(dbConnFactory); // Passa a Conexao para a Service
        System.out.println("DEBUG: ParametrizacaoService instanciado."); // <-- MENSAGEM 6

        System.out.println("DEBUG: Construtor ParametrizacaoController finalizado com sucesso."); // <-- MENSAGEM 7
    }

    /**Retorna a parametrização existente.*/
    @GetMapping
    public ResponseEntity<Parametrizacao> getParametrizacao() {
        try {
            Parametrizacao p = service.getParametrizacao();
            return ResponseEntity.ok(p);
        } catch (ParametrizacaoNaoEncontradaException e) {
            System.err.println("GET /parametrizacao - Parametrização não encontrada: " + e.getMessage()); // Depuração
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            System.err.println("GET /parametrizacao - Erro interno do servidor: " + e.getMessage()); // Depuração
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**Cria a parametrização. Só permite se não existir.*/
    @PostMapping("/novo")
    public ResponseEntity<RestResponseMessage> createParametrizacao(
            @Valid @RequestBody Parametrizacao parametrizacao) {
        try {
            service.createParametrizacao(parametrizacao);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Parametrização inserida com sucesso!");
            System.out.println("POST /parametrizacao/novo - Parametrização criada com sucesso."); // Depuração
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (ParametrizacaoJaCadastradaException ex) {
            System.err.println("POST /parametrizacao/novo - Erro de conflito: " + ex.getMessage()); // Depuração
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CONFLICT, ex.getMessage());
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } catch (Exception e) {
            System.err.println("POST /parametrizacao/novo - Erro interno do servidor: " + e.getMessage()); // Depuração
            e.printStackTrace();
            RestResponseMessage message = new RestResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar parametrização: " + e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**Atualiza a parametrização existente.*/
    @PutMapping("/editar")
    public ResponseEntity<RestResponseMessage> updateParametrizacao(@Valid @RequestBody Parametrizacao parametrizacao) {
        try {
            service.updateParametrizacao(parametrizacao);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Parametrização atualizada com sucesso!");
            System.out.println("PUT /parametrizacao/editar - Parametrização atualizada com sucesso."); // Depuração
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ParametrizacaoNaoEncontradaException ex) {
            System.err.println("PUT /parametrizacao/editar - Erro de não encontrado: " + ex.getMessage()); // Depuração
            RestResponseMessage message = new RestResponseMessage(HttpStatus.NOT_FOUND, ex.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.err.println("PUT /parametrizacao/editar - Erro interno do servidor: " + e.getMessage()); // Depuração
            e.printStackTrace();
            RestResponseMessage message = new RestResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar parametrização: " + e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}