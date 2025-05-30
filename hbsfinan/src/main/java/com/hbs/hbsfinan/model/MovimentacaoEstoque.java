package com.hbs.hbsfinan.model;

import com.hbs.hbsfinan.enums.TipoMovimentacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produtos produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @Column(nullable = false)
    private long quantidadeMovimentada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private LocalDateTime dataHoraMovimentacao;

    private String observacao;

    public MovimentacaoEstoque() {
        this.dataHoraMovimentacao = LocalDateTime.now();
    }
    public MovimentacaoEstoque(Produtos produto, Funcionario funcionario, long quantidadeMovimentada, TipoMovimentacao tipo, String observacao) {
        this();
        this.produto = produto;
        this.funcionario = funcionario;
        this.quantidadeMovimentada = quantidadeMovimentada;
        this.tipo = tipo;
        this.observacao = observacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Produtos getProduto() {
        return produto;
    }
    public void setProduto(Produtos produto) {
        this.produto = produto;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public long getQuantidadeMovimentada() {
        return quantidadeMovimentada;
    }
    public void setQuantidadeMovimentada(long quantidadeMovimentada) {
        this.quantidadeMovimentada = quantidadeMovimentada;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }
    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataHoraMovimentacao() {
        return dataHoraMovimentacao;
    }
    public void setDataHoraMovimentacao(LocalDateTime dataHoraMovimentacao) {
        this.dataHoraMovimentacao = dataHoraMovimentacao;
    }

    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}