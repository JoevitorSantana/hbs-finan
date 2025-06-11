package com.hbs.hbsfinan.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Entity
public class DoacaoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    private LocalDate data;

    @OneToMany(mappedBy = "doacaoProduto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDoacao> itens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }


    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<ItemDoacao> getItens() {
        return itens;
    }

    public void setItens(List<ItemDoacao> itens) {
        this.itens = itens;
    }
}
