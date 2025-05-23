package com.hbs.hbsfinan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProdutosDTO {

    @NotBlank
    private String nome;
    @Positive
    private long qtd;





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
