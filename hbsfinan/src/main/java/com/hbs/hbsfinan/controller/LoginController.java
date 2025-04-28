package com.hbs.hbsfinan.controller;

import com.hbs.hbsfinan.dto.LoginDTO;
import com.hbs.hbsfinan.dto.LoginResponseDTO;
import com.hbs.hbsfinan.infra.security.TokenService;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO usuario) {
        System.out.println(usuario.email() + " " + usuario.senha());
        var emailSenha = new UsernamePasswordAuthenticationToken(usuario.email(), usuario.senha());
        var login = this.authenticationManager.authenticate(emailSenha);
        var token = tokenService.generateToken((Usuario) login.getPrincipal());

        Usuario objUsuario = loginService.loadUsuarioByEmail(usuario.email());

        LoginResponseDTO response = new LoginResponseDTO(objUsuario.getNome(), objUsuario.getUltimoNome(), objUsuario.getEmail(), objUsuario.getRole(), token);

        return ResponseEntity.ok(response);
    }
}
