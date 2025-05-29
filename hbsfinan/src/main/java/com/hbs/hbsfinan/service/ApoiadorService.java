package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.implementation.ApoiadorRepository;
import com.hbs.hbsfinan.repository.implementation.GrupoRepository;
import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApoiadorService {


    private Conexao dbConnFactory;

    public ApoiadorService(){}

    public ApoiadorService(Conexao dbConnFactory){
        this.dbConnFactory = dbConnFactory;
        this.apoiadorRepository = new ApoiadorRepository(dbConnFactory);
    }


    @Autowired
    private ApoiadorRepository apoiadorRepository;

    public void save(Apoiador apoiador) {
        apoiadorRepository.save(apoiador);
    }

    public List<Apoiador> findAll() {
        return apoiadorRepository.findAll();
    }

    public void delete(Long id) {
        apoiadorRepository.delete(id);
    }

    public void update(Apoiador apoiador) {
        apoiadorRepository.update(apoiador);
    }

    public Apoiador findById(Long id) {
        return apoiadorRepository.findById(id);
    }

}
