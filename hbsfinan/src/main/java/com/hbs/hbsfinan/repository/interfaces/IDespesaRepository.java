package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.model.Despesa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IDespesaRepository {

    void save(Despesa despesa);
    Despesa findById(int id);
    List<Despesa> findAll();
    void delete(int id);
    void update(Despesa despesa);
    Optional<Despesa> findByIdComCaixa(int id);

}

