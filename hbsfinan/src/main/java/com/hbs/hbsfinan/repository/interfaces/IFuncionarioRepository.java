package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.FuncionarioCreateDTO;
import com.hbs.hbsfinan.model.Funcionario;

import java.util.List;

public interface IFuncionarioRepository {
    void save(Funcionario funcionario);
    Funcionario findById(int id);
    List<Funcionario> findAll();
    void delete(int id);
    void update(Funcionario funcionario);
    Funcionario findByEmail(String email);
    Funcionario findByCpf(String cpf);




}
