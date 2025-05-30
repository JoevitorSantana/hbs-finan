package com.hbs.hbsfinan.repository.interfaces;
import com.hbs.hbsfinan.model.Anotacoes;
import java.util.List;

public interface IAnotacoesRepository
{
    void save(Anotacoes anotacoes);
    boolean delete(int id);
    void update(Anotacoes anotacoes);
    Anotacoes findById(int id);
    List<Anotacoes> findAll();
}
