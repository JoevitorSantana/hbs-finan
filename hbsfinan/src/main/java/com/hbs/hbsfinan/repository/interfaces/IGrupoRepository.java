package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.model.Grupo;

import java.util.List;


public interface IGrupoRepository
{
    void save(Grupo grupo);
    void delete(int id);
    void update(Grupo grupo);
    Grupo findById(int id);
    Grupo findByNome(String nome);
    List<Grupo> findAll();
}
