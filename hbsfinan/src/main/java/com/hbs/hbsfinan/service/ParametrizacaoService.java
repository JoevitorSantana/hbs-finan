package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.exceptions.ParametrizacaoJaCadastradaException;
import com.hbs.hbsfinan.exceptions.ParametrizacaoNaoEncontradaException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Parametrizacao;
import com.hbs.hbsfinan.repository.implementation.ParametrizacaoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;


@Service
public class ParametrizacaoService {
    private ParametrizacaoRepository parametrizacaoRepository;
    private Conexao dbConnFactory;

    public ParametrizacaoService() {}

    public ParametrizacaoService(Conexao dbConnFactory) {
        this.dbConnFactory = dbConnFactory;
        this.parametrizacaoRepository = new ParametrizacaoRepository(dbConnFactory);
    }

    public Parametrizacao getParametrizacao() {
        Parametrizacao parametrizacao = parametrizacaoRepository.findFirst();
        if (parametrizacao == null) {
            throw new ParametrizacaoNaoEncontradaException("Nenhuma parametrização cadastrada.");
        }
        return parametrizacao;
    }
    @Transactional
    public Parametrizacao createParametrizacao(Parametrizacao parametrizacao) {
        if (parametrizacaoRepository.exists()) {
            throw new ParametrizacaoJaCadastradaException("Já existe uma parametrização cadastrada. Use o método de atualização.");
        }
        parametrizacaoRepository.save(parametrizacao);
        return parametrizacaoRepository.findFirst();
    }
    @Transactional
    public Parametrizacao updateParametrizacao(Parametrizacao parametrizacao) {
        Parametrizacao atual = parametrizacaoRepository.findFirst();

        if (atual == null) {
            throw new ParametrizacaoNaoEncontradaException("Parametrização não encontrada para atualização.");
        }

        parametrizacao.setId(atual.getId());

        parametrizacaoRepository.update(parametrizacao);
        return parametrizacaoRepository.findFirst(); // Retorna a parametrização atualizada do banco
    }

    public boolean existsParametrizacao() {
        return parametrizacaoRepository.exists();
    }
}