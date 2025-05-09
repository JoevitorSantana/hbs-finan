package com.hbs.hbsfinan.config;

import com.hbs.hbsfinan.service.ParametrizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Registra o ParametrizacaoInterceptor para todas as rotas da aplicação.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ParametrizacaoService parametrizacaoService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ParametrizacaoInterceptor(parametrizacaoService))
                .addPathPatterns("/**")
                // opcional: excluir estáticos se quiser
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/webjars/**");
    }
}
