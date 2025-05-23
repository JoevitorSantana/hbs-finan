package com.hbs.hbsfinan.repository.implementation;

import com.hbs.hbsfinan.dto.ApoiadorDTO;
import com.hbs.hbsfinan.model.Apoiador;
import com.hbs.hbsfinan.model.DoacaoAlimenticia;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoAlimenticia;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DoacaoAlimenticiaRepository extends IDoacaoAlimenticia {


    private RowMapper<DoacaoAlimenticia> rowMapper = (rs, rowNum) -> {
        DoacaoAlimenticia doacaoAlimenticia = new DoacaoAlimenticia();
        doacaoAlimenticia.setId_doacao(rs.getLong("Id_doacao"));
        doacaoAlimenticia.setData_Doacao(rs.getDate("Data_Doacao"));
        return doacaoAlimenticia;
    };



    @Override
    public void save(DoacaoAlimenticia doacaoAlimenticia) {
        dbConn.update("INSERT INTO apoiador (id, nome, endereco, sexo, telefone, cpf, email , data_nascimento) VALUES (?,?,?,?,?,?,?,?)", apoiadorDTO.getId(), apoiadorDTO.getNome(), apoiadorDTO.getEndereco(),apoiadorDTO.getSexo(), apoiadorDTO.getFone(), apoiadorDTO.getCpf(),apoiadorDTO.getEmail(),apoiadorDTO.getDataNasc());
    }

    @Override
    public DoacaoAlimenticia findById(int id) {
        return dbConn.queryForObject("SELECT * FROM apoiador WHERE id = ?", rowMapper, id);
    }

    @Override
    public List<DoacaoAlimenticia> findAll() {
        return dbConn.query("SELECT * FROM apoiador", rowMapper);
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(DoacaoAlimenticia doacaoAlimenticia) {
        dbConn.update(
                "UPDATE apoiador SET Data_Doacao = ? WHERE Id_doacao = ?",
                doacaoAlimenticia.getId_doacao(),
                doacaoAlimenticia.getData_Doacao()
        );
    }


}
