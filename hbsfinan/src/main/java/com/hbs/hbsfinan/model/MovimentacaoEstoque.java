package com.hbs.hbsfinan.model; // Mantendo o mesmo pacote das suas outras models

import com.hbs.hbsfinan.enums.TipoMovimentacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // Sem @Table, o JPA/Hibernate nomeará a tabela (provavelmente "movimentacao_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false) // É bom manter JoinColumn para FKs claras
    private Produtos produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false) // É bom manter JoinColumn para FKs claras
    private Funcionario funcionario;

    @Column(nullable = false) // O nome da coluna será inferido de "quantidadeMovimentada"
    private long quantidadeMovimentada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // O nome da coluna será inferido de "tipo"
    private TipoMovimentacao tipo;

    @Column(nullable = false) // O nome da coluna será inferido de "dataHoraMovimentacao"
    private LocalDateTime dataHoraMovimentacao;

    private String observacao; // O nome da coluna será inferido de "observacao"

    // Construtor padrão exigido pelo JPA
    public MovimentacaoEstoque() {
        this.dataHoraMovimentacao = LocalDateTime.now(); // Valor padrão
    }

    // Construtor para facilitar a criação de instâncias
    public MovimentacaoEstoque(Produtos produto, Funcionario funcionario, long quantidadeMovimentada, TipoMovimentacao tipo, String observacao) {
        this(); // Chama o construtor padrão para setar dataHoraMovimentacao
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