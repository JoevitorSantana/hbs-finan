package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.FuncionarioCreateDTO;
import com.hbs.hbsfinan.exceptions.FuncionarioNotFoundException;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.implementation.FuncionarioRepository;

import java.util.List;

public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public void save(FuncionarioCreateDTO dto) {
        if (funcionarioRepository.findByCpf(dto.getCpf()) != null) {
            throw new RuntimeException("CPF já está cadastrado");
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(dto.getNome());
        funcionario.setCpf(dto.getCpf());
        funcionario.setEmail(dto.getEmail());
        funcionario.setFone(dto.getFone());
        funcionario.setEndereco(dto.getEndereco());
        funcionario.setSexo(dto.getSexo());
        funcionario.setDataNascimento(dto.getDataNascimento());

        boolean saved = funcionarioRepository.save(funcionario);
        if (!saved) {
            throw new RuntimeException("Erro ao salvar funcionário");
        }
    }

    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public Funcionario findById(int id) {
        Funcionario f = funcionarioRepository.findById(id);
        if (f == null) {
            throw new FuncionarioNotFoundException("Funcionário não encontrado.");
        }
        return f;
    }

    public void update(Funcionario funcionario) {
        boolean updated = funcionarioRepository.update(funcionario);
        if (!updated) {
            throw new RuntimeException("Erro ao atualizar funcionário");
        }
    }

    public void delete(int id) {
        boolean deleted = funcionarioRepository.delete(id);
        if (!deleted) {
            throw new RuntimeException("Erro ao deletar funcionário");
        }
    }
}
