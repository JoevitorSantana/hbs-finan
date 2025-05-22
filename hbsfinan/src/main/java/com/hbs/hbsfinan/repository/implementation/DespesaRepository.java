package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.dto.DespesaCreateDTO;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.repository.interfaces.IDespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DespesaRepository implements IDespesaRepository {


    @Autowired
    private JdbcTemplate dbConn;

    private RowMapper<Despesa> rowMapper = (rs, rowNum) -> {
        Despesa despesa = new Despesa();
        despesa.setId(rs.getInt("id"));
        despesa.setDataLancamento(rs.getDate("dataLancamento").toLocalDate());
        despesa.setDataVencimento(rs.getDate("dataVencimento").toLocalDate());
        despesa.setDesc(rs.getString("Desc"));
        despesa.setPagamentoTotal(rs.getDouble("pagamentoTotal"));
        despesa.setValor(rs.getDouble("valor"));
        despesa.setDataQuitacao(rs.getDate("dataQuitacao").toLocalDate());

        return despesa;
    };

    private int id;
    private LocalDate dataLancamento;
    private LocalDate dataVencimento;
    private String Desc;
    private double pagamentoTotal; //valor pagou com multa ou desconto
    private double valor;
    private LocalDate dataQuitacao;


    @Override
    public void save(DespesaCreateDTO despesa) {
        dbConn.update("INSERT INTO despesa (dataLancamento,dataVencimento,Desc,pagamentoTotal,valor,dataQuitacao) VALUES (?,?,?,?,?,?)",despesa.getDataLancamento(),despesa.getDataVencimento(),despesa.getDesc(),despesa.getPagamentoTotal(),despesa.getValor(),despesa.getDataQuitacao());
    }

    @Override
    public Despesa findById(int id) {
        return dbConn.queryForObject("SELECT * FROM despesa WHERE id = ?", rowMapper, id);
    }

    @Override
    public List<Despesa> findAll() {
        return dbConn.query("SELECT * FROM despesa", rowMapper);
    }

    @Override
    public void delete(int id) {
        dbConn.update("DELETE FROM despesa WHERE id = ?", id);
    }

    @Override
    public void update(Despesa despesa) {
        dbConn.update("UPDATE despesa SET dataLancamento = ?, dataVencimento = ?,Desc = ?,pagamentoTotal = ?,valor = ?, dataQuitacao = ?  WHERE id = ?", despesa.getDataLancamento(), despesa.getDataVencimento(),despesa.getDesc(),despesa.getPagamentoTotal(),despesa.getValor(),despesa.getDataQuitacao(),despesa.getId());
    }
}
