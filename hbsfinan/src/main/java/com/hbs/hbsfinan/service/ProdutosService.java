package com.hbs.hbsfinan.service;



import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.repository.implementation.EventoRepository;
import com.hbs.hbsfinan.repository.implementation.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutosService {

    @Autowired
    private ProdutosRepository produtosRepository;

    private Conexao dbConnFactory;

    public ProdutosService(){}

    public ProdutosService(Conexao dbConnFactory)
    {
        this.dbConnFactory = dbConnFactory;
        this.produtosRepository = new ProdutosRepository(dbConnFactory);
    }
    public void save(Produtos produtos) {
        produtosRepository.save(produtos);
    }

    public List<Produtos> findAll() {
        return produtosRepository.findAll();
    }

    public void delete(int id) {
        produtosRepository.delete(id);
    }

    public void update(Produtos produtos) {
        produtosRepository.update(produtos);
    }

    public Produtos findById(int id) {
        return produtosRepository.findById(id);
    }
}
