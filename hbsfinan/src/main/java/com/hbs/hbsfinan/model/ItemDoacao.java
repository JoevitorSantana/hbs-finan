package com.hbs.hbsfinan.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ItemDoacao {

    @EmbeddedId
    private ItemDoacaoId id = new ItemDoacaoId();

    @ManyToOne
    @MapsId("doacaoProduto")
    @JoinColumn(name = "doacao_produto_id", nullable = false)
    private DoacaoProduto doacaoProduto;

    @ManyToOne
    @MapsId("produto")
    @JoinColumn(name = "produto_id", nullable = false)
    private Produtos produto;

    private long quantidade;

    private LocalDate dataValidade;

    public ItemDoacaoId getId() {
        return id;
    }

    public void setId(ItemDoacaoId id) {
        this.id = id;
    }

    public DoacaoProduto getDoacaoProduto() {
        return doacaoProduto;
    }

    public void setDoacaoProduto(DoacaoProduto doacaoProduto) {
        this.doacaoProduto = doacaoProduto;
        this.id.setDoacaoProduto(doacaoProduto.getId());
    }

    public Produtos getProduto() {
        return produto;
    }

    public void setProduto(Produtos produto) {
        this.produto = produto;
        this.id.setProduto(produto.getId());
    }

    public long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }
}
