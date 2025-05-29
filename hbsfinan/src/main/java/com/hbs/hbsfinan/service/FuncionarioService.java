package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.FuncionarioCreateDTO;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.implementation.FuncionarioRepository;
import com.hbs.hbsfinan.exceptions.FuncionarioNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FuncionarioService {

    private FuncionarioRepository funcionarioRepository;
    private Conexao dbConnFactory;

    public FuncionarioService() {}

    public FuncionarioService(Conexao dbConnFactory) {
        this.dbConnFactory = dbConnFactory;
        this.funcionarioRepository = new FuncionarioRepository(dbConnFactory);
    }

    public void save(FuncionarioCreateDTO funcionarioDTO) {
        if (funcionarioRepository.findByCpf(funcionarioDTO.getCpf()) != null) {
            throw new RuntimeException("CPF já está cadastrado");
        }

        // Converter DTO para entidade
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setEmail(funcionarioDTO.getEmail());
        funcionario.setFone(funcionarioDTO.getFone());
        funcionario.setEndereco(funcionarioDTO.getEndereco());
        funcionario.setDataNascimento(funcionarioDTO.getDataNascimento());
        funcionario.setSexo(funcionarioDTO.getSexo());
        funcionario.setCpf(funcionarioDTO.getCpf());

        // Agora sim, salva a entidade no repositório
        funcionarioRepository.save(funcionario);
    }


    public void delete(int id) {
        funcionarioRepository.delete(id);
    }

    public void update(Funcionario funcionario) {
        funcionarioRepository.update(funcionario);
    }

    public Funcionario findById(int id) {
        Funcionario funcionario = funcionarioRepository.findById(id);

        if (funcionario == null) throw new FuncionarioNotFoundException("Funcionário não encontrado!");

        return funcionario;
    }

    public List<Funcionario> findAll() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        List<Funcionario> retorno = new ArrayList<>();

        for (Funcionario f : funcionarios)
            retorno.add(f);

        return retorno;
    }

    public Funcionario findByCpf(String cpf) {
        Funcionario funcionario = funcionarioRepository.findByCpf(cpf);

        if (funcionario == null) throw new FuncionarioNotFoundException("Funcionário com CPF não encontrado!");

        return funcionario;
    }
}
