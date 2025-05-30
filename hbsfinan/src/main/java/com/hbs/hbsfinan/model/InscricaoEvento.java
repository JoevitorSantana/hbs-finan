package com.hbs.hbsfinan.model;

import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.Evento;
import jakarta.persistence.*;

@Entity
@Table(name = "inscricao_evento")
public class InscricaoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "apoiador_id", nullable = false)
    private Apoiador apoiador;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    // Construtores
    public InscricaoEvento() {}
    public InscricaoEvento(Apoiador apoiador, Evento evento) {
        this.apoiador = apoiador;
        this.evento = evento;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Apoiador getApoiador() { return apoiador; }
    public void setApoiador(Apoiador apoiador) { this.apoiador = apoiador; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
}
