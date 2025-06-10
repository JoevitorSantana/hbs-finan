package com.hbs.hbsfinan.config;

import com.hbs.hbsfinan.infra.db.Conexao; // Importa a classe Conexao
import com.hbs.hbsfinan.repository.implementation.ParametrizacaoRepository; // Importa o repositório
import com.hbs.hbsfinan.service.ParametrizacaoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Registra o ParametrizacaoInterceptor para todas as rotas da aplicação.*/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Conexao dbConnFactory = Conexao.getInstance();

        if (dbConnFactory == null || !dbConnFactory.getEstadoConexao()) {
            System.err.println("CRÍTICO: Falha na inicialização da Conexão. Não é possível configurar o ParametrizacaoInterceptor.");
            return;
        }

        ParametrizacaoRepository parametrizacaoRepository = new ParametrizacaoRepository(dbConnFactory);

        ParametrizacaoService parametrizacaoService = new ParametrizacaoService(dbConnFactory);

        registry.addInterceptor(new ParametrizacaoInterceptor(parametrizacaoService))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/parametrizacao",
                        "/error",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**"
                );
    }
}
