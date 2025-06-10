package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.exceptions.InscricaoEventoNotFoundException;
import com.hbs.hbsfinan.exceptions.ApoiadorNotFoundException; // Importe suas exceções
import com.hbs.hbsfinan.exceptions.EventoNotFoundException;   // Importe suas exceções
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Apoiador; // Importe Apoiador
import com.hbs.hbsfinan.model.Evento;   // Importe Evento
import com.hbs.hbsfinan.model.InscricaoEvento;
import com.hbs.hbsfinan.repository.implementation.InscricaoEventoRepository;
import com.hbs.hbsfinan.repository.implementation.ApoiadorRepository; // Importe ApoiadorRepository
import com.hbs.hbsfinan.repository.implementation.EventoRepository;     // Importe EventoRepository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe @Transactional

import java.util.List;

@Service
public class InscricaoEventoService {
    private InscricaoEventoRepository inscricaoEventoRepository;
    private ApoiadorRepository apoiadorRepository;
    private EventoRepository eventoRepository;

    private Conexao dbConnFactory;

    public InscricaoEventoService() {}
    public InscricaoEventoService(Conexao dbConnFactory) {
        this.dbConnFactory = dbConnFactory;
        this.inscricaoEventoRepository = new InscricaoEventoRepository(dbConnFactory);
        this.apoiadorRepository = new ApoiadorRepository(dbConnFactory);
        this.eventoRepository = new EventoRepository(dbConnFactory);
    }

    @Transactional
    public InscricaoEvento save(InscricaoEvento inscricaoEvento) {
        Long apoiadorId = inscricaoEvento.getApoiador().getId();
        Apoiador apoiadorExistente = apoiadorRepository.findById(apoiadorId);
        if (apoiadorExistente == null) {
            throw new ApoiadorNotFoundException("Apoiador com ID " + apoiadorId + " não encontrado.");
        }
        inscricaoEvento.setApoiador(apoiadorExistente);

        int eventoId = inscricaoEvento.getEvento().getId();
        Evento eventoExistente = eventoRepository.findById(eventoId);
        if (eventoExistente == null) {
            throw new EventoNotFoundException("Evento com ID " + eventoId + " não encontrado.");
        }
        inscricaoEvento.setEvento(eventoExistente);
        inscricaoEventoRepository.save(inscricaoEvento);
        return inscricaoEventoRepository.findById(inscricaoEvento.getId());
    }

    /** Retorna todas as inscrições de evento. */
    public List<InscricaoEvento> findAll() {
        return inscricaoEventoRepository.findAll();
    }

    /** Deleta uma inscrição de evento pelo ID. */
    @Transactional
    public void delete(Long id) {
        InscricaoEvento inscricaoExistente = inscricaoEventoRepository.findById(id);
        if (inscricaoExistente == null) {
            throw new InscricaoEventoNotFoundException("Inscrição com ID " + id + " não encontrada para exclusão.");
        }
        if (!inscricaoEventoRepository.delete(id)) {
            throw new RuntimeException("Erro ao excluir inscrição com ID " + id + ".");
        }
    }

    /** Atualiza uma inscrição de evento existente. */
    @Transactional
    public InscricaoEvento update(InscricaoEvento inscricaoEvento) {
        InscricaoEvento inscricaoExistente = inscricaoEventoRepository.findById(inscricaoEvento.getId());
        if (inscricaoExistente == null) {
            throw new InscricaoEventoNotFoundException("Inscrição com ID " + inscricaoEvento.getId() + " não encontrada para atualização.");
        }

        Long apoiadorId = inscricaoEvento.getApoiador().getId();
        Apoiador apoiadorExistente = apoiadorRepository.findById(apoiadorId);
        if (apoiadorExistente == null) {
            throw new ApoiadorNotFoundException("Apoiador com ID " + apoiadorId + " não encontrado para a atualização da inscrição.");
        }
        inscricaoEvento.setApoiador(apoiadorExistente);

        int eventoId = inscricaoEvento.getEvento().getId();
        Evento eventoExistente = eventoRepository.findById(eventoId);
        if (eventoExistente == null) {
            throw new EventoNotFoundException("Evento com ID " + eventoId + " não encontrado para a atualização da inscrição.");
        }
        inscricaoEvento.setEvento(eventoExistente);

        inscricaoEventoRepository.update(inscricaoEvento);
        return inscricaoEventoRepository.findById(inscricaoEvento.getId());
    }

    /** Retorna uma inscrição de evento pelo ID. */
    public InscricaoEvento findById(Long id) {
        InscricaoEvento inscricaoEvento = inscricaoEventoRepository.findById(id);
        if (inscricaoEvento == null) {
            throw new InscricaoEventoNotFoundException("Inscrição com ID " + id + " não encontrada.");
        }
        return inscricaoEvento;
    }
}