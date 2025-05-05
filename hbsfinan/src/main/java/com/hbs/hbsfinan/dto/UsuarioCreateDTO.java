package com.hbs.hbsfinan.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioCreateDTO {
    @NotBlank(message = "O usuário deve possuir nome!")
    private String nome;

    @NotBlank(message = "O usuário deve possuir ultimo nome!")
    private String ultimoNome;

    @NotBlank(message = "O Usuário deve possuir email!")
    @Email(message = "Insira um Email válido!")
    private String email;

    @NotBlank(message = "O Usuário deve possuir uma senha!")
    private String senha;

    @NotBlank(message = "O Usuário deve possuir uma role!")
    private String role;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
