package com.hbs.hbsfinan.repository.interfaces;

import com.hbs.hbsfinan.model.Parametrizacao;
import java.util.Optional;

public interface IParametrizacaoRepository {
    void save(Parametrizacao p);
    void update(Parametrizacao p);
    Parametrizacao findFirst();
    boolean exists();
}
