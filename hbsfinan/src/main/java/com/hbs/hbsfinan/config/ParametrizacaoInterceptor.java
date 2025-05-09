package com.hbs.hbsfinan.config;

import com.hbs.hbsfinan.service.ParametrizacaoService;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor que verifica, em toda requisição, se já há parametrização
 * cadastrada. Se não, redireciona automaticamente para a tela de parametrização.
 */
public class ParametrizacaoInterceptor implements HandlerInterceptor {

    private final ParametrizacaoService parametrizacaoService;

    public ParametrizacaoInterceptor(ParametrizacaoService parametrizacaoService) {
        this.parametrizacaoService = parametrizacaoService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        // Permite sempre acessar a própria parametrização e caminhos de erro
        if (path.startsWith(request.getContextPath() + "/parametrizacao") ||
                path.startsWith(request.getContextPath() + "/api/parametrizacao") ||
                path.startsWith(request.getContextPath() + "/error")) {
            return true;
        }

        // Se não tiver parametrização, força redirecionamento
        if (!parametrizacaoService.exists()) {
            response.sendRedirect(request.getContextPath() + "/parametrizacao");
            return false;
        }

        return true;
    }
}
