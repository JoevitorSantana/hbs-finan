package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.exceptions.InscricaoEventoNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.InscricaoEvento;
import com.hbs.hbsfinan.repository.implementation.InscricaoEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscricaoEventoService {

    @Autowired
    private InscricaoEventoRepository inscricaoEventoRepository;
    private Conexao dbConnFactory;

    public InscricaoEventoService() {}

    public InscricaoEventoService(Conexao dbConnFactory) {
        this.dbConnFactory = dbConnFactory;
        this.inscricaoEventoRepository = new InscricaoEventoRepository(dbConnFactory);
    }

    public void save(InscricaoEvento inscricaoEvento) {
        // Aqui pode adicionar validações (ex: checar se o apoiador e evento existem, regras de negócio etc)
        inscricaoEventoRepository.save(inscricaoEvento);
    }

    public List<InscricaoEvento> findAll() {
        return inscricaoEventoRepository.findAll();
    }

    public void delete(Long id) {
        if (inscricaoEventoRepository.findById(id) == null)
            throw new InscricaoEventoNotFoundException("Inscrição não encontrada!");
        if (!inscricaoEventoRepository.delete(id))
            throw new RuntimeException("Erro ao excluir inscrição!");
    }

    public void update(InscricaoEvento inscricaoEvento) {
        inscricaoEventoRepository.update(inscricaoEvento);
    }

    public InscricaoEvento findById(Long id) {
        InscricaoEvento inscricaoEvento = inscricaoEventoRepository.findById(id);
        if (inscricaoEvento == null)
            throw new InscricaoEventoNotFoundException("Inscrição não encontrada!");
        return inscricaoEvento;
    }

    // Aqui pode criar conversores para DTO se quiser trabalhar com retorno customizado para Controller/API
}
