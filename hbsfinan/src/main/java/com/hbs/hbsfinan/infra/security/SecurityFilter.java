package com.hbs.hbsfinan.infra.security;

import com.hbs.hbsfinan.exceptions.*;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.infra.db.SingletonDB;
import com.hbs.hbsfinan.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private Conexao dbConnFactory;

    @Autowired
    TokenService tokenService;

    UsuarioService usuarioService;

    public SecurityFilter(TokenService tokenService) {
        this.dbConnFactory = Conexao.getInstance();
        this.usuarioService = new UsuarioService(dbConnFactory);
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException
    {
        try {
            String path = request.getRequestURI();

            usuarioService.validarUsuarioPrimeiroAcesso();

            if (path.equals("/login")) {
                filterChain.doFilter(request, response);
                return;
            }

            var token = this.recoverToken(request);

            if (token == null) throw new MissingTokenException("Token ausente!");

            var login = tokenService.validateToken(token);

            if (login == null) throw new InvalidTokenException("Token inválido ou expirado!");

            UserDetails user = usuarioService.findByEmail(login);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (UsuarioNotFoundException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\": \"Token inválido!\"}");
        } catch ( InvalidTokenException | MissingTokenException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\": \"Erro interno na autenticação\"}");
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
