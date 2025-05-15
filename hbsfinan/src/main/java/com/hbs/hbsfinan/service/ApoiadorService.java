package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.implementation.ApoiadorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApoiadorService {


    @Autowired
    private ApoiadorRepository apoiadorRepository;
 //service
    public void save(ApoiadorDTO dto) {
        Apoiador apoiador = new Apoiador(
                dto.getNome(),
                dto.getEmail(),
                dto.getFone(),
                dto.getEndereco(),
                dto.getSexo(),
                dto.getCpf(),
                dto.getDataNasc(),
                dto.getId()
        );
        apoiadorRepository.save(apoiador);  // Aqui passa a entidade
    }


    public List<Apoiador> findAll() {
        return apoiadorRepository.findAll();
    }

    public void delete(int id) {
        apoiadorRepository.delete(id);
    }

    public void update(Apoiador apoiador) {
        apoiadorRepository.update(apoiador);
    }

    public Apoiador findById(int id) {
        return apoiadorRepository.findById(id);
    }

}
