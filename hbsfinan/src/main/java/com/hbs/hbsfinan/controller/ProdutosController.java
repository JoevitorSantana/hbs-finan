package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.dto.UsuarioEditResponseDTO;
import com.hbs.hbsfinan.dto.UsuarioResponseDTO;
import com.hbs.hbsfinan.exceptions.UsuarioNotFoundException;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.model.Funcionario;
import com.hbs.hbsfinan.model.Produtos;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.service.EventoService;
import com.hbs.hbsfinan.service.ProdutosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/produtos")
public class ProdutosController {


    ProdutosService produtosService;
    private Conexao dbConnFactory;

    public ProdutosController() {
        this.dbConnFactory = Conexao.getInstance();
        this.produtosService = new ProdutosService(dbConnFactory);
    }

    @PostMapping("/novo")
    public ResponseEntity save(@Valid @RequestBody Produtos produtos){
        try
        {
            produtosService.save(produtos);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Produto inserudo com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            System.err.println("Erro ao salvar produto: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Produtos>> findAll() {
        try {
            return ResponseEntity.ok(produtosService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        } return ResponseEntity.badRequest().build();
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody Produtos produtos){
        try{
            Produtos oldProdutos = produtosService.findById(id);

            if(produtos.getNome() != null && !produtos.getNome().equals(oldProdutos.getNome()))
                oldProdutos.setNome(produtos.getNome());

            if(produtos.getQtd() != 0 && produtos.getQtd() != oldProdutos.getQtd())
                oldProdutos.setQtd(produtos.getQtd());

            // Atualiza data validade, mesmo para null (permite remover)
            if (produtos.getDataValidade() != null || (produtos.getDataValidade() == null && oldProdutos.getDataValidade() != null)) {
                oldProdutos.setDataValidade(produtos.getDataValidade());
            }

            produtosService.update(oldProdutos);

            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Produto atualizado com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Produtos> findById(@PathVariable int id) {
        Produtos produtos = produtosService.findById(id);
        return ResponseEntity.ok(produtos);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {

        if (produtosService.findById(id) != null) {
            produtosService.delete(id);
            return ResponseEntity.ok("Deletado com sucesso!");
        }
        return ResponseEntity.badRequest().build();
    }
}


