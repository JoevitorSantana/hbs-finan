package com.hbs.hbsfinan.service;
import com.hbs.hbsfinan.exceptions.AnotacoesNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.model.Anotacoes;
import org.springframework.stereotype.Service;
import com.hbs.hbsfinan.repository.implementation.AnotacoesRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnotacoesService {
    private AnotacoesRepository anotacoesRepository;
    private Conexao dbConnFactory;

    public AnotacoesService(){}

    public AnotacoesService(Conexao dbConnFactory)
    {
        this.dbConnFactory = dbConnFactory;
        this.anotacoesRepository = new AnotacoesRepository(dbConnFactory);
    }

    public void save(Anotacoes anotacoes){anotacoesRepository.save(anotacoes);}

    public void delete (int id){anotacoesRepository.delete(id);}

    public void update(Anotacoes anotacoes){anotacoesRepository.update(anotacoes);}

    public Anotacoes findById(int id){
        Anotacoes anotacoes = anotacoesRepository.findById(id);

        if(anotacoes == null){throw new AnotacoesNotFoundException("Anotaçao nao encontrada!");}

        return anotacoes;
    }

    public List<Anotacoes> findAll(){
        List<Anotacoes> anotacoes = anotacoesRepository.findAll();
        List<Anotacoes> anotacoesRetorno = new ArrayList<>();
        for(Anotacoes anotacao : anotacoes){
            anotacoesRetorno.add(anotacao);
        }
        return anotacoesRetorno;
    }

}
