package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Parametrizacao;
import com.hbs.hbsfinan.repository.interfaces.IParametrizacaoRepository;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // Pode ser útil se você for adicionar findAll futuramente
import java.util.List;

@Repository
public class ParametrizacaoRepository implements IParametrizacaoRepository {
    private Conexao dbConn;

    public ParametrizacaoRepository(Conexao dbConn) {
        this.dbConn = dbConn;
    }

    @Override
    public void save(Parametrizacao p) {
        String sql = "INSERT INTO parametrizacao (nome_empresa, razao_social, endereco_rua, endereco_bairro, endereco_cidade, endereco_cep, endereco_estado, email, telefone, celular, nome_proprietario, cnpj, logo_pequena_url, logo_grande_url) VALUES ('#1','#2','#3','#4','#5','#6','#7','#8','#9','#10','#11','#12','#13','#14')";
        sql = sql.replace("#1", p.getNomeEmpresa());
        sql = sql.replace("#2", p.getRazaoSocial());
        sql = sql.replace("#3", p.getEnderecoRua());
        sql = sql.replace("#4", p.getEnderecoBairro());
        sql = sql.replace("#5", p.getEnderecoCidade());
        sql = sql.replace("#6", p.getEnderecoCep());
        sql = sql.replace("#7", p.getEnderecoEstado());
        sql = sql.replace("#8", p.getEmail());
        sql = sql.replace("#9", p.getTelefone());
        sql = sql.replace("#10", p.getCelular());
        sql = sql.replace("#11", p.getNomeProprietario());
        sql = sql.replace("#12", p.getCnpj());
        sql = sql.replace("#13", p.getLogoPequenaUrl());
        sql = sql.replace("#14", p.getLogoGrandeUrl());
        dbConn.update(sql);
    }

//    @Override
//    public void update(Parametrizacao p) {
//        String sql = "UPDATE parametrizacao SET nome_empresa='#1', razao_social='#2', endereco_rua='#3', endereco_bairro='#4', endereco_cidade='#5', endereco_cep='#6', endereco_estado='#7', email='#8', telefone='#9', celular='#10', nome_proprietario='#11', cnpj='#12', logo_pequena_url='#13', logo_grande_url='#14' WHERE id=#15";
//        sql = sql.replace("#1", p.getNomeEmpresa());
//        sql = sql.replace("#2", p.getRazaoSocial());
//        sql = sql.replace("#3", p.getEnderecoRua());
//        sql = sql.replace("#4", p.getEnderecoBairro());
//        sql = sql.replace("#5", p.getEnderecoCidade());
//        sql = sql.replace("#6", p.getEnderecoCep());
//        sql = sql.replace("#7", p.getEnderecoEstado());
//        sql = sql.replace("#8", p.getEmail());
//        sql = sql.replace("#9", p.getTelefone());
//        sql = sql.replace("#10", p.getCelular());
//        sql = sql.replace("#11", p.getNomeProprietario());
//        sql = sql.replace("#12", p.getCnpj());
//        sql = sql.replace("#13", p.getLogoPequenaUrl());
//        sql = sql.replace("#14", p.getLogoGrandeUrl());
//        sql = sql.replace("#15", String.valueOf(p.getId()));
//        dbConn.update(sql);
//    }
    @Override
    public void update(Parametrizacao p) {
        String sql = "UPDATE parametrizacao SET " + "nome_empresa='#1', " + "razao_social='#2', " + "endereco_rua='#3', " + "endereco_bairro='#4', " + "endereco_cidade='#5', " + "endereco_cep='#6', " + "endereco_estado='#7', " + "email='#8', " + "telefone='#9', " + "celular='#10', " + "nome_proprietario='#11', " + "cnpj='#12', " + "logo_pequena_url='#13', " + "logo_grande_url='#14' " + "WHERE id=#15";
        // Substitua os placeholders e GARANTA que os valores de STRING estejam entre aspas simples
        sql = sql.replace("#1", p.getNomeEmpresa());
        sql = sql.replace("#2", p.getRazaoSocial());
        sql = sql.replace("#3", p.getEnderecoRua());
        sql = sql.replace("#4", p.getEnderecoBairro());
        sql = sql.replace("#5", p.getEnderecoCidade());
        sql = sql.replace("#6", p.getEnderecoCep());
        sql = sql.replace("#7", p.getEnderecoEstado());
        sql = sql.replace("#8", p.getEmail());
        sql = sql.replace("#9", p.getTelefone());
        sql = sql.replace("#10", p.getCelular());
        sql = sql.replace("#11", p.getNomeProprietario());
        sql = sql.replace("#12", p.getCnpj());
        sql = sql.replace("#13", p.getLogoPequenaUrl());
        sql = sql.replace("#14", p.getLogoGrandeUrl());
        sql = sql.replace("#15", String.valueOf(p.getId()));

        String updatedSql = "UPDATE parametrizacao SET " +
                "nome_empresa='" + p.getNomeEmpresa() + "', " +
                "razao_social='" + p.getRazaoSocial() + "', " +
                "endereco_rua='" + p.getEnderecoRua() + "', " +
                "endereco_bairro='" + p.getEnderecoBairro() + "', " +
                "endereco_cidade='" + p.getEnderecoCidade() + "', " +
                "endereco_cep='" + p.getEnderecoCep() + "', " +
                "endereco_estado='" + p.getEnderecoEstado() + "', " +
                "email='" + p.getEmail() + "', " +
                "telefone='" + p.getTelefone() + "', " +
                "celular='" + p.getCelular() + "', " +
                "nome_proprietario='" + p.getNomeProprietario() + "', " +
                "cnpj='" + p.getCnpj() + "', " +
                "logo_pequena_url='" + p.getLogoPequenaUrl() + "', " +
                "logo_grande_url='" + p.getLogoGrandeUrl() + "' " +
                "WHERE id=" + p.getId();
        dbConn.update(updatedSql);
    }

    @Override
    public Parametrizacao findFirst() {
        Parametrizacao parametrizacao = null;
        String sql = "SELECT * FROM parametrizacao ORDER BY id ASC LIMIT 1"; // Busca apenas o primeiro registro
        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) { // Se houver um resultado
                parametrizacao = mapResultSetToParametrizacao(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar parametrização: " + e.getMessage());
            e.printStackTrace();
        }
        return parametrizacao;
    }

    @Override
    public boolean exists() {
        String sql = "SELECT COUNT(*) FROM parametrizacao";
        try {
            ResultSet rs = dbConn.query(sql);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar existência de parametrização: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private Parametrizacao mapResultSetToParametrizacao(ResultSet rs) throws SQLException {
        Parametrizacao p = new Parametrizacao();
        p.setId(rs.getInt("id"));
        p.setNomeEmpresa(rs.getString("nome_empresa"));
        p.setRazaoSocial(rs.getString("razao_social"));
        p.setEnderecoRua(rs.getString("endereco_rua"));
        p.setEnderecoBairro(rs.getString("endereco_bairro"));
        p.setEnderecoCidade(rs.getString("endereco_cidade"));
        p.setEnderecoCep(rs.getString("endereco_cep"));
        p.setEnderecoEstado(rs.getString("endereco_estado"));
        p.setEmail(rs.getString("email"));
        p.setTelefone(rs.getString("telefone"));
        p.setCelular(rs.getString("celular"));
        p.setNomeProprietario(rs.getString("nome_proprietario"));
        p.setCnpj(rs.getString("cnpj"));
        p.setLogoPequenaUrl(rs.getString("logo_pequena_url"));
        p.setLogoGrandeUrl(rs.getString("logo_grande_url"));
        return p;
    }
}