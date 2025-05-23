package com.hbs.hbsfinan.repository.implementation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.hbs.hbsfinan.model.DoacaoAlimenticia;
import com.hbs.hbsfinan.repository.interfaces.IDoacaoAlimenticia;


import java.util.List;

@Repository
public class DoacaoAlimenticiaRepository implements IDoacaoAlimenticia {
    @Autowired
    private JdbcTemplate dbConn;

    private RowMapper<DoacaoAlimenticia> rowMapper = (rs, rowNum) -> {
        DoacaoAlimenticia doacaoAlimenticia = new DoacaoAlimenticia();
        doacaoAlimenticia.setId_doacao(rs.getLong("Id_doacao"));
        doacaoAlimenticia.setData_Doacao(rs.getDate("Data_Doacao"));
        return doacaoAlimenticia;
    };



    @Override
    public void save(DoacaoAlimenticia doacaoAlimenticia) {
        dbConn.update("INSERT INTO apoiador (Id_doacao,Data_Doacao) VALUES (?,?)", doacaoAlimenticia.getId_doacao(), doacaoAlimenticia.getData_Doacao());
    }

    @Override
    public DoacaoAlimenticia findById(int id) {
        return dbConn.queryForObject("SELECT * FROM doacaoAlimenticia WHERE Id_doacao = ?", rowMapper, id);
    }

    @Override
    public List<DoacaoAlimenticia> findAll() {
        return dbConn.query("SELECT * FROM doacaoAlimenticia", rowMapper);
    }

    @Override
    public void delete(int id) {
        dbConn.update("DELETE FROM doacaoAlimenticia WHERE Id_doacao = ?", id);
    }

    @Override
    public void update(DoacaoAlimenticia doacaoAlimenticia) {
        dbConn.update(
                "UPDATE doacaoAlimenticia SET Data_Doacao = ? WHERE Id_doacao = ?",
                doacaoAlimenticia.getId_doacao(),
                doacaoAlimenticia.getData_Doacao()
        );
    }


}
