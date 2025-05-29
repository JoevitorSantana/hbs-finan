package com.hbs.hbsfinan.dto;


import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class QuitacaoDTO {

    @NotNull(message = "O valor do pagamento total é obrigatório")
    private Double pagamentoTotal;

    public QuitacaoDTO() {}

    public Double getPagamentoTotal() {
        return pagamentoTotal;
    }

    public void setPagamentoTotal(Double pagamentoTotal) {
        this.pagamentoTotal = pagamentoTotal;
    }
}
