package com.teste.sinerji.presentation.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Testes unitários para o LoginBean.
 * Testa a lógica de autenticação e autorização.
 */
@ExtendWith(MockitoExtension.class)
class LoginBeanTest {

    @InjectMocks
    private LoginBean loginBean;

    @Mock
    private FacesContext facesContext;

    @Mock
    private ExternalContext externalContext;

    private Map<String, Object> sessionMap;

    private MockedStatic<FacesContext> mockedFacesContext;
    
    @BeforeEach
    void setUp() {
       
        mockedFacesContext = mockStatic(FacesContext.class);
        mockedFacesContext.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
        
        lenient().when(facesContext.getExternalContext()).thenReturn(externalContext);

        sessionMap = new HashMap<>();
        lenient().when(externalContext.getSessionMap()).thenReturn(sessionMap);
    }
    
    @AfterEach
    void tearDown() {
        // Fechar o mock estático para evitar vazamento entre testes
        if (mockedFacesContext != null) {
            mockedFacesContext.close();
        }
    }

    @Test
    @DisplayName("Login com credenciais de admin deve autenticar como ADMIN")
    void loginComCredenciaisDeAdminDeveAutenticarComoAdmin() {
        loginBean.setUsername("admin");
        loginBean.setPassword("admin");

        String resultado = loginBean.login();

        assertEquals("/index.xhtml?faces-redirect=true", resultado);
        assertEquals("ADMIN", loginBean.getRole());
        assertTrue(loginBean.isLoggedIn());
        assertTrue(loginBean.isAdmin());
        assertFalse(loginBean.isUser());
    }

    @Test
    @DisplayName("Login com credenciais de user deve autenticar como USER")
    void loginComCredenciaisDeUserDeveAutenticarComoUser() {
        loginBean.setUsername("user");
        loginBean.setPassword("user");

        String resultado = loginBean.login();

        assertEquals("/index.xhtml?faces-redirect=true", resultado);
        assertEquals("USER", loginBean.getRole());
        assertTrue(loginBean.isLoggedIn());
        assertFalse(loginBean.isAdmin());
        assertTrue(loginBean.isUser());
    }

    @Test
    @DisplayName("Login com credenciais inválidas deve falhar")
    void loginComCredenciaisInvalidasDeveFalhar() {
        loginBean.setUsername("invalido");
        loginBean.setPassword("invalido");

        String resultado = loginBean.login();

        assertNull(resultado);
        assertNull(loginBean.getRole());
        assertFalse(loginBean.isLoggedIn());
        verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
    }

    @Test
    @DisplayName("Logout deve invalidar a sessão")
    void logoutDeveInvalidarSessao() {
        loginBean.setUsername("admin");
        loginBean.setPassword("admin");
        loginBean.login();

        String resultado = loginBean.logout();

        assertEquals("/login.xhtml?faces-redirect=true", resultado);
        verify(externalContext).invalidateSession();
    }

    @Test
    @DisplayName("checkLogin deve redirecionar quando não autenticado")
    void checkLoginDeveRedirecionarQuandoNaoAutenticado() throws IOException {
        
        loginBean.checkLogin();
        
        verify(externalContext).redirect("login.xhtml");
    }

    @Test
    @DisplayName("checkAdmin deve redirecionar quando não é admin")
    void checkAdminDeveRedirecionarQuandoNaoEAdmin() throws IOException {
        loginBean.setUsername("user");
        loginBean.setPassword("user");
        loginBean.login();
        when(externalContext.getRequestContextPath()).thenReturn("");

        loginBean.checkAdmin();
        
        verify(externalContext).redirect("/index.xhtml");
        assertEquals("Rota não autorizada para user !", sessionMap.get("errorMessage"));
    }

    @Test
    @DisplayName("checkErrorMessages deve exibir mensagem de erro da sessão")
    void checkErrorMessagesDeveExibirMensagemDeErroDaSessao() {
       
        sessionMap.put("errorMessage", "Mensagem de teste");

        loginBean.checkErrorMessages();

        verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
        assertNull(sessionMap.get("errorMessage"));
    }
}
