package com.hbs.hbsfinan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

//    @OneToOne
//    @JoinColumn(name = "id_caixa")//ver se esta certo
//    @JsonIgnore
//    private Caixa caixa;

    public DoacaoMonetaria() {
    }

    public DoacaoMonetaria(int id, long valor, Date data, Apoiador apoiador) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.apoiador = apoiador;
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

    public Apoiador getApoiador() {
        return apoiador;
    }

    public void setApoiador(Apoiador apoiador) {
        this.apoiador = apoiador;
    }
}
