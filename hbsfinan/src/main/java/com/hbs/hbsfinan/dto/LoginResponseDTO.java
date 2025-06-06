package com.hbs.hbsfinan.dto;

import com.hbs.hbsfinan.enums.UserRole;

public record LoginResponseDTO(int id, String nome, String ultimoNome, String email, UserRole role, String token){}