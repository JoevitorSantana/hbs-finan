package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.dto.CaixaCreateDTO;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.repository.interfaces.ICaixaRepository;

import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CaixaRepository implements ICaixaRepository {

    private Conexao dbConn;

    public CaixaRepository(Conexao dbConn) {
        this.dbConn = dbConn;
    }

    @Override
    public void save(CaixaCreateDTO dto) {
        String sql = "INSERT INTO caixa (valor_inicial, dt_abertura_caixa, fun_id) " +
                "VALUES (#1, '#2', #3)";

        sql = sql.replace("#1", "" + dto.getValorInicial());
        sql = sql.replace("#2", dto.getDataAberturaCaixa().toString());
        sql = sql.replace("#3", "" + dto.getFunId());

        dbConn.update(sql);
    }

    @Override
    public void update(Caixa caixa) {
        String sql = "UPDATE caixa SET valor_inicial = #1, valor_final = #2, dt_abertura_caixa = '#3', dt_fechamento_caixa = '#4', fun_id = #5 WHERE id = #6";

        sql = sql.replace("#1", "" + caixa.getValorInicial());
        sql = sql.replace("#2", "" + caixa.getValorFinal());
        sql = sql.replace("#3", caixa.getDataAberturaCaixa().toString());
        sql = sql.replace("#4", caixa.getDataFechamentoCaixa() != null ? caixa.getDataFechamentoCaixa().toString() : "NULL");
        sql = sql.replace("#5", String.valueOf(caixa.getFuncionario().getId()));
        sql = sql.replace("#6", String.valueOf(caixa.getId()));

        dbConn.update(sql);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM caixa WHERE id = #1";
        sql = sql.replace("#1", String.valueOf(id));
        return dbConn.update(sql);
    }

    @Override
    public Caixa findById(int id) {
        Caixa caixa = null;
        String sql = "SELECT * FROM caixa WHERE id = #1";
        sql = sql.replace("#1", String.valueOf(id));

        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                caixa = mapResultSetToCaixa(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return caixa;
    }

    @Override
    public List<Caixa> findAll() {
        List<Caixa> caixas = new ArrayList<>();
        String sql = "SELECT * FROM caixa";

        try {
            ResultSet rs = dbConn.query(sql);
            while (rs.next()) {
                caixas.add(mapResultSetToCaixa(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return caixas;
    }

    private Caixa mapResultSetToCaixa(ResultSet rs) throws Exception {
        Caixa caixa = new Caixa();
        caixa.setId(rs.getInt("id"));
        caixa.setValorInicial(rs.getDouble("valor_inicial"));
        caixa.setValorFinal(rs.getDouble("valor_final"));

        caixa.setDataAberturaCaixa(rs.getTimestamp("dt_abertura_caixa").toLocalDateTime());

        if (rs.getTimestamp("dt_fechamento_caixa") != null) {
            caixa.setDataFechamentoCaixa(rs.getTimestamp("dt_fechamento_caixa").toLocalDateTime());
        }

        // Assumindo que apenas o ID do funcionário está disponível
        Funcionario funcionario = new Funcionario();
        funcionario.setId(rs.getInt("fun_id"));
        caixa.setFuncionario(funcionario);

        return caixa;
    }
}
