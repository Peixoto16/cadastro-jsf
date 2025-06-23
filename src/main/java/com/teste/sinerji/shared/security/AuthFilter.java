package com.teste.sinerji.shared.security;

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
        
        String requestPath = getRequestPath(httpRequest);
        
        if (isPublicResource(requestPath) || loginBean.isLoggedIn()) {
            // Se for recurso público ou usuário autenticado, continua a requisição
            chain.doFilter(request, response);
        } else {
            // Caso contrário, redireciona para a página de login
            redirectToLogin(httpRequest, httpResponse);
        }
    }
    
    /**
     * Obtém o caminho da requisição sem o contexto da aplicação
     * 
     * @param request A requisição HTTP
     * @return O caminho da requisição
     */
    private String getRequestPath(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }
    
    /**
     * Verifica se o recurso solicitado é público (página de login ou recursos estáticos)
     * 
     * @param path O caminho da requisição
     * @return true se for um recurso público, false caso contrário
     */
    private boolean isPublicResource(String path) {
        boolean isLoginPage = path.equals("/login.xhtml");
        boolean isResourceRequest = path.startsWith("/jakarta.faces.resource/") || 
                                  path.startsWith("/javax.faces.resource/");
        return isLoginPage || isResourceRequest;
    }
    
    /**
     * Redireciona para a página de login
     * 
     * @param request A requisição HTTP
     * @param response A resposta HTTP
     * @throws IOException Se ocorrer um erro de I/O durante o redirecionamento
     */
    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login.xhtml");
    }

    @Override
    public void destroy() {
        // Limpeza de recursos
    }
}
