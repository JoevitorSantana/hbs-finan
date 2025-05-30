package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.MovimentacaoEstoqueRequestDTO;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.model.MovimentacaoEstoque;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.repository.implementation.FuncionarioRepository;
import com.hbs.hbsfinan.repository.implementation.MovimentacaoEstoqueRepository;
import com.hbs.hbsfinan.repository.implementation.ProdutosRepository;

import com.hbs.hbsfinan.repository.interfaces.IFuncionarioRepository;
import com.hbs.hbsfinan.repository.interfaces.IMovimentacaoEstoqueRepository;
import com.hbs.hbsfinan.repository.interfaces.IProdutosRepository;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class EstoqueService {
    private final IProdutosRepository produtoRepository;
    private final IFuncionarioRepository funcionarioRepository;
    private final IMovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public EstoqueService(Conexao dbConn) {
        if (dbConn == null) {
            throw new IllegalArgumentException("Instância de Conexao não pode ser nula para EstoqueService.");
        }

        this.produtoRepository = new ProdutosRepository(dbConn);
        this.funcionarioRepository = new FuncionarioRepository(dbConn);
        this.movimentacaoEstoqueRepository = new MovimentacaoEstoqueRepository(dbConn);
    }


    public void registrarMovimentacao(MovimentacaoEstoqueRequestDTO dto) {
        if (dto == null || dto.getProdutoId() <= 0 || dto.getFuncionarioId() <= 0 || dto.getTipo() == null || dto.getQuantidadeMovimentada() == 0) {
            throw new IllegalArgumentException("Dados inválidos para movimentação de estoque.");
        }

        Produtos produto = produtoRepository.findById(dto.getProdutoId());
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado com ID: " + dto.getProdutoId());
        }

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId());
        if (funcionario == null) {
            throw new RuntimeException("Funcionário não encontrado com ID: " + dto.getFuncionarioId());
        }

        long quantidadeAtual = produto.getQtd();
        long quantidadeMovimentar = dto.getQuantidadeMovimentada();
        long novaQuantidade = quantidadeAtual + quantidadeMovimentar;

        boolean isSaida = quantidadeMovimentar < 0 ||
                dto.getTipo().name().startsWith("SAIDA_") ||
                dto.getTipo().name().equals("AJUSTE_SAIDA");

        if (isSaida && novaQuantidade < 0) {
            throw new RuntimeException(
                    "Estoque insuficiente para o produto: " + produto.getNome() +
                            ". Estoque atual: " + quantidadeAtual +
                            ", Tentativa de saída: " + Math.abs(quantidadeMovimentar)
            );
        }

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setFuncionario(funcionario);
        movimentacao.setQuantidadeMovimentada(quantidadeMovimentar);
        movimentacao.setTipo(dto.getTipo());
        movimentacao.setDataHoraMovimentacao(LocalDateTime.now());
        movimentacao.setObservacao(dto.getObservacao());

        movimentacaoEstoqueRepository.save(movimentacao);

        produto.setQtd(novaQuantidade);
        produtoRepository.update(produto);

        System.out.println("Movimentacao de estoque registrada para produto ID " + produto.getId() +
                ". Nova quantidade: " + novaQuantidade);
    }

//    public List<ProdutoEstoqueDTO> listarProdutosComEstoque() {
//        List<Produtos> produtosModel = produtoRepository.findAll();
//        if (produtosModel == null) {
//            return List.of();
//        }
//        return produtosModel.stream()
//                .map(p -> new ProdutoEstoqueDTO(p.getId(), p.getNome(), p.getQtd()))
//                .collect(Collectors.toList());
//    }
    public List<MovimentacaoEstoque> listarTodasMovimentacoes() {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findAll();
        if (movimentacoes == null) {
            return Collections.emptyList();
        }
        return movimentacoes;
    }
}