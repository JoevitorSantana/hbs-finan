package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IApoiadorRepository {

    void save(Apoiador apoiador);
    Apoiador findById(Long id);
    List<Apoiador> findAll();
    boolean delete(Long id);
    void update(Apoiador apoiador);
    Apoiador findByEmail(String email);
}
