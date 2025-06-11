package com.hbs.hbsfinan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DoacaoInstituicaoCreateDTO {

    private String nome;
    private String cnpj;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date data;

    private long valor;
    private int idCaixa;

    public DoacaoInstituicaoCreateDTO() {
    }

    public DoacaoInstituicaoCreateDTO(String nome, String cnpj, Date data, long valor, int idCaixa) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.data = data;
        this.valor = valor;
        this.idCaixa = idCaixa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }

    public int getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(int idCaixa) {
        this.idCaixa = idCaixa;
    }
}
