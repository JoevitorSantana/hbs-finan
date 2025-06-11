//package com.hbs.hbsfinan.model;
//
//import com.hbs.hbsfinan.enums.TipoMovimentacao;
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//public class MovimentacaoEstoque {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "produto_id", nullable = false)
//    private Produtos produto;
//
//    private TipoMovimentacao tipo; //incrementar ou decrementar
//
//    @Column(nullable = false)
//    private long quantidadeMovimentada;
//
//
//    public MovimentacaoEstoque() {}
//    public MovimentacaoEstoque(Produtos produto, TipoMovimentacao tipo, long quantidadeMovimentada) {
//        this.produto = produto;
//        this.tipo = tipo;
//        this.quantidadeMovimentada = quantidadeMovimentada;
//    }
//
//    public Long getId() {
//        return id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Produtos getProduto() {
//        return produto;
//    }
//    public void setProduto(Produtos produto) {
//        this.produto = produto;
//    }
//
//    public TipoMovimentacao getTipo() {
//        return tipo;
//    }
//    public void setTipo(TipoMovimentacao tipo) {
//        this.tipo = tipo;
//    }
//
//    public long getQuantidadeMovimentada() {
//        return quantidadeMovimentada;
//    }
//    public void setQuantidadeMovimentada(long quantidadeMovimentada) {
//        this.quantidadeMovimentada = quantidadeMovimentada;
//    }
//}