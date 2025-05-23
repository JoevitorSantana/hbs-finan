package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.model.InscricaoEvento;
import com.hbs.hbsfinan.repository.interfaces.IInscricaoEventoRepository;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InscricaoEventoRepository implements IInscricaoEventoRepository {

    private final Conexao dbConn;

    public InscricaoEventoRepository(Conexao dbConn) {
        this.dbConn = dbConn;
    }

    @Override
    public void save(InscricaoEvento inscricaoEvento) {
        String sql = "INSERT INTO inscricao_evento (apoiador_id, evento_id) VALUES (#1, #2)";
        sql = sql.replace("#1", "" + inscricaoEvento.getApoiador().getId());
        sql = sql.replace("#2", "" + inscricaoEvento.getEvento().getId());
        dbConn.update(sql);
    }

    @Override
    public void update(InscricaoEvento inscricaoEvento) {
        String sql = "UPDATE inscricao_evento SET apoiador_id = #1, evento_id = #2 WHERE id = #3";
        sql = sql.replace("#1", "" + inscricaoEvento.getApoiador().getId());
        sql = sql.replace("#2", "" + inscricaoEvento.getEvento().getId());
        sql = sql.replace("#3", "" + inscricaoEvento.getId());
        dbConn.update(sql);
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM inscricao_evento WHERE id = #1";
        sql = sql.replace("#1", "" + id);
        return dbConn.update(sql);
    }

    @Override
    public InscricaoEvento findById(Long id) {
        InscricaoEvento inscricaoEvento = null;
        String sql = "SELECT * FROM inscricao_evento WHERE id = #1";
        sql = sql.replace("#1", "" + id);

        try {
            ResultSet rs = dbConn.query(sql);

            if (rs.next()) {
                inscricaoEvento = new InscricaoEvento();
                inscricaoEvento.setId(rs.getLong("id"));

                // Busca básica - você pode trocar por findById do próprio repository de Apoiador/Evento se preferir
                Apoiador apoiador = new Apoiador();
                apoiador.setId(rs.getLong("apoiador_id"));
                inscricaoEvento.setApoiador(apoiador);

                Evento evento = new Evento();
                evento.setId(rs.getInt("evento_id"));
                inscricaoEvento.setEvento(evento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inscricaoEvento;
    }

    @Override
    public List<InscricaoEvento> findAll() {
        List<InscricaoEvento> lista = new ArrayList<>();
        String sql = "SELECT * FROM inscricao_evento";
        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                InscricaoEvento inscricaoEvento = new InscricaoEvento();
                inscricaoEvento.setId(rs.getLong("id"));

                Apoiador apoiador = new Apoiador();
                apoiador.setId(rs.getLong("apoiador_id"));
                inscricaoEvento.setApoiador(apoiador);

                Evento evento = new Evento();
                evento.setId(rs.getInt("evento_id"));
                inscricaoEvento.setEvento(evento);

                lista.add(inscricaoEvento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
