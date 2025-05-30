package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.DoacaoInstituicaoCreateDTO;
import com.hbs.hbsfinan.exceptions.DoacaoInstituicaoNotFoundException;
import com.hbs.hbsfinan.model.DoacaoInstituicao;
import com.hbs.hbsfinan.repository.implementation.DoacaoInstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoacaoInstituicaoService {

    @Autowired
    private DoacaoInstituicaoRepository doacaoInstituicaoRepository;

    public void save(DoacaoInstituicaoCreateDTO dto) {
        doacaoInstituicaoRepository.save(dto);
    }

    public void delete(int id) {
        boolean success = doacaoInstituicaoRepository.delete(id);
        if (!success) {
            throw new DoacaoInstituicaoNotFoundException("Doação de instituição com ID " + id + " não encontrada para exclusão.");
        }
    }

    public void update(DoacaoInstituicao doacaoInstituicao) {
        doacaoInstituicaoRepository.update(doacaoInstituicao);
    }

    public DoacaoInstituicao findById(int id) {
        DoacaoInstituicao doacao = doacaoInstituicaoRepository.findById(id);
        if (doacao == null) {
            throw new DoacaoInstituicaoNotFoundException("Doação de instituição com ID " + id + " não encontrada.");
        }
        return doacao;
    }

    public List<DoacaoInstituicao> findAll() {
        return doacaoInstituicaoRepository.findAll();
    }

    public List<DoacaoInstituicao> findByCaixa(int idCaixa) {
        return doacaoInstituicaoRepository.findByCaixa(idCaixa);
    }
}