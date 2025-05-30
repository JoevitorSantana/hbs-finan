package com.hbs.hbsfinan.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
@Entity
public class Anotacoes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String anotacao;

    private Date data;//colocar formatação de data

    @ManyToOne
    @JoinColumn(name = "even_id")//ver se esta certo
    @JsonIgnore
    private Evento evento;

    public Anotacoes(){}

    public Anotacoes(int id, String anotacao, Date data, Evento evento) {
        this.id = id;
        this.anotacao = anotacao;
        this.data = data;
        this.evento = evento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(String anotacao) {
        this.anotacao = anotacao;
    }

    @JsonIgnore
    public Evento getEvento() {
        return evento;
    }

    @JsonProperty
    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @JsonProperty("even_id")
    public int getEvenId() {
        if (evento != null)
        {
            return evento.getId();
        }
        else
        {
            return 0;
        }
    }
}
