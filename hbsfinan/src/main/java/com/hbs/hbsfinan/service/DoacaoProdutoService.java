package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.model.DoacaoProduto;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoAlimenticia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoacaoProdutoService {

    private final IDoacaoAlimenticia doacaoProdutoRepository;

    @Autowired
    public DoacaoProdutoService(IDoacaoAlimenticia doacaoProdutoRepository) {
        this.doacaoProdutoRepository = doacaoProdutoRepository;
    }

    public List<DoacaoProduto> listarTodos() {
        return doacaoProdutoRepository.findAll();
    }

    public void salvar(DoacaoProduto doacaoProduto) {
        doacaoProdutoRepository.save(doacaoProduto);
    }

    public void deletar(int id) {
        doacaoProdutoRepository.delete(id);
    }

    public DoacaoProduto buscarPorId(int id) {
        return doacaoProdutoRepository.findById(id);
    }
}
