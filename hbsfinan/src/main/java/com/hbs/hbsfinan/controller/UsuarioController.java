package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/novo")
    public ResponseEntity save(@RequestBody Usuario usuario) {
        try {
            // validar campos vindos do body
            usuarioService.save(usuario);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> findAll() {
        try
        {
            return ResponseEntity.ok(usuarioService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Usuario usuario) {
        try {
            Usuario oldUsuario = usuarioService.findById(id);

            if (oldUsuario == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");

            if (usuario.getNome() != null && !usuario.getNome().equals(oldUsuario.getNome()))
                oldUsuario.setNome(usuario.getNome());

            if (usuario.getUltimoNome() != null && !usuario.getUltimoNome().equals(oldUsuario.getUltimoNome()))
                oldUsuario.setUltimoNome(usuario.getUltimoNome());

            if (usuario.getRole() != null && !usuario.getRole().equals(oldUsuario.getRole()))
                oldUsuario.setRole(usuario.getRole());

            if (usuario.getEmail() != null && !usuario.getEmail().equals(oldUsuario.getEmail()))
                oldUsuario.setEmail(usuario.getEmail());

            if (usuario.getSenha() != null && !usuario.getSenha().equals(oldUsuario.getSenha()))
                oldUsuario.setSenha(usuario.getSenha());

            // adicionar atualização de role também

            usuarioService.update(oldUsuario);
            return ResponseEntity.ok("Editado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable int id) {
        Usuario usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        // refatorar validação
        if (usuarioService.findById(id) != null) {
            usuarioService.delete(id);
            return ResponseEntity.ok("Deletado com sucesso!");
        }
        return ResponseEntity.badRequest().build();
    }
}
