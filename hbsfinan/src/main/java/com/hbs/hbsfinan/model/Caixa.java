package com.hbs.hbsfinan.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "caixa")
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "valor_inicial", nullable = false)
    private double valorInicial;

    @Column(name = "valor_final")
    private double valorFinal;

    @Column(name = "dt_abertura_caixa", nullable = false)
    private LocalDateTime dataAberturaCaixa;

    @Column(name = "dt_fechamento_caixa")
    private LocalDateTime dataFechamentoCaixa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fun_id", referencedColumnName = "id")
    private Funcionario funcionario;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(double valorInicial) {
        this.valorInicial = valorInicial;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public LocalDateTime getDataAberturaCaixa() {
        return dataAberturaCaixa;
    }

    public void setDataAberturaCaixa(LocalDateTime dataAberturaCaixa) {
        this.dataAberturaCaixa = dataAberturaCaixa;
    }

    public LocalDateTime getDataFechamentoCaixa() {
        return dataFechamentoCaixa;
    }

    public void setDataFechamentoCaixa(LocalDateTime dataFechamentoCaixa) {
        this.dataFechamentoCaixa = dataFechamentoCaixa;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}