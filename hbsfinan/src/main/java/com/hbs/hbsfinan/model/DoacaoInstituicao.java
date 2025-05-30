package com.hbs.hbsfinan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class DoacaoInstituicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String cnpj;
    private Date data;
    private long valor;




    @ManyToOne
    @JoinColumn(name = "id_caixa")//ver se esta certo
    @JsonIgnore
    private Caixa caixa;


    public DoacaoInstituicao(int id, String nome, String cnpj, Date data, long valor, Caixa caixa) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.data = data;
        this.valor = valor;
        this.caixa = caixa;
    }

    public DoacaoInstituicao() {
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

    @JsonIgnore
    public Caixa getCaixa() {
        return caixa;
    }

    @JsonProperty
    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    @JsonProperty("id_caixa")
    public long getCaixaId() {
        if (caixa != null) {
            return caixa.getId();
        } else {
            return 0;
        }
    }

}
