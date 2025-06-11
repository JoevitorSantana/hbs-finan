package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.enums.TipoMovimentacao;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.repository.implementation.ProdutosRepository;
import com.hbs.hbsfinan.infra.db.Conexao;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EstoqueService {
    private final ProdutosRepository produtosRepository; // Referência à implementação concreta

    // O service recebe a instância da Conexao (ou uma factory para ela)
    // E então cria a instância do repositório
    public EstoqueService(Conexao dbConnFactory) {
        // Validação básica para garantir que a Conexao não é nula
        if (dbConnFactory == null) {
            throw new IllegalArgumentException("A fábrica de conexão com o banco de dados não pode ser nula.");
        }
        this.produtosRepository = new ProdutosRepository(dbConnFactory);
    }

    // O método de atualização de estoque permanece o mesmo
    public Produtos atualizarQuantidadeEstoque(int produtoId, TipoMovimentacao tipoMovimentacao, long quantidade) {
        // 1. Validações de Entrada
        if (produtoId <= 0) {
            throw new IllegalArgumentException("ID do produto inválido. Deve ser um número positivo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade a ser movimentada deve ser maior que zero.");
        }
        Objects.requireNonNull(tipoMovimentacao, "Tipo de movimentação não pode ser nulo.");

        // 2. Busca o Produto Existente
        Produtos produto = produtosRepository.findById(produtoId);

        if (produto == null) {
            throw new RuntimeException("Produto com ID " + produtoId + " não encontrado no sistema.");
        }

        // 3. Aplica a Lógica de Negócio (Incrementar/Decrementar)
        long quantidadeAtual = produto.getQtd();

        if (tipoMovimentacao == TipoMovimentacao.INCREMENTAR) {
            produto.setQtd(quantidadeAtual + quantidade);
            System.out.println("Produto ID " + produtoId + ": Quantidade incrementada de " + quantidadeAtual + " para " + produto.getQtd());
        } else if (tipoMovimentacao == TipoMovimentacao.DECREMENTAR) {
            if (quantidadeAtual < quantidade) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente para decrementar. Produto ID " + produtoId + " tem " + quantidadeAtual +
                                " unidades, mas " + quantidade + " foram solicitadas."
                );
            }
            produto.setQtd(quantidadeAtual - quantidade);
            System.out.println("Produto ID " + produtoId + ": Quantidade decrementada de " + quantidadeAtual + " para " + produto.getQtd());
        } else {
            throw new IllegalArgumentException("Tipo de movimentação desconhecido: " + tipoMovimentacao);
        }

        // 4. Persiste as Alterações no Banco de Dados
        try {
            produtosRepository.update(produto);
            System.out.println("Estoque do produto ID " + produtoId + " atualizado com sucesso para: " + produto.getQtd());
            return produto;
        } catch (Exception e) {
            System.err.println("Erro ao salvar a atualização de estoque do produto ID " + produtoId + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha na persistência ao atualizar o estoque do produto.", e);
        }
    }
}