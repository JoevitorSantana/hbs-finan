package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.MovimentacaoEstoqueRequestDTO;
import com.hbs.hbsfinan.dto.ProdutoEstoqueDTO;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.model.MovimentacaoEstoque;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.repository.interfaces.IFuncionarioRepository;
import com.hbs.hbsfinan.repository.interfaces.IMovimentacaoEstoqueRepository;
import com.hbs.hbsfinan.repository.interfaces.IProdutosRepository;

import org.springframework.beans.factory.annotation.Autowired; // Importe Autowired
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService {

    private final IMovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final IProdutosRepository produtoRepository;
    private final IFuncionarioRepository funcionarioRepository;

    @Autowired // O Spring vai injetar os beans dos repositórios aqui
    public EstoqueService(IProdutosRepository produtoRepository,
                          IFuncionarioRepository funcionarioRepository,
                          IMovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    // ... (métodos registrarMovimentacao e listarProdutosComEstoque permanecem os mesmos) ...
    public void registrarMovimentacao(MovimentacaoEstoqueRequestDTO dto) {
        // Validações iniciais
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

    public List<ProdutoEstoqueDTO> listarProdutosComEstoque() {
        List<Produtos> produtosModel = produtoRepository.findAll();
        if (produtosModel == null) {
            return List.of();
        }
        return produtosModel.stream()
                .map(p -> new ProdutoEstoqueDTO(p.getId(), p.getNome(), p.getQtd()))
                .collect(Collectors.toList());
    }
}