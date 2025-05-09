package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.model.Parametrizacao;
import com.hbs.hbsfinan.repository.interfaces.IParametrizacaoRepository;
import com.hbs.hbsfinan.exceptions.ParametrizacaoJaCadastradaException;
import com.hbs.hbsfinan.exceptions.ParametrizacaoNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParametrizacaoService {

    private final IParametrizacaoRepository repo;

    @Autowired
    public ParametrizacaoService(IParametrizacaoRepository repo) {
        this.repo = repo;
    }

    /**
     * Verifica se já existe configuração cadastrada.
     */
    public boolean exists() {
        return repo.exists();
    }

    /**
     * Recupera a configuração existente ou lança exceção se não existir.
     */
    public Parametrizacao get() {
        return repo.findFirst()
                .orElseThrow(() -> new ParametrizacaoNaoEncontradaException("Parametrização não encontrada."));
    }

    /**
     * Cria uma nova parametrização. Só permite se não houver registro.
     */
    @Transactional
    public Parametrizacao create(Parametrizacao dto) {
        if (repo.exists()) {
            throw new ParametrizacaoJaCadastradaException("Já existe uma parametrização cadastrada.");
        }
        repo.save(dto);
        return repo.findFirst().get();
    }

    /**
     * Atualiza a parametrização existente com os dados fornecidos.
     */
    @Transactional
    public Parametrizacao update(Parametrizacao dto) {
        Parametrizacao atual = repo.findFirst()
                .orElseThrow(() -> new ParametrizacaoNaoEncontradaException("Parametrização não encontrada para atualização."));
        // Copia cada campo do DTO para a entidade existente
        atual.setNomeEmpresa(dto.getNomeEmpresa());
        atual.setRazaoSocial(dto.getRazaoSocial());
        atual.setEnderecoRua(dto.getEnderecoRua());
        atual.setEnderecoBairro(dto.getEnderecoBairro());
        atual.setEnderecoCidade(dto.getEnderecoCidade());
        atual.setEnderecoCep(dto.getEnderecoCep());
        atual.setEnderecoEstado(dto.getEnderecoEstado());
        atual.setEmail(dto.getEmail());
        atual.setTelefone(dto.getTelefone());
        atual.setCelular(dto.getCelular());
        atual.setNomeProprietario(dto.getNomeProprietario());
        atual.setCnpj(dto.getCnpj());
        atual.setLogoPequenaUrl(dto.getLogoPequenaUrl());
        atual.setLogoGrandeUrl(dto.getLogoGrandeUrl());
        repo.update(atual);
        return atual;
    }
}
