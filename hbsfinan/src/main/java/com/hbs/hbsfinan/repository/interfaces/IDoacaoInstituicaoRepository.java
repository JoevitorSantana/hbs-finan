package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.dto.DoacaoInstituicaoCreateDTO;
import com.hbs.hbsfinan.model.DoacaoInstituicao;
import com.hbs.hbsfinan.repository.implementation.DoacaoInstituicaoRepository;

import java.util.List;

public interface IDoacaoInstituicaoRepository {



    void save(DoacaoInstituicaoCreateDTO dto);

    boolean delete(int id);



    void update(DoacaoInstituicao doacaoInstituicao);

    DoacaoInstituicao findById(int id);

    List<DoacaoInstituicao> findByCaixa(int idCaixa);

    List<DoacaoInstituicao> findAll();
}
