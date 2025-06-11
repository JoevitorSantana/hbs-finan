package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.MovimentacaoEstoqueRequestDTO;
import com.hbs.hbsfinan.enums.TipoMovimentacao;
import com.hbs.hbsfinan.exceptions.ProdutoNotFoundException;
import com.hbs.hbsfinan.exceptions.FuncionarioNotFoundException;
import com.hbs.hbsfinan.exceptions.EstoqueInsuficienteException;
import com.hbs.hbsfinan.exceptions.MovimentacaoEstoqueNotFoundException;

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
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

@Service
public class EstoqueService {
    private final IProdutosRepository produtoRepository;
    private final IFuncionarioRepository funcionarioRepository;
    private final IMovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    private final Conexao dbConnFactory;

    public EstoqueService(Conexao dbConn) {
        if (dbConn == null) {
            throw new IllegalArgumentException("Instância de Conexao não pode ser nula para EstoqueService.");
        }
        this.dbConnFactory = dbConn;
        this.produtoRepository = new ProdutosRepository(dbConnFactory);
        this.funcionarioRepository = new FuncionarioRepository(dbConnFactory);
        this.movimentacaoEstoqueRepository = new MovimentacaoEstoqueRepository(dbConnFactory);
    }

    private MovimentacaoEstoque _registrarMovimentacaoUnica(MovimentacaoEstoqueRequestDTO dto, Funcionario funcionario) {
        if (dto.getProdutoId() <= 0 || dto.getTipo() == null || dto.getQuantidadeMovimentada() == 0) {
            throw new IllegalArgumentException("Dados inválidos para movimentação de estoque: produtoId, tipo e quantidadeMovimentada são obrigatórios e quantidade deve ser diferente de zero.");
        }

        Produtos produto = produtoRepository.findById(dto.getProdutoId());
        if (produto == null) {
            throw new ProdutoNotFoundException("Produto não encontrado com ID: " + dto.getProdutoId());
        }
        long quantidadeAtual = produto.getQtd();
        long quantidadeMovimentar = dto.getQuantidadeMovimentada();
        long novaQuantidade;
        if (dto.getTipo().name().startsWith("ENTRADA_")) {
            novaQuantidade = quantidadeAtual + quantidadeMovimentar;
        } else if (dto.getTipo().name().startsWith("SAIDA_") || dto.getTipo().equals(TipoMovimentacao.AJUSTE_SAIDA)) {
            long quantidadeEfetivaSaida = Math.abs(quantidadeMovimentar);
            novaQuantidade = quantidadeAtual - quantidadeEfetivaSaida;
            if (novaQuantidade < 0) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para o produto: " + produto.getNome() +
                                ". Estoque atual: " + quantidadeAtual +
                                ", Tentativa de saída: " + quantidadeEfetivaSaida
                );
            }
        } else if (dto.getTipo().equals(TipoMovimentacao.AJUSTE_ENTRADA)){
            novaQuantidade = quantidadeAtual + quantidadeMovimentar;
        }
        else {
            throw new IllegalArgumentException("Tipo de movimentação inválido: " + dto.getTipo() + ". O tipo deve começar com ENTRADA_, SAIDA_ ou ser AJUSTE_ENTRADA/AJUSTE_SAIDA.");
        }
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setFuncionario(funcionario);
        movimentacao.setQuantidadeMovimentada(novaQuantidade - quantidadeAtual);
        movimentacao.setTipo(dto.getTipo());
        movimentacao.setDataHoraMovimentacao(LocalDateTime.now());
        movimentacao.setObservacao(dto.getObservacao());
        movimentacaoEstoqueRepository.save(movimentacao);
        produto.setQtd(novaQuantidade);
        produtoRepository.update(produto);
        System.out.println("Movimentação de estoque registrada para produto ID " + produto.getId() +
                ". Nova quantidade: " + novaQuantidade + ". Tipo: " + dto.getTipo());
        return movimentacao;
    }

    @Transactional
    public List<MovimentacaoEstoque> registrarMovimentacoes(List<MovimentacaoEstoqueRequestDTO> movimentacoes) {
        if (movimentacoes == null || movimentacoes.isEmpty()) {
            throw new IllegalArgumentException("A lista de movimentações não pode ser vazia.");
        }

        for (MovimentacaoEstoqueRequestDTO dto : movimentacoes) {
            if (dto == null) {
                throw new IllegalArgumentException("Um item da lista de movimentações é nulo.");
            }
            if (dto.getFuncionarioId() <= 0) {
                throw new IllegalArgumentException("ID do funcionário é obrigatório para cada movimentação na lista.");
            }
        }

        List<MovimentacaoEstoque> movimentacoesSalvas = new java.util.ArrayList<>();
        java.util.Map<Integer, Funcionario> funcionariosCache = new java.util.HashMap<>();
        for (MovimentacaoEstoqueRequestDTO dto : movimentacoes) {
            Funcionario funcionario = funcionariosCache.get(dto.getFuncionarioId());
            if (funcionario == null) {
                funcionario = funcionarioRepository.findById(dto.getFuncionarioId());
                if (funcionario == null) {
                    throw new FuncionarioNotFoundException("Funcionário não encontrado com ID: " + dto.getFuncionarioId() + " para a movimentação do produto ID: " + dto.getProdutoId());
                }
                funcionariosCache.put(dto.getFuncionarioId(), funcionario); // Adiciona ao cache
            }

            MovimentacaoEstoque movSalva = _registrarMovimentacaoUnica(dto, funcionario);
            movimentacoesSalvas.add(movSalva);
        }
        return movimentacoesSalvas;
    }

    public List<MovimentacaoEstoque> listarTodasMovimentacoes() {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findAll();
        if (movimentacoes == null) {
            return Collections.emptyList();
        }
        return movimentacoes;
    }

    @Transactional
    public void excluirMovimentacao(Long id) {
        MovimentacaoEstoque movimentacao = movimentacaoEstoqueRepository.findById(id);
        if (movimentacao == null) {
            throw new MovimentacaoEstoqueNotFoundException("Movimentação de estoque com ID " + id + " não encontrada.");
        }

        Produtos produto = movimentacao.getProduto();
        long quantidadeMovimentada = movimentacao.getQuantidadeMovimentada();
        long novaQuantidadeProduto = produto.getQtd() - quantidadeMovimentada;
        if (novaQuantidadeProduto < 0) {
            throw new EstoqueInsuficienteException(
                    "Não é possível reverter a movimentação ID " + id + " pois causaria estoque negativo para o produto: " + produto.getNome() +
                            ". Estoque atual: " + produto.getQtd() + ", Quantidade a reverter: " + quantidadeMovimentada + ", Estoque após reversão: " + novaQuantidadeProduto
            );
        }

        produto.setQtd(novaQuantidadeProduto);
        produtoRepository.update(produto);
        if (!movimentacaoEstoqueRepository.delete(id)) {
            throw new RuntimeException("Erro ao excluir movimentação de estoque com ID: " + id);
        }
        System.out.println("Movimentação de estoque ID " + id + " excluída e estoque do produto ID " + produto.getId() + " revertido.");
    }

    public MovimentacaoEstoque findMovimentacaoById(Long id) {
        MovimentacaoEstoque movimentacao = movimentacaoEstoqueRepository.findById(id);
        if (movimentacao == null) {
            throw new MovimentacaoEstoqueNotFoundException("Movimentação de estoque com ID " + id + " não encontrada.");
        }
        return movimentacao;
    }

    public Produtos findProdutoById(int id) {
        Produtos produto = produtoRepository.findById(id);
        if (produto == null) {
            throw new ProdutoNotFoundException("Produto não encontrado com ID: " + id);
        }
        return produto;
    }

    public Funcionario findFuncionarioById(int id) {
        Funcionario funcionario = funcionarioRepository.findById(id);
        if (funcionario == null) {
            throw new FuncionarioNotFoundException("Funcionário não encontrado com ID: " + id);
        }
        return funcionario;
    }
}