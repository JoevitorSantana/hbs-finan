package com.hbs.hbsfinan.model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String local;

    private Date data;//colocar formatação de data

    private String descricao;

    private String nome;

    private String materiais;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "func_id")//ver se esta certo
    private Funcionario funcionario;

    public Evento() {}

    public Evento(int id, String local, Date data, String descricao, String nome, String materiais, Funcionario funcionario) {
        this.id = id;
        this.local = local;
        this.data = data;
        this.descricao = descricao;
        this.nome = nome;
        this.materiais = materiais;
        this.funcionario = funcionario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMateriais() {
        return materiais;
    }

    public void setMateriais(String materiais) {
        this.materiais = materiais;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
