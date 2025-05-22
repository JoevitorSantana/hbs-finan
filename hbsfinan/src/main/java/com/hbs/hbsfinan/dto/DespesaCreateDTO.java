package com.hbs.hbsfinan.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class DespesaCreateDTO {

    @NotNull(message = "A data de lançamento é obrigatória")
    private LocalDate dataLancamento;

    @NotNull(message = "A data de vencimento é obrigatória")
    private LocalDate dataVencimento;

    @NotBlank(message = "A descrição é obrigatória")
    private String Desc;

    @NotNull(message = "O valor total é obrigatório")
    private Double pagamentoTotal; //valor se pagou com multa ou desconto

    @NotNull(message = "O valor é obrigatório")
    private Double valor;

    @NotNull(message = "A data de quitação é obrigatória")
    private LocalDate dataQuitacao;


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

    public Double getPagamentoTotal() {
        return pagamentoTotal;
    }

    public void setPagamentoTotal(Double pagamentoTotal) {
        this.pagamentoTotal = pagamentoTotal;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDate getDataQuitacao() {
        return dataQuitacao;
    }

    public void setDataQuitacao(LocalDate dataQuitacao) {
        this.dataQuitacao = dataQuitacao;
    }
}

