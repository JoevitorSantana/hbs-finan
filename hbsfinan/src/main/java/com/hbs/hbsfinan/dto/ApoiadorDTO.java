package com.hbs.hbsfinan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ApoiadorDTO {

    private Long id;

    @NotBlank(message = "O usuário deve possuir nome!")
    private String nome;

    @NotBlank(message = "O Usuário deve possuir email!")
    @Email(message = "Insira um Email válido!")
    private String email;

    @NotBlank(message = "O usuário deve possuir telefone!")
    private String fone;

    @NotBlank(message = "O usuário deve possuir endereco!")
    private String endereco;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataNasc;

    @NotBlank(message = "CPF inválido")
    private String cpf;

    private String sexo;

    // Em vez de manter o objeto Grupo, guarda só o ID
    private Long grupoId;

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @JsonProperty("id_grupo")
    public Long getGrupoId() {
        return grupoId;
    }

    @JsonProperty("id_grupo")
    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

}
