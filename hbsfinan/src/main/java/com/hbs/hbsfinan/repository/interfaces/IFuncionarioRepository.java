package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.FuncionarioCreateDTO;
import com.hbs.hbsfinan.model.Funcionario;

import java.util.List;

public interface IFuncionarioRepository {
    boolean save(Funcionario funcionario);
    Funcionario findById(int id);
    List<Funcionario> findAll();
    boolean delete(int id);
    boolean update(Funcionario funcionario);
    //Funcionario findByEmail(String email);
    Funcionario findByCpf(String cpf);
}
