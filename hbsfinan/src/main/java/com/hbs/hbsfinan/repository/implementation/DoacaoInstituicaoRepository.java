package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.dto.DoacaoInstituicaoCreateDTO;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.model.DoacaoInstituicao;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoInstituicaoRepository;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoacaoInstituicaoRepository implements IDoacaoInstituicaoRepository {

    private final Conexao dbConn = Conexao.getInstance();

    @Override
    public void save(DoacaoInstituicaoCreateDTO dto) {

        System.out.println("Recebido no back:");
        System.out.println("Nome: " + dto.getNome());
        System.out.println("CNPJ: " + dto.getCnpj());
        System.out.println("Data: " + dto.getData());
        System.out.println("Valor: " + dto.getValor());
        System.out.println("Caixa: " + dto.getIdCaixa());
        String dataFormatada = new SimpleDateFormat("yyyy-MM-dd").format(dto.getData());
        String sql = "INSERT INTO doacao_instituicao (nome, cnpj, data, valor, id_caixa) " +
                "VALUES ('#1', '#2', '#3', #4, #5)";
        sql = sql.replace("#1", dto.getNome())
                .replace("#2", dto.getCnpj())
                .replace("#3", dataFormatada)
                .replace("#4", String.valueOf(dto.getValor()))
                .replace("#5", String.valueOf(dto.getIdCaixa()));

        dbConn.update(sql);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM doacao_instituicao WHERE id = " + id;
        return dbConn.update(sql);
    }

    public void update(DoacaoInstituicao doacao) {
        String dataFormatada = new SimpleDateFormat("yyyy-MM-dd").format(doacao.getData());

        String nome = doacao.getNome() != null ? doacao.getNome() : "";
        String cnpj = doacao.getCnpj() != null ? doacao.getCnpj() : "";

        String sql = "UPDATE doacao_instituicao SET nome = '#1', cnpj = '#2', data = '#3', valor = #4, id_caixa = #5 WHERE id = #6";
        sql = sql.replace("#1", nome)
                .replace("#2", cnpj)
                .replace("#3", dataFormatada)
                .replace("#4", String.valueOf(doacao.getValor()))
                .replace("#5", String.valueOf(doacao.getCaixaId()))
                .replace("#6", String.valueOf(doacao.getId()));

        if (!dbConn.update(sql)) {
            throw new RuntimeException("Falha ao atualizar doação instituição");
        }
    }

    @Override
    public DoacaoInstituicao findById(int id) {
        String sql = "SELECT * FROM doacao_instituicao WHERE id = " + id;
        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                return montarDoacao(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DoacaoInstituicao> findByCaixa(int idCaixa) {
        List<DoacaoInstituicao> lista = new ArrayList<>();
        String sql = "SELECT * FROM doacao_instituicao WHERE id_caixa = " + idCaixa;
        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                lista.add(montarDoacao(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<DoacaoInstituicao> findAll() {
        List<DoacaoInstituicao> lista = new ArrayList<>();
        String sql = "SELECT * FROM doacao_instituicao";
        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                lista.add(montarDoacao(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private DoacaoInstituicao montarDoacao(ResultSet rs) throws Exception {
        DoacaoInstituicao doacao = new DoacaoInstituicao();
        doacao.setId(rs.getInt("id"));
        doacao.setNome(rs.getString("nome"));
        doacao.setCnpj(rs.getString("cnpj"));
        doacao.setData(rs.getDate("data"));
        doacao.setValor(rs.getLong("valor"));

        Caixa caixa = new Caixa();
        caixa.setId(rs.getInt("id_caixa"));
        doacao.setCaixa(caixa);

        return doacao;
    }
}