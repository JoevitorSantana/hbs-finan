package com.hbs.hbsfinan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class DoacaoMonetaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long valor;

    private Date data;

    @ManyToOne
    @JoinColumn(name = "id_ap")//ver se esta certo
    @JsonIgnore
    private Apoiador apoiador;

    @ManyToOne
    @JoinColumn(name = "id_caixa")//ver se esta certo
    @JsonIgnore
    private Caixa caixa;

    public DoacaoMonetaria() {
    }

    public DoacaoMonetaria(int id, long valor, Date data, Apoiador apoiador, Caixa caixa) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.apoiador = apoiador;
        this.caixa = caixa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @JsonIgnore
    public Apoiador getApoiador() {
        return apoiador;
    }

    @JsonProperty
    public void setApoiador(Apoiador apoiador) {
        this.apoiador = apoiador;
    }
    @JsonProperty("id_ap")
    public long getApoiadorId() {
        if (apoiador != null)
        {
            return apoiador.getId();
        }
        else
        {
            return 0;
        }
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


    public void setApoiadorId(int idApoiador) {
    }

    public void setCaixaId(int idCaixa) {
    }
}
