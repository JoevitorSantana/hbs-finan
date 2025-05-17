package com.hbs.hbsfinan.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Apoiador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String nome;
    private String email;
    private String fone;
    private String endereco;
    private Date data_nasc;
    private String sexo;
    private String cpf;

    public Apoiador(){}

    public Apoiador(String nome, String email, String fone, String endereco, String sexo, String cpf, Date data_nasc, Long Id)
    {
        this.cpf=cpf;
        this.data_nasc=data_nasc;
        this.Id=Id;
        this.email=email;
        this.nome=nome;
        this.sexo=sexo;
        this.fone=fone;
        this.endereco=endereco;
    }

    public void setDataNasc(Date data_nasc) {
        this.data_nasc = data_nasc;
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

    public void setId(Long id) {
        Id = id;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }


    public String getNome() {
        return nome;
    }

    public Date getDataNasc() {
        return data_nasc;
    }

    public Long getId() {
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
