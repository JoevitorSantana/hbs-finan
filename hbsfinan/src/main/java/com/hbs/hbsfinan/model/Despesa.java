package com.hbs.hbsfinan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;

import java.time.LocalDate;

// @Entity
public class Despesa {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate dataLancamento;
    private LocalDate dataVencimento;
    private String Desc;
    private double pagamentoTotal; //valor pagou com multa ou desconto
    private double valor;
    private LocalDate dataQuitacao;

    //one to one ->cada pessoa tem um cpf
    //many to one ->um cliente pode ter mts pedidos


//    @ManyToOne
//    @JoinColumn(name = "caixa_id", nullable = false)
//    private Caixa caixa;

    //FAZER GET E SET CAIXA

    public Despesa() {
    }


    public Despesa(int id, LocalDate dataLancamento, LocalDate dataVencimento, String desc, double pagamentoTotal, double valor, LocalDate dataQuitacao /*, Caixa caixa*/) {
        this.id = id;
        this.dataLancamento = dataLancamento;
        this.dataVencimento = dataVencimento;
        Desc = desc;
        this.pagamentoTotal = pagamentoTotal;
        this.valor = valor;
        this.dataQuitacao = dataQuitacao;
        //this.caixa = caixa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public double getPagamentoTotal() {
        return pagamentoTotal;
    }

    public void setPagamentoTotal(double pagamentoTotal) {
        this.pagamentoTotal = pagamentoTotal;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataQuitacao() {
        return dataQuitacao;
    }

    public void setDataQuitacao(LocalDate dataQuitacao) {
        this.dataQuitacao = dataQuitacao;
    }
}
