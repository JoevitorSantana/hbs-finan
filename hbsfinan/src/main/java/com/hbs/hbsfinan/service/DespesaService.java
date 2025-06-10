package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.dto.QuitacaoDTO;
import com.hbs.hbsfinan.exceptions.DespesaNotFoundException;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.repository.implementation.CaixaRepository;
import com.hbs.hbsfinan.repository.implementation.DespesaRepository;

import java.time.LocalDate;
import java.util.List;

public class DespesaService {

    private final DespesaRepository despesaRepository;
private CaixaRepository caixaRepository;
    public DespesaService(DespesaRepository despesaRepository, CaixaRepository caixaRepository) {
        this.despesaRepository = despesaRepository;
        this.caixaRepository = caixaRepository;
    }

    public Despesa criarDespesa(DespesaCreateDTO dto, int idCaixa) {
        Caixa caixa = caixaRepository.findById(idCaixa);
        if (caixa == null) {
            throw new RuntimeException("Caixa não encontrado com ID: " + idCaixa);
        }

        Despesa despesa = new Despesa();
        despesa.setDescricao(dto.getDescricao());
        despesa.setValor(dto.getValor());
        despesa.setDataLancamento(dto.getDataLancamento());
        despesa.setDataVencimento(dto.getDataVencimento());

        despesa.setPagamentoTotal(0.0);
        System.out.println("PagamentoTotal setado para: " + despesa.getPagamentoTotal());

        despesa.setCaixa(caixa); // ESSENCIAL


        // Atualiza o saldo do caixa (soma o valor da despesa ao saldo)
        caixa.setValorFinal(caixa.getValorFinal() + dto.getValor());

        caixaRepository.update(caixa); // se estiver usando um repositório manual

        return despesaRepository.save(despesa);
    }



    public Despesa quitarDespesa(int idDespesa, QuitacaoDTO dto) {
        Despesa despesa = despesaRepository.findById(idDespesa)
                .orElseThrow(() -> new DespesaNotFoundException("Despesa não encontrada"));

        if (despesa.getDataQuitacao() != null) {
            throw new RuntimeException("Despesa já está quitada");
        }

        // lógica para aplicar multa ou desconto pode ser implementada aqui, ex:
        // comparar dto.getDataQuitacao() com despesa.getDataVencimento()

        despesa.setDataQuitacao(LocalDate.now());


        // Atualize no banco
        return despesaRepository.update(despesa);
    }

    public List<Despesa> findAll() {
        return despesaRepository.findAll();
    }

    public Despesa findById(int id) {
        return despesaRepository.findById(id)
                .orElseThrow(() -> new DespesaNotFoundException("Despesa não encontrada"));
    }

    public Despesa update(Despesa despesa) {
        // Você pode validar aqui se a despesa existe antes de atualizar
        findById(despesa.getId());
        return despesaRepository.update(despesa);
    }

    public void delete(int id) {
        Despesa despesa = findById(id);
        despesaRepository.delete(despesa.getId());
    }


}
