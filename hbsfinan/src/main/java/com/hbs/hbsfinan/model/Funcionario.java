package com.hbs.hbsfinan.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Funcionario {
    @Id
    private int id;
    private String nome;
    private String email;
    private String fone;
    private String endereco;
    private Date dataNascimento;
    private String sexo;
    private String cpf;

    //@ManyToOne
    //@JoinColumn(name = "usu_id", nullable = false);
    //private Usuario usuario;


//@JoinColumn(name = "cx_id", nullable = false);
    //private Caixa caixa;

    //@JoinColumn(name = "event_id", nullable = false);
    //private Evento evento;

    //@JoinColumn(name = "dm_id", nullable = false);
    //private Doacao_Material doacaoMaterial;

    //@JoinColumn(name = "da_id", nullable = false);
    //private Doacao_Alimenticia doacaoAlimenticia;

    //@JoinColumn(name = "di_id", nullable = false);
    //private Doacao_Instituicao doacaoInstituicao;


    public Funcionario() {}

    public Funcionario(int id, String nome, String email, Date dataNascimento, String fone, String endereco, String sexo, String cpf) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.fone = fone;
        this.endereco = endereco;
        this.sexo = sexo;
        this.cpf = cpf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

//    public Usuario getUsuario() {
//        return usuario;
//    }
//    public void setUsuario(Usuario usuario) {
//        this.usuario = usuario;
//    }
}
