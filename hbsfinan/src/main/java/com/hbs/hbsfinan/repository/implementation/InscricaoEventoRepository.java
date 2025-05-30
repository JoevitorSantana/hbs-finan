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

    public InscricaoEventoRepository() {
        this.dbConn = Conexao.getInstance();
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
        String sql = "SELECT i.id AS inscricao_id, " +
                "a.id AS apoiador_id, a.nome AS apoiador_nome, a.email AS apoiador_email, a.cpf AS apoiador_cpf, a.fone AS apoiador_fone, a.endereco AS apoiador_endereco, a.data_nasc AS apoiador_data_nasc, a.sexo AS apoiador_sexo, " +
                "e.id AS evento_id, e.nome AS evento_nome, e.local AS evento_local, e.data AS evento_data, e.descricao AS evento_descricao, e.materiais AS evento_materiais " +
                "FROM inscricao_evento i " +
                "JOIN apoiador a ON a.id = i.apoiador_id " +
                "JOIN evento e ON e.id = i.evento_id " +
                "WHERE i.id = " + id;
        try {
            ResultSet rs = dbConn.query(sql);

            if (rs.next()) {
                inscricaoEvento = new InscricaoEvento();
                inscricaoEvento.setId(rs.getLong("inscricao_id"));

                Apoiador apoiador = new Apoiador();
                apoiador.setId(rs.getLong("apoiador_id"));
                apoiador.setNome(rs.getString("apoiador_nome"));
                apoiador.setEmail(rs.getString("apoiador_email"));
                apoiador.setCpf(rs.getString("apoiador_cpf"));
                apoiador.setFone(rs.getString("apoiador_fone"));
                apoiador.setEndereco(rs.getString("apoiador_endereco"));
                apoiador.setDataNasc(rs.getDate("apoiador_data_nasc").toLocalDate());
                apoiador.setSexo(rs.getString("apoiador_sexo"));
                inscricaoEvento.setApoiador(apoiador);

                Evento evento = new Evento();
                evento.setId(rs.getInt("evento_id"));
                evento.setNome(rs.getString("evento_nome"));
                evento.setLocal(rs.getString("evento_local"));
                evento.setData(rs.getDate("evento_data"));
                evento.setDescricao(rs.getString("evento_descricao"));
                evento.setMateriais(rs.getString("evento_materiais"));
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
        String sql = "SELECT i.id AS inscricao_id, " +
                "a.id AS apoiador_id, a.nome AS apoiador_nome, a.email AS apoiador_email, a.cpf AS apoiador_cpf, a.fone AS apoiador_fone, a.endereco AS apoiador_endereco, a.data_nasc AS apoiador_data_nasc, a.sexo AS apoiador_sexo, " +
                "e.id AS evento_id, e.nome AS evento_nome, e.local AS evento_local, e.data AS evento_data, e.descricao AS evento_descricao, e.materiais AS evento_materiais " +
                "FROM inscricao_evento i " +
                "JOIN apoiador a ON a.id = i.apoiador_id " +
                "JOIN evento e ON e.id = i.evento_id";
        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                InscricaoEvento inscricaoEvento = new InscricaoEvento();
                inscricaoEvento.setId(rs.getLong("inscricao_id"));

                Apoiador apoiador = new Apoiador();
                apoiador.setId(rs.getLong("apoiador_id"));
                apoiador.setNome(rs.getString("apoiador_nome"));
                apoiador.setEmail(rs.getString("apoiador_email"));
                apoiador.setCpf(rs.getString("apoiador_cpf"));
                apoiador.setFone(rs.getString("apoiador_fone"));
                apoiador.setEndereco(rs.getString("apoiador_endereco"));
                apoiador.setDataNasc(rs.getDate("apoiador_data_nasc").toLocalDate());
                apoiador.setSexo(rs.getString("apoiador_sexo"));
                inscricaoEvento.setApoiador(apoiador);

                Evento evento = new Evento();
                evento.setId(rs.getInt("evento_id"));
                evento.setNome(rs.getString("evento_nome"));
                evento.setLocal(rs.getString("evento_local"));
                evento.setData(rs.getDate("evento_data"));
                evento.setDescricao(rs.getString("evento_descricao"));
                evento.setMateriais(rs.getString("evento_materiais"));
                inscricaoEvento.setEvento(evento);

                lista.add(inscricaoEvento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
