package com.hbs.hbsfinan.config;

import com.hbs.hbsfinan.exceptions.ParametrizacaoNaoEncontradaException;
import com.hbs.hbsfinan.service.ParametrizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Interceptor que verifica, em toda requisição, se já há parametrização cadastrada. Se não, lança uma exceção indicando a ausência.*/
@Component
public class ParametrizacaoInterceptor implements HandlerInterceptor {
    private final ParametrizacaoService service;

    @Autowired
    public ParametrizacaoInterceptor(ParametrizacaoService service) {
        this.service = service;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String path = request.getRequestURI();
        if (path.equals("/login") || path.equals("/parametrizacao") || path.startsWith("/error")) {
            return true;
        }
        if (!service.existsParametrizacao()) {
            throw new ParametrizacaoNaoEncontradaException("Nenhuma parametrização cadastrada. Por favor, configure a parametrização inicial.");
        }
        return true;
    }
}