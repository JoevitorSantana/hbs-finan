package com.hbs.hbsfinan.dto;
import com.hbs.hbsfinan.enums.TipoMovimentacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class EstoqueAtualizacaoDTO {
    @NotNull(message = "O ID do produto não pode ser nulo.")
    @Min(value = 1, message = "O ID do produto deve ser um número positivo.")
    private int produtoId;

    @NotNull(message = "O tipo de movimentação não pode ser nulo.")
    private TipoMovimentacao tipo;

    @Min(value = 1, message = "A quantidade movimentada deve ser maior que zero.")
    private long quantidade;

    public EstoqueAtualizacaoDTO() {}
    public EstoqueAtualizacaoDTO(int produtoId, TipoMovimentacao tipo, long quantidade) {
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
    }

    // Getters e Setters para todos os campos
    public int getProdutoId() {
        return produtoId;
    }
    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }
    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public long getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }
}