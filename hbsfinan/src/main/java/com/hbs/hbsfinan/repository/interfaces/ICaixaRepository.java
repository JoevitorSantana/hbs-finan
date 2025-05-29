package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.CaixaCreateDTO;
import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.model.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface ICaixaRepository {
    void save(CaixaCreateDTO usuario);
    void update(Caixa caixa);
    boolean delete(int id );
    Caixa findById(int id);
    List<Caixa> findAll();
    Caixa findByDate(LocalDate date);
}
