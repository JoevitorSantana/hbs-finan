package com.hbs.hbsfinan.model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private long qtd;

    private LocalDate dataValidade; // <-- nova coluna (pode ser null)

    public Produtos() {
    }

    public Produtos(int id, String nome, long qtd, LocalDate dataValidade) {
        this.id = id;
        this.nome = nome;
        this.qtd = qtd;
        this.dataValidade = dataValidade;
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

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }
}


