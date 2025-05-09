package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.model.Parametrizacao;
import com.hbs.hbsfinan.repository.interfaces.IParametrizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
            p.setNomeEmpresa(rs.getString("nomeEmpresa"));
            p.setRazaoSocial(rs.getString("razaoSocial"));
            p.setEnderecoRua(rs.getString("enderecoRua"));
            p.setEnderecoBairro(rs.getString("enderecoBairro"));
            p.setEnderecoCidade(rs.getString("enderecoCidade"));
            p.setEnderecoCep(rs.getString("enderecoCep"));
            p.setEnderecoEstado(rs.getString("enderecoEstado"));
            p.setEmail(rs.getString("email"));
            p.setTelefone(rs.getString("telefone"));
            p.setCelular(rs.getString("celular"));
            p.setNomeProprietario(rs.getString("nomeProprietario"));
            p.setCnpj(rs.getString("cnpj"));
            p.setLogoPequenaUrl(rs.getString("logoPequenaUrl"));
            p.setLogoGrandeUrl(rs.getString("logoGrandeUrl"));
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
    public Optional<Parametrizacao> findFirst() { // Recupera o primeiro (único) registro da tabela, se existir. Tenta buscar o primeiro registro da tabela, limitando a 1 linha (LIMIT 1).
        try {
            Parametrizacao p = dbConn.queryForObject(
                    "SELECT * FROM parametrizacao LIMIT 1", rowMapper);
            return Optional.ofNullable(p);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean exists() { //Verifica se já existe algum registro na tabela parametrizacao. Executa SELECT COUNT(*) para saber quantas linhas existem na tabela.
        Integer count = dbConn.queryForObject(
                "SELECT COUNT(*) FROM parametrizacao", Integer.class);
        return count != null && count > 0;
    }
}
