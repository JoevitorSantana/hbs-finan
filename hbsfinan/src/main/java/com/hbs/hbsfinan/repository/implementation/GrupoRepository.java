package com.hbs.hbsfinan.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GrupoRepository {

    @Autowired
    private JdbcTemplate dbConn;
    //continuar
}
