package com.hbs.hbsfinan.dto;

import com.hbs.hbsfinan.enums.TipoMovimentacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MovimentacaoEstoqueRequestDTO {

    @NotNull(message = "O ID do produto não pode ser nulo.")
    @Min(value = 1, message = "O ID do produto deve ser maior que zero.")
    private Integer produtoId;

    @NotNull(message = "O ID do funcionário não pode ser nulo.")
    @Min(value = 1, message = "O ID do funcionário deve ser maior que zero.")
    private Integer funcionarioId;

    @NotNull(message = "A quantidade movimentada não pode ser nula.")
    private long quantidadeMovimentada;

    @NotNull(message = "O tipo de movimentação não pode ser nulo.")
    private TipoMovimentacao tipo;

    private String observacao; // Opcional

    public MovimentacaoEstoqueRequestDTO() {}

    public MovimentacaoEstoqueRequestDTO(Integer produtoId, Integer funcionarioId, long quantidadeMovimentada, TipoMovimentacao tipo, String observacao) {
        this.produtoId = produtoId;
        this.funcionarioId = funcionarioId;
        this.quantidadeMovimentada = quantidadeMovimentada;
        this.tipo = tipo;
        this.observacao = observacao;
    }

    public Integer getProdutoId() { return produtoId; }
    public void setProdutoId(Integer produtoId) { this.produtoId = produtoId; }
    public Integer getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Integer funcionarioId) { this.funcionarioId = funcionarioId; }
    public long getQuantidadeMovimentada() { return quantidadeMovimentada; }
    public void setQuantidadeMovimentada(long quantidadeMovimentada) { this.quantidadeMovimentada = quantidadeMovimentada; }
    public TipoMovimentacao getTipo() { return tipo; }
    public void setTipo(TipoMovimentacao tipo) { this.tipo = tipo; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}