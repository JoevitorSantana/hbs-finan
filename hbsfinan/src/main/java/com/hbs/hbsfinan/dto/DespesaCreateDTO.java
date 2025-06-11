package com.hbs.hbsfinan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class DespesaCreateDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "A data de lançamento é obrigatória")
    private LocalDate dataLancamento;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "A data de vencimento é obrigatória")
    private LocalDate dataVencimento;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    private Double valor;

//    @NotNull(message = "O caixa é obrigatório")
//    private Integer idCaixa;
//
//    public Integer getIdCaixa() {
//        return idCaixa;
//    }
//
//    public void setIdCaixa(Integer idCaixa) {
//        this.idCaixa = idCaixa;
//    }

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

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }



}
