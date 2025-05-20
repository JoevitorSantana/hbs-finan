//package com.hbs.hbsfinan.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToOne;
//
//import java.net.DatagramSocket;
//import java.util.Date;
//
//@Entity
//public class Despesas {
//
//    @Id
//    private int id;
//    private Date Data_Lancamento;
//    private Date Data_Vencimento;
//    private String Desc;
//    private double PagamentoTotal; //valor se pagou com multa ou desconto
//    private double valor;
//    private Date Data_Quitacao;
//
//    //one to one ->cada pessoa tem um cpf
//    //many to one ->um cliente pode ter mts pedidos
//
//    //@OneToOne
//    //@JoinColumn(name = cx_id, nullable = false_);
//    //private Caixa caixa;
//
//    //PENSAR: AQUI VAI TER GET E SET DO CAIXA!!!!
//    public Despesas() {
//    }
//
//    public Despesas(int id, Date data_Lancamento, Date data_Vencimento, String desc, double valor, Date data_Quitacao) {
//        this.id = id;
//        Data_Lancamento = data_Lancamento;
//        Data_Vencimento = data_Vencimento;
//        Desc = desc;
//        this.valor = valor;
//        Data_Quitacao = data_Quitacao;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public Date getData_Lancamento() {
//        return Data_Lancamento;
//    }
//
//    public void setData_Lancamento(Date data_Lancamento) {
//        Data_Lancamento = data_Lancamento;
//    }
//
//    public Date getData_Vencimento() {
//        return Data_Vencimento;
//    }
//
//    public void setData_Vencimento(Date data_Vencimento) {
//        Data_Vencimento = data_Vencimento;
//    }
//
//    public String getDesc() {
//        return Desc;
//    }
//
//    public void setDesc(String desc) {
//        Desc = desc;
//    }
//
//    public double getValor() {
//        return valor;
//    }
//
//    public void setValor(double valor) {
//        this.valor = valor;
//    }
//
//    public Date getData_Quitacao() {
//        return Data_Quitacao;
//    }
//
//    public void setData_Quitacao(Date data_Quitacao) {
//        Data_Quitacao = data_Quitacao;
//    }
//}
