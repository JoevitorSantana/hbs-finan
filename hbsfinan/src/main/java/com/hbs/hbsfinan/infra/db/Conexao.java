package com.hbs.hbsfinan.infra.db;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class Conexao
{
    private static Conexao instance = null;
    private Connection connect = null;
    private String erro = "";

    private Conexao() { }

    private boolean conectar()
    {
        boolean conectado = false;
        try {
            final String host = "jdbc:postgresql://localhost:5432/";
            // private static final String CONNECTION_DATABASE = "neondb?sslmode=require";
            final String banco = "hbsdb";
            //private static final String CONNECTION_USERNAME = "neondb_owner";
            final String usuario = "postgres";
            //private static final String CONNECTION_PASSWORD = "npg_tmjQ74ZJDHaW";
            final String senha = "postgres";

            String url = host+banco;
            connect = DriverManager.getConnection(url,usuario,senha);
            conectado=true;
        }
        catch ( SQLException sqlex ) {
            erro="Impossivel conectar com a base de dados: " + sqlex.toString();
        } catch ( Exception ex ) {
            erro="Outro erro: " + ex.toString();
        }
        return conectado;
    }

    public Connection getConnection()
    {
        return connect;
    }

    public static Conexao getInstance() {
        try {
            if (instance == null || instance.getConnection().isClosed()) {
                instance = new Conexao();
                instance.conectar();
            }
            return instance;
        } catch (SQLException sqlex) {
            sqlex.fillInStackTrace();
        }
        return null;
    }

    public String getMensagemErro()
    {
        return erro;
    }

    public boolean getEstadoConexao()
    {
        return (connect!=null);
    }

    public boolean update(String sql) // inserir, alterar,excluir
    {
        boolean executou=false;
        try {
            Statement statement = connect.createStatement();
            int result = statement.executeUpdate(sql);
            statement.close();
            if(result >= 1)
                executou = true;
        }
        catch ( SQLException sqlex )
        {  erro = "Erro: " + sqlex.toString(); }
        return executou;
    }

    public ResultSet query(String sql)
    {
        ResultSet rs = null;
        try {
            Statement statement = connect.createStatement();
            rs = statement.executeQuery(sql);
        }
        catch ( SQLException sqlex )
        {
            erro= "Erro: " + sqlex.toString();
            rs = null;
        }
        return rs;
    }
}
