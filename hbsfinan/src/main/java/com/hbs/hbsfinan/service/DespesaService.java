package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.dto.QuitacaoDTO;
import com.hbs.hbsfinan.exceptions.DespesaNotFoundException;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.repository.implementation.CaixaRepository;
import com.hbs.hbsfinan.repository.implementation.DespesaRepository;
import com.hbs.hbsfinan.repository.interfaces.IDespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;
    @Autowired
    private IDespesaRepository iDespesaRepository;
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
        caixa.setValorInicial(caixa.getValorInicial() + dto.getValor());

        caixaRepository.update(caixa); // se estiver usando um repositório manual

        return despesaRepository.save(despesa);
    }



    public Despesa quitarDespesa(int idDespesa, QuitacaoDTO dto) {
        Despesa despesa = despesaRepository.findById(idDespesa)
                .orElseThrow(() -> new DespesaNotFoundException("Despesa não encontrada"));

        if (despesa.getDataQuitacao() != null) {
            throw new RuntimeException("Despesa já está quitada");
        }

        despesa.setDataQuitacao(LocalDate.now());
        Despesa despesaAtualizada = despesaRepository.update(despesa);

        // BUSCAR O CAIXA EXPLICITAMENTE, para garantir que não seja null
        Integer idCaixa = despesa.getCaixa().getId(); // ou despesa.getCaixaId()
        Caixa caixa = caixaRepository.findById(idCaixa);
        if (caixa == null) {
            throw new RuntimeException("Caixa não encontrado para a despesa");
        }

        double valorInicial = caixa.getValorInicial();

        BigDecimal saldoAtualizado = BigDecimal.valueOf(valorInicial)
                .subtract(BigDecimal.valueOf(despesa.getValor()));

        caixa.setValorInicial(saldoAtualizado.doubleValue());
        caixaRepository.update(caixa);

        return despesaAtualizada;
    }



    public List<Despesa> findAll() {
        return despesaRepository.findAll();
    }

    //    public Despesa findById(int id) {
//        return despesaRepository.findById(id)
//                .orElseThrow(() -> new DespesaNotFoundException("Despesa não encontrada"));
//    }
    public Despesa findById(int id) {
        return iDespesaRepository.findByIdComCaixa(id)
                .orElseThrow(() -> new DespesaNotFoundException("Despesa não encontrada"));
    }


    public Despesa atualizarDespesaComImpactoNoCaixa(int id, DespesaCreateDTO dto) {
        Despesa despesaExistente = findById(id);

        if (despesaExistente.getDataQuitacao() != null) {
            throw new RuntimeException("Despesa já quitada não pode ser editada.");
        }

        double valorAntigo = despesaExistente.getValor();
        double novoValor = dto.getValor();

        // Atualiza os campos da despesa
        if (dto.getDescricao() != null)
            despesaExistente.setDescricao(dto.getDescricao());
        if (dto.getDataLancamento() != null)
            despesaExistente.setDataLancamento(dto.getDataLancamento());
        if (dto.getDataVencimento() != null)
            despesaExistente.setDataVencimento(dto.getDataVencimento());
        despesaExistente.setValor(novoValor);

        // Ajusta o caixa
        Caixa caixa = despesaExistente.getCaixa();
        if (caixa == null) {
            throw new RuntimeException("Caixa associado à despesa não existe.");
        }

        double diferenca = novoValor - valorAntigo;
        caixa.setValorInicial(caixa.getValorInicial() - diferenca);
        caixaRepository.update(caixa);

        return despesaRepository.update(despesaExistente);
    }


    public void delete(int id) {
        Despesa despesa = findById(id);
        despesaRepository.delete(despesa.getId());
    }


}
