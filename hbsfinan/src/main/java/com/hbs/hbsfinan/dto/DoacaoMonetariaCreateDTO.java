package com.hbs.hbsfinan.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class DoacaoMonetariaCreateDTO {

    @NotNull(message = "O valor da doação é obrigatório")
    @Min(value = 1, message = "O valor da doação deve ser maior que zero")
    private double valor;

    // Apoiador é opcional (caso a doação seja anônima, por exemplo)
    private int idApoiador;

    @NotNull(message = "O ID do caixa é obrigatório")
    private int idCaixa;

    @NotNull(message = "Informe a data da doação")
    private LocalDateTime data;

    // Getters e Setters
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getIdApoiador() {
        return idApoiador;
    }

    public void setIdApoiador(int idApoiador) {
        this.idApoiador = idApoiador;
    }

    public int getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(int idCaixa) {
        this.idCaixa = idCaixa;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
