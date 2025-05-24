package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.exceptions.DoacaoMonetariaNotFoundException;
import com.hbs.hbsfinan.exceptions.EventoNotFoundException;
import com.hbs.hbsfinan.model.DoacaoMonetaria;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.repository.implementation.DoacaoMonetariaRepository;
import com.hbs.hbsfinan.repository.implementation.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoacaoMonetariaService {

    private DoacaoMonetariaRepository doacaoMonetariaRepository;

    public void save(DoacaoMonetaria doacaoMonetaria) {doacaoMonetariaRepository.save(doacaoMonetaria);};

    public void delete(int id) {doacaoMonetariaRepository.delete(id);}

    public void update(DoacaoMonetaria doacaoMonetaria) {doacaoMonetariaRepository.update(doacaoMonetaria);}

    public DoacaoMonetaria findById(int id) {
        DoacaoMonetaria doacaoMonetaria = doacaoMonetariaRepository.findById(id);

        if (doacaoMonetaria == null) throw new DoacaoMonetariaNotFoundException("Doacao não encontrada!");

        return doacaoMonetaria;
    }

    public List<DoacaoMonetaria> findAll() {
        List<DoacaoMonetaria> doacaoMonetarias = doacaoMonetariaRepository.findAll();
        List<DoacaoMonetaria> doacaoMonetariasRetorno = new ArrayList<>();
        for (DoacaoMonetaria doacaoMonetaria : doacaoMonetarias)
            doacaoMonetariasRetorno.add(doacaoMonetaria);
        return doacaoMonetariasRetorno;
    }

    public DoacaoMonetaria findByApoiador(String apoiador) {
        DoacaoMonetaria doacaoMonetaria = doacaoMonetariaRepository.findByApoiador(apoiador);

        if (doacaoMonetaria == null) throw new EventoNotFoundException("Doacao não encontrada!");

        return doacaoMonetaria;
    }


}
