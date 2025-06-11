package com.hbs.hbsfinan.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemDoacaoId implements Serializable {

    private Long doacaoProduto;
    private int produto;

    public ItemDoacaoId() {}

    public ItemDoacaoId(Long doacaoProduto, int produto) {
        this.doacaoProduto = doacaoProduto;
        this.produto = produto;
    }

    public Long getDoacaoProduto() {
        return doacaoProduto;
    }

    public void setDoacaoProduto(Long doacaoProduto) {
        this.doacaoProduto = doacaoProduto;
    }

    public int getProduto() {
        return produto;
    }

    public void setProduto(int produto) {
        this.produto = produto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDoacaoId)) return false;
        ItemDoacaoId that = (ItemDoacaoId) o;
        return Objects.equals(doacaoProduto, that.doacaoProduto) &&
                Objects.equals(produto, that.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doacaoProduto, produto);
    }
}
