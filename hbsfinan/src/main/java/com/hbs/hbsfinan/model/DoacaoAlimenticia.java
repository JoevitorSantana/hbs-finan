package com.hbs.hbsfinan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

public class DoacaoAlimenticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id_doacao;

    private Date Data_Doacao;



    @ManyToOne
    @JoinColumn(name = "id")//ver se esta certo
    @JsonIgnore
    private Funcionario funcionario;

    public DoacaoAlimenticia(){}

    public DoacaoAlimenticia (Long Id_Doacao, Date Data_Doacao, Funcionario funcionario)
    {
        this.Data_Doacao=Data_Doacao;
        this.Id_doacao=Id_Doacao;
        this.funcionario= funcionario;
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



    @JsonProperty
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @JsonProperty("func_id")
    public int getFuncId() {
        if (funcionario != null)
        {
            return funcionario.getId();
        }
        else
        {
            return 0;
        }
    }


}
