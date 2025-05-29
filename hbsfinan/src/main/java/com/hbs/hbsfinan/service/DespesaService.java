package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.exceptions.DespesaNotFoundException;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.repository.implementation.CaixaRepository;
import com.hbs.hbsfinan.repository.implementation.DespesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final CaixaRepository caixaRepository;

    public DespesaService(DespesaRepository despesaRepository, CaixaRepository caixaRepository) {
        this.despesaRepository = despesaRepository;
        this.caixaRepository = caixaRepository;
    }

    public Despesa criarDespesa(DespesaCreateDTO dto, int idCaixa) {
        Caixa caixa = caixaRepository.findById(idCaixa);
        if (caixa == null) {
            throw new RuntimeException("Caixa não encontrado");
        }

        // Salva a despesa
        despesaRepository.save(dto);

        // Busca a despesa recém criada (pela "única combinação" de campos)
        Despesa despesaCriada = despesaRepository.findByUniqueFields(dto, idCaixa);
        if (despesaCriada == null) {
            throw new RuntimeException("Erro ao buscar despesa criada");
        }

        // Atualiza o valor final do caixa somando o valor da despesa
        double novoValorFinal = caixa.getValorFinal() + despesaCriada.getValor();
        caixa.setValorFinal(novoValorFinal);
        caixaRepository.update(caixa);

        return despesaCriada;
    }


    public List<Despesa> findAll() {
        return despesaRepository.findAll();
    }

    public void delete(int id) {
        despesaRepository.delete(id);
    }

    public void update(Despesa despesa) {
        despesaRepository.update(despesa);
    }

    public Despesa findById(int id) {
        Despesa despesa = despesaRepository.findById(id);
        if (despesa == null)
            throw new DespesaNotFoundException("Despesa não encontrada com ID " + id);
        return despesa;
    }
}
