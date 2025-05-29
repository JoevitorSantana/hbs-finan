package com.hbs.hbsfinan.dto;

import com.hbs.hbsfinan.enums.TipoMovimentacao; // Certifique-se de que o caminho para o Enum está correto

public class MovimentacaoEstoqueRequestDTO {

    private int produtoId;
    private int funcionarioId;
    private long quantidadeMovimentada; // Positivo para entradas, negativo para saídas
    private TipoMovimentacao tipo;
    private String observacao; // Opcional

    // Construtor padrão
    public MovimentacaoEstoqueRequestDTO() {
    }

    // Construtor com todos os campos
    public MovimentacaoEstoqueRequestDTO(int produtoId, int funcionarioId, long quantidadeMovimentada, TipoMovimentacao tipo, String observacao) {
        this.produtoId = produtoId;
        this.funcionarioId = funcionarioId;
        this.quantidadeMovimentada = quantidadeMovimentada;
        this.tipo = tipo;
        this.observacao = observacao;
    }

    // Getters e Setters
    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public long getQuantidadeMovimentada() {
        return quantidadeMovimentada;
    }

    public void setQuantidadeMovimentada(long quantidadeMovimentada) {
        this.quantidadeMovimentada = quantidadeMovimentada;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}