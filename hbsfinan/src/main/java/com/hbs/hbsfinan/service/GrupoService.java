package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.exceptions.GrupoNotFoundException;
import com.hbs.hbsfinan.model.Grupo;
import com.hbs.hbsfinan.repository.implementation.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
//
@Service
public class GrupoService {
    @Autowired
    private GrupoRepository grupoRepository;

    public void save(Grupo grupo) {grupoRepository.save(grupo);}

    public void delete(int id) {grupoRepository.delete(id);}

    public void update(Grupo grupo) {grupoRepository.update(grupo);}

    public Grupo findById(int id) {
        Grupo grupo = grupoRepository.findById(id);

        if (grupo == null) throw new GrupoNotFoundException("Grupo não encontrado!");

        return grupo;
    }

    public List<Grupo> findAll() {
        List<Grupo> grupos = grupoRepository.findAll();
        List<Grupo> gruposRetorno = new ArrayList<>();
        for (Grupo grupo : grupos)
            gruposRetorno.add(grupo);
        return gruposRetorno;
    }

    public Grupo findByNome(String nome) {
        Grupo grupo = grupoRepository.findByNome(nome);

        if (grupo == null) throw new GrupoNotFoundException("Grupo não encontrado!");

        return grupo;
    }
}
