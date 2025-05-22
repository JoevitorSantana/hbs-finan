package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.FuncionarioCreateDTO;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.implementation.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository funcionarioRepository;


    public void save(FuncionarioCreateDTO funcionario) {
        Funcionario existente = funcionarioRepository.findByCpf(funcionario.getCpf());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe um funcionário com este CPF.");
        }
        funcionarioRepository.save(funcionario);
    }


    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public void delete(int id) {
        funcionarioRepository.delete(id);
    }

    public void update(Funcionario funcionario) {
        funcionarioRepository.update(funcionario);
    }

    public Funcionario findById(int id) {
        return funcionarioRepository.findById(id);
    }
}
