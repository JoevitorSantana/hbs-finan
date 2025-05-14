package com.hbs.hbsfinan.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Produtos {

    @Id
    @GeneratedValue
    private int id;

    private String nome;
    private long qtd;

    public Produtos() {

    }
    public Produtos(int id, String nome, long qtd) {
        this.id = id;
        this.nome = nome;
        this.qtd = qtd;
    }

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
