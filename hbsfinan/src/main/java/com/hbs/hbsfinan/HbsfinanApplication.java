package com.hbs.hbsfinan;

import com.hbs.hbsfinan.dto.UsuarioCreateDTO;
import com.hbs.hbsfinan.infra.db.Conexao;
import com.hbs.hbsfinan.infra.db.SingletonDB;
import com.hbs.hbsfinan.model.Usuario;
import com.hbs.hbsfinan.service.UsuarioService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.SQLException;

@SpringBootApplication
public class HbsfinanApplication {

    public static void main(String[] args) {
        if (Conexao.getInstance() == null) {
            System.out.println(SingletonDB.getConexao().getMensagemErro());
        }
        validarPrimeiroAcesso();
        SpringApplication.run(HbsfinanApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    private static void validarPrimeiroAcesso() {
        // Verificar se não existem usuários cadastrados
        UsuarioService usuarioService = new UsuarioService(Conexao.getInstance());
        usuarioService.validarUsuarioPrimeiroAcesso();
    }
}
