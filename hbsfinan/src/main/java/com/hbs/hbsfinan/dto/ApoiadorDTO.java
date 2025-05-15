package com.hbs.hbsfinan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
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



    private String sexo;

    @NotBlank(message = "CPF incompleto!")
    private String cpf;


    public String getEndereco() {
        return endereco;
    }

    public String getSexo() {
        return sexo;
    }

    public String getFone() {
        return fone;
    }

    public String getEmail() {
        return email;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public String getNome() {
        return nome;
    }
    public String getCpf() {
        return cpf;
    }


    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
