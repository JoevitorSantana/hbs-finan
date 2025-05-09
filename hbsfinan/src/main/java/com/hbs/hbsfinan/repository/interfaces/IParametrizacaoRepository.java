package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.model.Parametrizacao;
import java.util.Optional;

public interface IParametrizacaoRepository {
    void save(Parametrizacao p); //Persiste uma nova parametrização na base.
    void update(Parametrizacao p); //Atualiza a parametrização existente.
    Optional<Parametrizacao> findFirst(); //Retorna o primeiro registro de parametrização ou vazio se não existir.
    boolean exists(); //Verifica se já existe algum registro de parametrização.
}
