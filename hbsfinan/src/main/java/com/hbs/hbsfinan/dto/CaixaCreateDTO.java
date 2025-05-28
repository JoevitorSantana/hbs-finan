package com.hbs.hbsfinan.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CaixaCreateDTO {

    @NotNull(message = "O valor inicial é obrigatório")
    private double valorInicial;

    @NotNull(message = "A data de abertura do caixa é obrigatória")
    private LocalDateTime dataAberturaCaixa;

    @NotNull(message = "O ID do funcionário é obrigatório")
    private int funId;

    // Getters e Setters
    public double getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(double valorInicial) {
        this.valorInicial = valorInicial;
    }

    public LocalDateTime getDataAberturaCaixa() {
        return dataAberturaCaixa;
    }

    public void setDataAberturaCaixa(LocalDateTime dataAberturaCaixa) {
        this.dataAberturaCaixa = dataAberturaCaixa;
    }

    public int getFunId() {
        return funId;
    }

    public void setFunId(int funId) {
        this.funId = funId;
    }
}
