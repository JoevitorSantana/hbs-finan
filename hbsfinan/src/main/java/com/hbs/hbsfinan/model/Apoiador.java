package com.hbs.hbsfinan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.util.Date;

@Entity
public class Apoiador {

    @Id
    private int Id;
    private String nome;
    private String email;
    private String fone;
    private String endereco;
    private Date dataNasc;
    private String sexo;
    private String cpf;

    public Apoiador(){}

    public Apoiador(String nome, String email, String fone, String endereco, String sexo, String cpf, Date dataNasc, int Id)
    {
        this.cpf=cpf;
        this.dataNasc=dataNasc;
        this.Id=Id;
        this.email=email;
        this.nome=nome;
        this.sexo=sexo;
        this.fone=fone;
        this.endereco=endereco;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }


    public String getNome() {
        return nome;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public int getId() {
        return Id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getFone() {
        return fone;
    }

    public String getSexo() {
        return sexo;
    }

}
