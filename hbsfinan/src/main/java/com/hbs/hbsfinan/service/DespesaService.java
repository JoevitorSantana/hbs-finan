package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.implementation.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

//    @Autowired
//    private CaixaRepository caixaRepository;

    public Despesa CriarDespesa(DespesaCreateDTO dto, Long idCaixa){
        //Caixa caixa = caixaRepository.findById(idCaixa).orElseThrow(() -> new RuntimeException("Caixa não encontrado"));
        Caixa caixa = new Caixa();
        caixa.setId(idCaixa); // simula um caixa existente

        Despesa despesa = new Despesa();

        despesa.setDataLancamento(dto.getDataLancamento());
        despesa.setDataVencimento(dto.getDataVencimento());
        despesa.setDesc(dto.getDesc());
        //despesa.setPagamentoTotal(dto.getPagamentoTotal());
        despesa.setValor(dto.getValor());
        //despesa.setDataQuitacao(dto.getDataQuitacao());
        //despesa.setCaixa(caixa);

        return despesaRepository.save(despesa);

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
        return despesaRepository.findById(id);
    }


}
