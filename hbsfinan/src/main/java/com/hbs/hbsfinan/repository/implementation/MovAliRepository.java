//package com.hbs.hbsfinan.repository.implementation;
//
//import com.hbs.hbsfinan.model.DoacaoAlimenticia;
//import com.hbs.hbsfinan.model.Funcionario;
//import com.hbs.hbsfinan.model.MovimentacaoAli;
//import com.hbs.hbsfinan.repository.interfaces.IMovAlimentacao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class MovAliRepository implements IMovAlimentacao {
//
//    @Autowired
//    private JdbcTemplate dbConn;
//
//    private RowMapper<DoacaoAlimenticia> rowMapper = (rs, rowNum) -> {
//        MovimentacaoAli movimentacaoAli = new MovimentacaoAli();
//        movimentacaoAli.setData_Doacao(rs.getDate("Data_Doacao"));
//        Funcionario funcionario = new Funcionario();
//        funcionario.setId(rs.getInt("id"));
//        doacaoAlimenticia.setFuncionario(funcionario);
//        return doacaoAlimenticia;
//    };
//
//
//}
