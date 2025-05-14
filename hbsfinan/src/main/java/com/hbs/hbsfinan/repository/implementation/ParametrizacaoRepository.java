package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Parametrizacao;
import com.hbs.hbsfinan.repository.interfaces.IParametrizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ParametrizacaoRepository implements IParametrizacaoRepository {

    @Autowired
    private JdbcTemplate dbConn;

    //RowMapper define como mapear cada linha do ResultSet para um objeto Parametrizacao.
    private final RowMapper<Parametrizacao> rowMapper = new RowMapper<Parametrizacao>() {
        @Override
        public Parametrizacao mapRow(ResultSet rs, int rowNum) throws SQLException {
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
    };

    @Override
    public void save(Parametrizacao p) { //Insere um novo registro na tabela parametrizacao. Executa um INSERT puro em SQL.
        dbConn.update(
                "INSERT INTO parametrizacao (nome_empresa, razao_social, endereco_rua, endereco_bairro, endereco_cidade, endereco_cep, endereco_estado, email, telefone, celular, nome_proprietario, cnpj, logo_pequena_url, logo_grande_url) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                p.getNomeEmpresa(), p.getRazaoSocial(), p.getEnderecoRua(), p.getEnderecoBairro(),
                p.getEnderecoCidade(), p.getEnderecoCep(), p.getEnderecoEstado(),
                p.getEmail(), p.getTelefone(), p.getCelular(), p.getNomeProprietario(),
                p.getCnpj(), p.getLogoPequenaUrl(), p.getLogoGrandeUrl());
    }

    @Override
    public void update(Parametrizacao p) { //Atualiza o único registro da tabela parametrizacao. Executa um UPDATE ... WHERE id = ?, alterando somente o registro cujo id bate com p.getId().
        dbConn.update(
                "UPDATE parametrizacao SET nome_empresa=?, razao_social=?, endereco_rua=?, endereco_bairro=?, endereco_cidade=?, endereco_cep=?, endereco_estado=?, email=?, telefone=?, celular=?, nome_proprietario=?, cnpj=?, logo_pequena_url=?, logo_grande_url=? WHERE id=?",
                p.getNomeEmpresa(), p.getRazaoSocial(), p.getEnderecoRua(), p.getEnderecoBairro(),
                p.getEnderecoCidade(), p.getEnderecoCep(), p.getEnderecoEstado(),
                p.getEmail(), p.getTelefone(), p.getCelular(), p.getNomeProprietario(),
                p.getCnpj(), p.getLogoPequenaUrl(), p.getLogoGrandeUrl(), p.getId());
    }

    @Override
    public Parametrizacao findFirst() {
        try {
            // Versão mais compatível (usando JdbcTemplate corretamente)
            return dbConn.queryForObject("SELECT * FROM parametrizacao", rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean exists() { //Verifica se já existe algum registro na tabela parametrizacao. Executa SELECT COUNT(*) para saber quantas linhas existem na tabela.
        Integer count = dbConn.queryForObject(
                "SELECT COUNT(*) FROM parametrizacao", Integer.class);
        return count != null && count > 0;
    }
}
