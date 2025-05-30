package com.hbs.hbsfinan.dto;

public class ProdutoEstoqueDTO {
    private int id;
    private String nome;
    private long qtd; // Quantidade em estoque

    public ProdutoEstoqueDTO() {
    }

    public ProdutoEstoqueDTO(int id, String nome, long qtd) {
        this.id = id;
        this.nome = nome;
        this.qtd = qtd;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getQtd() {
        return qtd;
    }

    public void setQtd(long qtd) {
        this.qtd = qtd;
    }
}