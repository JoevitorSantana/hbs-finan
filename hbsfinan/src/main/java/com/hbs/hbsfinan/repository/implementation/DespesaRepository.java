package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.repository.interfaces.IDespesaRepository;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DespesaRepository implements IDespesaRepository {

    private Conexao dbConn;

    public DespesaRepository(Conexao dbConn) {
        this.dbConn = dbConn;
    }

    @Override
    public void save(DespesaCreateDTO despesa) {
        String sql = "INSERT INTO despesa (dataLancamento, dataVencimento, Desc, valor, dataQuitacao, pagamentoTotal) " +
                "VALUES ('#1', '#2', '#3', #4, NULL, NULL)";
        sql = sql.replace("#1", despesa.getDataLancamento().toString());
        sql = sql.replace("#2", despesa.getDataVencimento().toString());
        sql = sql.replace("#3", despesa.getDesc());
        sql = sql.replace("#4", String.valueOf(despesa.getValor()));
        dbConn.update(sql);
    }

    public Despesa findByUniqueFields(DespesaCreateDTO dto, int idCaixa) {
        Despesa despesa = null;
        String sql = "SELECT * FROM despesa WHERE dataLancamento = '#1' AND dataVencimento = '#2' " +
                "AND Desc = '#3' AND valor = #4 AND id_caixa = #5 ORDER BY id DESC LIMIT 1";

        sql = sql.replace("#1", dto.getDataLancamento().toString());
        sql = sql.replace("#2", dto.getDataVencimento().toString());
        sql = sql.replace("#3", dto.getDesc());
        sql = sql.replace("#4", String.valueOf(dto.getValor()));
        sql = sql.replace("#5", String.valueOf(idCaixa));

        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                despesa = extractDespesaFromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return despesa;
    }


    @Override
    public Despesa findById(int id) {
        Despesa despesa = null;
        String sql = "SELECT * FROM despesa WHERE id = " + id;

        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                despesa = extractDespesaFromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return despesa;
    }

    @Override
    public List<Despesa> findAll() {
        List<Despesa> list = new ArrayList<>();
        String sql = "SELECT * FROM despesa";

        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                list.add(extractDespesaFromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM despesa WHERE id = " + id;
        dbConn.update(sql);
    }

    @Override
    public void update(Despesa despesa) {
        String sql = "UPDATE despesa SET dataLancamento = '#1', dataVencimento = '#2', Desc = '#3', " +
                "pagamentoTotal = #4, valor = #5, dataQuitacao = #6 WHERE id = " + despesa.getId();

        sql = sql.replace("#1", despesa.getDataLancamento().toString());
        sql = sql.replace("#2", despesa.getDataVencimento().toString());
        sql = sql.replace("#3", despesa.getDesc());
        sql = sql.replace("#4", String.valueOf(despesa.getPagamentoTotal()));
        sql = sql.replace("#5", String.valueOf(despesa.getValor()));

        if (despesa.getDataQuitacao() != null) {
            sql = sql.replace("#6", "'" + despesa.getDataQuitacao().toString() + "'");
        } else {
            sql = sql.replace("#6", "NULL");
        }

        dbConn.update(sql);
    }

    private Despesa extractDespesaFromResultSet(ResultSet rs) throws Exception {
        Despesa despesa = new Despesa();
        despesa.setId(rs.getInt("id"));
        despesa.setDataLancamento(rs.getDate("dataLancamento").toLocalDate());
        despesa.setDataVencimento(rs.getDate("dataVencimento").toLocalDate());
        despesa.setDesc(rs.getString("Desc"));
        despesa.setValor(rs.getDouble("valor"));

        java.sql.Date quitacaoDate = rs.getDate("dataQuitacao");
        despesa.setDataQuitacao(quitacaoDate != null ? quitacaoDate.toLocalDate() : null);

        despesa.setPagamentoTotal(rs.getDouble("pagamentoTotal"));

        return despesa;
    }
}
