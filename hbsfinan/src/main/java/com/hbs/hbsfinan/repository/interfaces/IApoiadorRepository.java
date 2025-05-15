package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.Funcionario;

import java.util.List;

public interface IApoiadorRepository {

    void save(ApoiadorDTO apoiadorDTO);
    Apoiador findById(int id);
    List<Apoiador> findAll();
    void delete(int id);
    void update(Apoiador apoiador);
    Apoiador findByEmail(String email);
}
