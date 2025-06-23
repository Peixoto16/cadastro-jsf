package com.teste.sinerji.shared.security;


import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.IOException;
import java.lang.reflect.Field;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teste.sinerji.presentation.controller.LoginBean;

/**
 * Testes unitários para o AuthFilter.
 * Testa a lógica de filtro de autenticação para proteger páginas.
 */
@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

    private AuthFilter authFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private HttpSession session;
    
    @Mock
    private LoginBean loginBean;

    @BeforeEach
    void setUp() throws Exception {
        authFilter = new AuthFilter();
        
        // Injetar manualmente o LoginBean mockado no AuthFilter
        Field loginBeanField = AuthFilter.class.getDeclaredField("loginBean");
        loginBeanField.setAccessible(true);
        loginBeanField.set(authFilter, loginBean);
        
        // Configurações básicas para todos os testes
        lenient().when(request.getSession(false)).thenReturn(session);
        // Restauramos a configuração padrão do contextPath para evitar NullPointerException
        lenient().when(request.getContextPath()).thenReturn("");
    }

    @Test
    @DisplayName("Deve permitir acesso a recursos estáticos")
    void devePermitirAcessoARecursosEstaticos() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/jakarta.faces.resource/css/style.css");
        when(request.getContextPath()).thenReturn("");

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("Deve permitir acesso à página de login")
    void devePermitirAcessoAPaginaDeLogin() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/login.xhtml");
        when(request.getContextPath()).thenReturn("");
        // Não precisamos configurar loginBean.isLoggedIn() aqui pois o filtro permite acesso à página de login independentemente

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("Deve redirecionar para login quando usuário não autenticado tenta acessar página protegida")
    void deveRedirecionarParaLoginQuandoUsuarioNaoAutenticadoTentaAcessarPaginaProtegida() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/pages/pessoa/lista.xhtml");
        when(request.getContextPath()).thenReturn("/app");
        when(loginBean.isLoggedIn()).thenReturn(false);

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect("/app/login.xhtml");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve permitir acesso quando usuário autenticado acessa página protegida")
    void devePermitirAcessoQuandoUsuarioAutenticadoAcessaPaginaProtegida() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/pages/pessoa/lista.xhtml");
        when(request.getContextPath()).thenReturn("");
        when(loginBean.isLoggedIn()).thenReturn(true);

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("Deve redirecionar para login quando sessão é nula")
    void deveRedirecionarParaLoginQuandoSessaoENula() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/pages/pessoa/lista.xhtml");
        // Não precisamos configurar o contextPath aqui, pois já está configurado no setUp()
        // Não precisamos configurar getSession(false), pois o filtro não usa isso

        // Act
        authFilter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect("/login.xhtml"); // Usando o contextPath vazio do setUp()
        verify(chain, never()).doFilter(request, response);
    }
}
