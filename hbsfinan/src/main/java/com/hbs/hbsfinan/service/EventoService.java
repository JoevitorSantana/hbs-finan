package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.exceptions.EventoNotFoundException;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.repository.implementation.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;

    public void save(Evento evento) {eventoRepository.save(evento);};

    public void delete(int id) {eventoRepository.delete(id);}

    public void update(Evento evento) {eventoRepository.update(evento);}

    public Evento findById(int id) {
        Evento evento = eventoRepository.findById(id);

        if (evento == null) throw new EventoNotFoundException("Evento não encontrado!");

        return evento;
    }

    public List<Evento> findAll() {
        List<Evento> eventos = eventoRepository.findAll();
        List<Evento> eventosRetorno = new ArrayList<>();
        for (Evento evento : eventos)
            eventosRetorno.add(evento);
        return eventosRetorno;
    }

    public Evento findByNome(String nome) {
        Evento evento = eventoRepository.findByNome(nome);

        if (evento == null) throw new EventoNotFoundException("Evento não encontrado!");

        return evento;
    }
}
