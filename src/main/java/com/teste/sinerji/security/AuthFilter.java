package com.teste.sinerji.security;

import com.teste.sinerji.presentation.controller.LoginBean;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro para controlar acesso a páginas protegidas.
 * Redireciona para a página de login se o usuário não estiver autenticado.
 */
@WebFilter(urlPatterns = {"*.xhtml"})
public class AuthFilter implements Filter {

    @Inject
    private LoginBean loginBean;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização do filtro
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Obtém o caminho da requisição
        String requestPath = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        
        // Verifica se é a página de login ou recursos estáticos
        boolean isLoginPage = requestPath.equals("/login.xhtml");
        boolean isResourceRequest = requestPath.startsWith("/jakarta.faces.resource/") || 
                                   requestPath.startsWith("/javax.faces.resource/");
        
        if (isLoginPage || isResourceRequest || loginBean.isLoggedIn()) {
            // Se for a página de login, recursos estáticos ou usuário autenticado, continua a requisição
            chain.doFilter(request, response);
        } else {
            // Caso contrário, redireciona para a página de login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
        }
    }

    @Override
    public void destroy() {
        // Limpeza de recursos
    }
}
