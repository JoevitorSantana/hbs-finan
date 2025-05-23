//package com.hbs.hbsfinan.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//
//public class MovimentacaoAli {
//
//    @ManyToOne
//    @JoinColumn(name = "Id_doacao")
//    @JsonIgnore
//    private DoacaoAlimenticia doacaoAlimenticia;
//
//    @ManyToOne
//    @JoinColumn(name = "id")
//    @JsonIgnore
//    private Produto produto;
//
//    private int qtde;
//
//    public MovimentacaoAli(){}
//
//    public MovimentacaoAli(int qtde, Produto produto, DoacaoAlimenticia doacaoAlimenticia)
//    {
//        this.qtde=qtde;
//        this.doacaoAlimenticia=doacaoAlimenticia;
//        this.produto=produto;
//    }
//
//    public void setQtde(int qtde) {
//        this.qtde = qtde;
//    }
//
//    public int getQtde() {
//        return qtde;
//    }
//
//    public DoacaoAlimenticia getDoacaoAlimenticia() {
//        return doacaoAlimenticia;
//    }
//
//    public Produto getProduto() {
//        return produto;
//    }
//
//    @JsonProperty
//    public void setProduto(Produto produto) {
//        this.produto = produto;
//    }
//
//
//    @JsonProperty("id")
//    public int getId_Prod() {
//        if (produto != null)
//        {
//            return produto.getId();
//        }
//        else
//        {
//            return 0;
//        }
//    }
//
//    @JsonProperty
//    public void setDoacaoAlimenticia(DoacaoAlimenticia doacaoAlimenticia) {
//        this.doacaoAlimenticia = doacaoAlimenticia;
//    }
//
//    @JsonProperty("Id_doacao")
//    public Long getId_Doacao() {
//        if (doacaoAlimenticia != null)
//        {
//            return doacaoAlimenticia.getId_doacao();
//        }
//        else
//        {
//            return 0L;
//        }
//    }
//
//
//}
