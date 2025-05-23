package com.hbs.hbsfinan.repository.interfaces;
import com.hbs.hbsfinan.model.Evento;
import java.util.List;
////
public interface IEventoRepository {
    void save(Evento evento);
    boolean delete(int id);
    void update(Evento evento);
    Evento findById(int id);
    Evento findByNome(String nome);
    List<Evento> findAll();
}
