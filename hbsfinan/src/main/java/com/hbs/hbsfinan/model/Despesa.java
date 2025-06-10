package com.hbs.hbsfinan.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "despesa")
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "pagamento_total", nullable = false)
    private Double pagamentoTotal; // valor efetivo pago (com multa/ desconto)

    @Column(name = "valor", nullable = false)
    private double valor;

    @Column(name = "data_quitacao")
    private LocalDate dataQuitacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_caixa", nullable = false)
    private Caixa caixa;

    public Despesa() {
    }

    public Despesa(int id, LocalDate dataLancamento, LocalDate dataVencimento, String descricao,
                   double pagamentoTotal, double valor, LocalDate dataQuitacao, Caixa caixa) {
        this.id = id;
        this.dataLancamento = dataLancamento;
        this.dataVencimento = dataVencimento;
        this.descricao = descricao;
        this.pagamentoTotal = pagamentoTotal;
        this.valor = valor;
        this.dataQuitacao = dataQuitacao;
        this.caixa = caixa;
    }

    // Getters e Setters

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPagamentoTotal() {
        return pagamentoTotal;
    }

    public void setPagamentoTotal(Double pagamentoTotal) {
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

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }
}
