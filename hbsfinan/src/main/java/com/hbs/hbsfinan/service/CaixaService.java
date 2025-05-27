package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.CaixaCreateDTO;
import com.hbs.hbsfinan.exceptions.CaixaNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.repository.implementation.CaixaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CaixaService {

    private CaixaRepository caixaRepository;
    private Conexao dbConnFactory;

    public CaixaService() {}

    public CaixaService(Conexao dbConnFactory) {
        this.dbConnFactory = dbConnFactory;
        this.caixaRepository = new CaixaRepository(dbConnFactory);
    }

    public void save(CaixaCreateDTO dto) {
        caixaRepository.save(dto);
    }

    public List<Caixa> findAll() {
        List<Caixa> caixas = caixaRepository.findAll();
        return caixas;
    }

    public Caixa findById(int id) {
        Caixa caixa = caixaRepository.findById(id);
        if (caixa == null)
            throw new CaixaNotFoundException("Caixa não encontrado!");
        return caixa;
    }

    public void delete(int id) {
        Caixa caixa = caixaRepository.findById(id);
        if (caixa == null)
            throw new CaixaNotFoundException("Caixa não encontrado!");

        if (!caixaRepository.delete(id)) {
            throw new RuntimeException("Erro ao excluir caixa.");
        }
    }

    public void update(Caixa caixa) {
        if (caixaRepository.findById(caixa.getId()) == null)
            throw new CaixaNotFoundException("Caixa não encontrado!");
        caixaRepository.update(caixa);
    }
}
