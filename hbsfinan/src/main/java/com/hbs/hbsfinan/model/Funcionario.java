package com.hbs.hbsfinan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Funcionario {
    @Id
    private int id;
    private String cpf;
    private String funcao;
    private Date dataNascimento;

    public Funcionario() {}

    public Funcionario(String cpf, String funcao, Date dataNascimento) {
        this.cpf = cpf;
        this.funcao = funcao;
        this.dataNascimento = dataNascimento;
    }

    // getters
    public int getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getFuncao() {
        return funcao;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
