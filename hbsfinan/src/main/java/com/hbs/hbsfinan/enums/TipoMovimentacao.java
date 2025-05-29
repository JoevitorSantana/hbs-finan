package com.hbs.hbsfinan.enums;

public enum TipoMovimentacao {
    ENTRADA_DOACAO_ALIMENTICIA("Entrada por Doação Alimentícia"),
    SAIDA_DOACAO_ALIMENTICIA("Saída por Doação Alimentícia"),
    ENTRADA_DOACAO_MATERIAL("Entrada por Doação de Material"),
    SAIDA_DOACAO_MATERIAL("Saída por Doação de Material"),
    ENTRADA_COMPRA("Entrada por Compra"),
    AJUSTE_ENTRADA("Ajuste de Inventário (Entrada)"),
    AJUSTE_SAIDA("Ajuste de Inventário (Saída)"),
    SAIDA_PERDA_DANO("Saída por Perda ou Dano"),
    ENTRADA_DEVOLUCAO("Entrada por Devolução"),
    SAIDA_VENDA("Saída por Venda");

    private final String descricao;

    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}