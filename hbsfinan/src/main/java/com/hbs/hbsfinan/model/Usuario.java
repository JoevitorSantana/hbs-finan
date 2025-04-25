package com.hbs.hbsfinan.model;

import com.hbs.hbsfinan.enums.UserRole;
import com.hbs.hbsfinan.utils.UserRoleConverter;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
public class Usuario implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String ultimo_nome;

    @Convert(converter = UserRoleConverter.class)
    @Column(nullable = false)
    private UserRole role;
    private String email;
    private String senha;

    public Usuario() {}

    public int getId() { return id; }
    public String getNome() {
        return nome;
    }
    public String getUltimoNome() { return ultimo_nome; }
    public UserRole getRole() {
        return role;
    }
    public String getEmail() { return email; }
    public String getSenha() {
        return senha;
    }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setUltimoNome(String ultimoNome) { this.ultimo_nome = ultimoNome; }
    public void setRole(UserRole role) { this.role = role; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
