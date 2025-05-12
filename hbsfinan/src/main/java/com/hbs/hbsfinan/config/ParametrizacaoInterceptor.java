package com.hbs.hbsfinan.config;

import com.hbs.hbsfinan.exceptions.ParametrizacaoNaoEncontradaException;
import com.hbs.hbsfinan.service.ParametrizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor que verifica, em toda requisição, se já há parametrização
 * cadastrada. Se não, redireciona automaticamente para a tela de parametrização.
 */
//public class ParametrizacaoInterceptor implements HandlerInterceptor {
//
//    private final ParametrizacaoService parametrizacaoService;
//
//    public ParametrizacaoInterceptor(ParametrizacaoService parametrizacaoService) {
//        this.parametrizacaoService = parametrizacaoService;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String path = request.getRequestURI();
//
//        // Permite sempre acessar a própria parametrização e caminhos de erro
//        if (path.startsWith(request.getContextPath() + "/parametrizacao") ||
//                path.startsWith(request.getContextPath() + "/api/parametrizacao") ||
//                path.startsWith(request.getContextPath() + "/error")) {
//            return true;
//        }
//
//        // Se não tiver parametrização, força redirecionamento
//        if (!parametrizacaoService.exists()) {
//            response.sendRedirect(request.getContextPath() + "/parametrizacao");
//            return false;
//        }
//
//        return true;
//    }
//}
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
        // se já estivermos na rota /parametrizacao, deixa passar (para criar ou get)
        String path = request.getRequestURI();
        if ( path.equals("/login")) {
            return true;
        }

        // se não existir parametrização ainda, lança exceção
        if (!service.exists()) {
            throw new ParametrizacaoNaoEncontradaException("Parametrização não encontrada.");
        }

        // caso contrário, continua normalmente
        return true;
    }
}

