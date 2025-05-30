package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Caixa;
import com.hbs.hbsfinan.model.Despesa;
import com.hbs.hbsfinan.exceptions.DespesaNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DespesaRepository {

    private final Conexao conexao;

    private CaixaRepository caixaRepository;
    public DespesaRepository(Conexao conexao) {
        this.conexao = conexao;
    }

    public Despesa save(Despesa despesa) {
        String sql = "INSERT INTO despesa (data_lancamento, data_vencimento, descricao, valor, caixa_id, pagamento_total) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(despesa.getDataLancamento()));
            ps.setDate(2, Date.valueOf(despesa.getDataVencimento()));
            ps.setString(3, despesa.getDescricao());
            ps.setDouble(4, despesa.getValor());
            ps.setInt(5, despesa.getCaixa().getId());
            ps.setDouble(6, despesa.getPagamentoTotal());


            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir despesa, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    despesa.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter ID da despesa inserida.");
                }
            }

            return despesa;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar despesa: " + e.getMessage(), e);
        }
    }

    public Optional<Despesa> findById(int id) {
        String sql = "SELECT * FROM despesa WHERE id = ?";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Despesa despesa = mapToDespesa(rs);
                return Optional.of(despesa);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar despesa: " + e.getMessage(), e);
        }
    }

    public List<Despesa> findAll() {
        String sql = "SELECT * FROM despesa";
        List<Despesa> lista = new ArrayList<>();
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapToDespesa(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar despesas: " + e.getMessage(), e);
        }
    }

    public Despesa update(Despesa despesa) {
        String sql = "UPDATE despesa SET data_lancamento = ?, data_vencimento = ?, descricao = ?, valor = ?, data_quitacao = ? WHERE id = ?";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(despesa.getDataLancamento()));
            ps.setDate(2, Date.valueOf(despesa.getDataVencimento()));
            ps.setString(3, despesa.getDescricao());
            ps.setDouble(4, despesa.getValor());
            if (despesa.getDataQuitacao() != null) {
                ps.setDate(5, Date.valueOf(despesa.getDataQuitacao()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.setInt(6, despesa.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DespesaNotFoundException("Despesa não encontrada para atualização.");
            }
            return despesa;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar despesa: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM despesa WHERE id = ?";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DespesaNotFoundException("Despesa não encontrada para exclusão.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar despesa: " + e.getMessage(), e);
        }
    }

    private Despesa mapToDespesa(ResultSet rs) throws SQLException {
        Despesa despesa = new Despesa();
        despesa.setId(rs.getInt("id"));

        Date dataLanc = rs.getDate("data_lancamento");
        if (dataLanc != null) despesa.setDataLancamento(dataLanc.toLocalDate());

        Date dataVenc = rs.getDate("data_vencimento");
        if (dataVenc != null) despesa.setDataVencimento(dataVenc.toLocalDate());

        Date dataQuit = rs.getDate("data_quitacao");
        if (dataQuit != null) despesa.setDataQuitacao(dataQuit.toLocalDate());

        despesa.setDescricao(rs.getString("descricao"));
        despesa.setValor(rs.getDouble("valor"));
        despesa.setPagamentoTotal(rs.getDouble("pagamento_total"));

        try {
            int idCaixa = rs.getInt("id");
            if (!rs.wasNull()) {
                Caixa caixa = caixaRepository.findById(idCaixa);
                if (caixa != null) {
                    despesa.setCaixa(caixa);
                } else {
                    System.out.println("Caixa não encontrado para id: " + idCaixa);
                    // Por enquanto, seta um Caixa dummy só pra teste
                    Caixa caixaDummy = new Caixa();
                    caixaDummy.setId(idCaixa);
                    caixaDummy.setValorInicial(0);
                    caixaDummy.setValorFinal(0);
                    despesa.setCaixa(caixaDummy);
                }
            }

        } catch (Exception e) {
            System.err.println("⚠️ Erro ao buscar caixa: " + e.getMessage());
        }

        return despesa;
    }

}
