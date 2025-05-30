package com.hbs.hbsfinan.repository.implementation;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.infra.db.SingletonDB;
import com.hbs.hbsfinan.model.Evento;
import com.hbs.hbsfinan.model.Anotacoes;
import com.hbs.hbsfinan.repository.interfaces.IAnotacoesRepository;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AnotacoesRepository implements IAnotacoesRepository
{
    private Conexao dbConn;

    public AnotacoesRepository(Conexao dbConn) {this.dbConn = dbConn;}

    @Override
    public void save(Anotacoes anotacoes) {
        String sql = "INSERT INTO anotacoes(anotacao,data,even_id) VALUES ('#1', '#2', #3)";
        sql = sql.replace("#1", anotacoes.getAnotacao());
        sql = sql.replace("#2", anotacoes.getData().toString());
        sql = sql.replace("#3",""+anotacoes.getEvento().getId());

        dbConn.update(sql);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM anotacoes WHERE id = #1";
        sql = sql.replace("#1", "" + id);
        return dbConn.update(sql);
    }

    @Override
    public void update(Anotacoes anotacoes) {
        String sql = "UPDATE anotacoes SET anotacao = '#1', data = '#2' WHERE id = #3 ";
        sql = sql.replace("#1",anotacoes.getAnotacao());
        sql = sql.replace("#2",anotacoes.getData().toString());
        sql = sql.replace("#3",""+anotacoes.getId());

        dbConn.update(sql);
    }

    @Override
    public Anotacoes findById(int id) {
        Anotacoes anotacoes = null;
        String sql = "SELECT * FROM anotacoes WHERE id = #1";
        sql = sql.replace("#1", "" + id);
        try
        {
            ResultSet rs = dbConn.query(sql);
            if(rs.next())
            {
                anotacoes = new Anotacoes();
                anotacoes.setId(rs.getInt("id"));
                anotacoes.setAnotacao(rs.getString("anotacao"));
                anotacoes.setData(rs.getDate("data"));
                Evento evento = new Evento();
                evento.setId(rs.getInt("even_id"));
                anotacoes.setEvento(evento);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return anotacoes;
    }

    @Override
    public List<Anotacoes> findAll() {
        List<Anotacoes> anotacoes = new ArrayList<>();
        String sql = "SELECT * FROM anotacoes";
        try
        {
            ResultSet rs = dbConn.query(sql);
            while(rs.next())
            {
                Anotacoes anota = new Anotacoes();
                anota.setId(rs.getInt("id"));
                anota.setAnotacao(rs.getString("anotacao"));
                anota.setData(rs.getDate("data"));
                Evento evento = new Evento();
                evento.setId(rs.getInt("even_id"));
                anota.setEvento(evento);

                anotacoes.add(anota);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return anotacoes;
    }
}
