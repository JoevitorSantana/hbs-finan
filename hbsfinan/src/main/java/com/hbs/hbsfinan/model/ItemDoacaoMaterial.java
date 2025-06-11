package com.hbs.hbsfinan.model;

import jakarta.persistence.*;

@Entity
public class ItemDoacaoMaterial {

    @EmbeddedId
    private ItemMaterialId id = new ItemMaterialId();

    @ManyToOne
    @MapsId("doacaoMaterial") // nome do campo na chave composta
    @JoinColumn(name = "doacao_material_id", nullable = false)
    private DoacaoMaterial doacaoMaterial;

    @ManyToOne
    @MapsId("produto") // nome do campo na chave composta
    @JoinColumn(name = "produto_id", nullable = false)
    private Produtos produto;

    private long quantidade;

    // Getters e setters
    public ItemMaterialId getId() {
        return id;
    }

    public void setId(ItemMaterialId id) {
        this.id = id;
    }

    public DoacaoMaterial getDoacaoMaterial() {
        return doacaoMaterial;
    }

    public void setDoacaoMaterial(DoacaoMaterial doacaoMaterial) {
        this.doacaoMaterial = doacaoMaterial;
        this.id.setDoacaoMaterial(doacaoMaterial.getId());
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
}
