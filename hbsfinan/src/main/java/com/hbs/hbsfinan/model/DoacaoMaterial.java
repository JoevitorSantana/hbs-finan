package com.hbs.hbsfinan.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class DoacaoMaterial {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "funcionario_id", nullable = false)
        private Funcionario funcionario;

        private LocalDate data;

        @OneToMany(mappedBy = "doacaoMaterial", cascade = CascadeType.ALL)
        private List<ItemDoacaoMaterial> itens;

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

        public List<ItemDoacaoMaterial> getItens() {
            return itens;
        }

        public void setItens(List<ItemDoacaoMaterial> itens) {
            this.itens = itens;
        }
}

