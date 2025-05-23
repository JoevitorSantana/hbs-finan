package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.DoacaoAlimenticia;
import com.hbs.hbsfinan.repository.implementation.ApoiadorRepository;
import com.hbs.hbsfinan.repository.implementation.DoacaoAlimenticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoacaoAlimenticiaService {

    @Autowired
    DoacaoAlimenticiaRepository doacaoAlimenticiaRepository;

    public void save(DoacaoAlimenticia doacaoAlimenticia) {
        doacaoAlimenticiaRepository.save(doacaoAlimenticia);
    }

    public List<DoacaoAlimenticia> findAll() {
        return doacaoAlimenticiaRepository.findAll();
    }

    public void delete(int id) {
        doacaoAlimenticiaRepository.delete(id);
    }

    public void update(DoacaoAlimenticia doacaoAlimenticia) {
       doacaoAlimenticiaRepository.update(doacaoAlimenticia);
    }

    public DoacaoAlimenticia findById(int id) {
        return doacaoAlimenticiaRepository.findById(id);
    }
}
