package com.hbs.hbsfinan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Parametrizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nomeEmpresa;
    private String razaoSocial;
    private String enderecoRua;
    private String enderecoBairro;
    private String enderecoCidade;
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve seguir o formato 00000-000") //esse comando garante que o valor de uma String siga uma expressão regular (regex) específica
    private String enderecoCep;
    private String enderecoEstado;
    @Email
    private String email;
    private String telefone;
    private String celular;
    private String nomeProprietario;
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ deve seguir o formato 00.000.000/0000-00")
    private String cnpj;
    private String logoPequenaUrl;
    private String logoGrandeUrl;

    public Parametrizacao() {}
    public Parametrizacao(String nomeEmpresa, String razaoSocial, String enderecoRua, String enderecoBairro, String enderecoCidade, String enderecoCep, String enderecoEstado, String email, String telefone, String celular, String nomeProprietario, String cnpj, String logoPequenaUrl, String logoGrandeUrl) {
        this.nomeEmpresa = nomeEmpresa;
        this.razaoSocial = razaoSocial;
        this.enderecoRua = enderecoRua;
        this.enderecoBairro = enderecoBairro;
        this.enderecoCidade = enderecoCidade;
        this.enderecoCep = enderecoCep;
        this.enderecoEstado = enderecoEstado;
        this.email = email;
        this.telefone = telefone;
        this.celular = celular;
        this.nomeProprietario = nomeProprietario;
        this.cnpj = cnpj;
        this.logoPequenaUrl = logoPequenaUrl;
        this.logoGrandeUrl = logoGrandeUrl;
    }
    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
    }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getEnderecoRua() { return enderecoRua; }
    public void setEnderecoRua(String enderecoRua) { this.enderecoRua = enderecoRua; }

    public String getEnderecoBairro() { return enderecoBairro; }
    public void setEnderecoBairro(String enderecoBairro) { this.enderecoBairro = enderecoBairro; }

    public String getEnderecoCidade() { return enderecoCidade; }
    public void setEnderecoCidade(String enderecoCidade) { this.enderecoCidade = enderecoCidade; }

    public String getEnderecoCep() { return enderecoCep; }
    public void setEnderecoCep(String enderecoCep) { this.enderecoCep = enderecoCep; }

    public String getEnderecoEstado() { return enderecoEstado; }
    public void setEnderecoEstado(String enderecoEstado) { this.enderecoEstado = enderecoEstado; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getNomeProprietario() { return nomeProprietario; }
    public void setNomeProprietario(String nomeProprietario) { this.nomeProprietario = nomeProprietario; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getLogoPequenaUrl() { return logoPequenaUrl; }
    public void setLogoPequenaUrl(String logoPequenaUrl) { this.logoPequenaUrl = logoPequenaUrl; }

    public String getLogoGrandeUrl() { return logoGrandeUrl; }
    public void setLogoGrandeUrl(String logoGrandeUrl) { this.logoGrandeUrl = logoGrandeUrl; }
}
