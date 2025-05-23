package com.hbs.hbsfinan.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Date;

public class DoacaoAlimenticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id_doacao;

    private Date Data_Doacao;


    public DoacaoAlimenticia(){}

    public DoacaoAlimenticia (Long Id_Doacao, Date Data_Doacao)
    {
        this.Data_Doacao=Data_Doacao;
        this.Id_doacao=Id_Doacao;
    }


    public Long getId_doacao() {
        return Id_doacao;
    }

    public void setData_Doacao(Date data_Doacao) {
        Data_Doacao = data_Doacao;
    }

    public void setId_doacao(Long id_doacao) {
        Id_doacao = id_doacao;
    }

    public Date getData_Doacao() {
        return Data_Doacao;
    }
}
