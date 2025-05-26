package com.hbs.hbsfinan.infra.db;

public class SingletonDB {
    private static Conexao conexao=null;

    //private static final String CONNECTION_URL = "jdbc:postgresql://ep-dawn-band-a5ez9679-pooler.us-east-2.aws.neon.tech:5432/";
    private static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/";
    // private static final String CONNECTION_DATABASE = "neondb?sslmode=require";
    private static final String CONNECTION_DATABASE = "hbsdb";
    //private static final String CONNECTION_USERNAME = "neondb_owner";
    private static final String CONNECTION_USERNAME = "postgres";
    //private static final String CONNECTION_PASSWORD = "npg_tmjQ74ZJDHaW";
    private static final String CONNECTION_PASSWORD = "postgres123";

    private SingletonDB() { }

    public static boolean conectar()
    {
        //conexao = new Conexao();
        //return conexao.conectar(CONNECTION_URL, CONNECTION_DATABASE, CONNECTION_USERNAME,CONNECTION_PASSWORD);
        return false;
    }
    public static Conexao getConexao() {
        return conexao;
    }
}
