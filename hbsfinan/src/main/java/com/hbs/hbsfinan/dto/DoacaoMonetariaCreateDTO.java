package com.hbs.hbsfinan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

public class DoacaoMonetariaCreateDTO {


    private int id;

    @NotNull(message = "O valor da doação é obrigatório")
    @Min(value = 1, message = "O valor da doação deve ser maior que zero")
    private long valor;

    // Apoiador é opcional (caso a doação seja anônima, por exemplo)
    private int idApoiador;

    @NotNull(message = "O ID do caixa é obrigatório")
    private int idCaixa;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Informe a data da doação")
    private Date data;

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
