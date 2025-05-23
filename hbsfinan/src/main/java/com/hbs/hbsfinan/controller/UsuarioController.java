package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.RestResponseMessage;
import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.dto.UsuarioEditResponseDTO;
import com.hbs.hbsfinan.dto.UsuarioResponseDTO;
import com.hbs.hbsfinan.exceptions.EmailExistenteException;
import com.hbs.hbsfinan.exceptions.RoleInvalidaException;
import com.hbs.hbsfinan.exceptions.UsuarioNotFoundException;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.service.UsuarioService;
import jakarta.validation.Valid;
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
    public ResponseEntity save(@Valid @RequestBody UsuarioCreateDTO usuario) {
        try {
            // validar campos vindos do body
            // validar email
            usuarioService.save(usuario);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.CREATED, "Usuário inserido com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (EmailExistenteException e){
            throw new EmailExistenteException(e.getMessage());
        } catch (RoleInvalidaException e) {
            throw new RoleInvalidaException(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
        try {
            return ResponseEntity.ok(usuarioService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        } return ResponseEntity.badRequest().build();
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody Usuario usuario) {
        try {
            Usuario oldUsuario = usuarioService.findById(id);

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

            usuarioService.update(oldUsuario);
            RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Usuário atualizado com sucesso!");
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (UsuarioNotFoundException e) {
            throw new UsuarioNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEditResponseDTO> findById(@PathVariable int id) {
        try {
            Usuario usuario = usuarioService.findById(id);
            UsuarioEditResponseDTO response = usuarioService.convertToUsuarioEditDTO(usuario);
            return ResponseEntity.ok(response);
        } catch (UsuarioNotFoundException e) {
            throw new UsuarioNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        usuarioService.findById(id);
        usuarioService.delete(id);
        RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Usuário excluído com sucesso!");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
