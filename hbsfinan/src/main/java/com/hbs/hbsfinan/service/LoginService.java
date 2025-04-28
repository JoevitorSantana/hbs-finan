package com.hbs.hbsfinan.service;

import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.repository.implementation.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public String authenticate(String email, String senha) {
        return null;
    }

    public Usuario loadUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email);
    }
}
