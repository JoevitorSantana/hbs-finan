package com.hbs.hbsfinan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Apoiador {

    @Id
    private Long Id;



    @ManyToOne
    @JoinColumn(name = "id_grupo")//ver se esta certo
    private Grupo grupo;


    private String nome;
    private String email;
    private String fone;
    private String endereco;
    private LocalDate dataNasc;
    private String sexo;
    private String cpf;

    public Apoiador(){}

    public Apoiador(String nome, String email, String fone, String endereco, String sexo, String cpf, LocalDate dataNasc, Long Id, Grupo grupo)
    {
        this.cpf=cpf;
        this.grupo=grupo;
        this.dataNasc=dataNasc;
        this.Id=Id;
        this.email=email;
        this.nome=nome;
        this.sexo=sexo;
        this.fone=fone;
        this.endereco=endereco;
    }

    public void setDataNasc(LocalDate dataNasc) {
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

    public void setId(Long id) {
        Id = id;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }


    public String getNome() {
        return nome;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
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

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    @JsonProperty("id_grupo")
    public int getGrupoId() {
        if (grupo != null)
        {
            return grupo.getId();
        }
        else
        {
            return 0;
        }
    }



}
