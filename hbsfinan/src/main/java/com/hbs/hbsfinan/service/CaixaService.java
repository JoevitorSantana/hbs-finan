package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.CaixaCreateDTO;
import com.hbs.hbsfinan.exceptions.CaixaNotFoundException;
import com.hbs.hbsfinan.exceptions.ExistingCaixaException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.model.DoacaoMonetaria;
import com.hbs.hbsfinan.repository.implementation.CaixaRepository;
import com.hbs.hbsfinan.repository.implementation.DoacaoMonetariaRepository;
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
        Caixa caixaTeste = caixaRepository.findByDate(dto.getDataAberturaCaixa().toLocalDate());
        if (caixaTeste != null) {
            throw new ExistingCaixaException("Já existe caixa aberto para esta data!"); 
        }
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

        if (findDoacoesMonetarias(id).size() > 0)
            throw new ExistingCaixaException("Não é possível excluir um caixa com doações!");

        if (!caixaRepository.delete(id)) {
            throw new RuntimeException("Erro ao excluir caixa.");
        }
    }

    public void update(Caixa caixa) {
        if (caixaRepository.findById(caixa.getId()) == null)
            throw new CaixaNotFoundException("Caixa não encontrado!");
        caixaRepository.update(caixa);
    }

    public List<DoacaoMonetaria> findDoacoesMonetarias(int idCaixa)
    {
        DoacaoMonetariaRepository doacaoMonetariaRepository = new DoacaoMonetariaRepository();
        return doacaoMonetariaRepository.findByCaixa(idCaixa);
    }
}
