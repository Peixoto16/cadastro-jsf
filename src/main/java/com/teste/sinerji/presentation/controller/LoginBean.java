package com.teste.sinerji.presentation.controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 * Bean de login com usuários hardcoded (admin/admin e user/user).
 * Controla autenticação e autorização simples.
 */
@Named
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private String role; // "ADMIN" ou "USER"

    /**
     * Realiza o login do usuário.
     * 
     * @return String com a navegação para a página inicial em caso de sucesso
     */
    public String login() {
        if ("admin".equals(username) && "admin".equals(password)) {
            role = "ADMIN";
            return "/index.xhtml?faces-redirect=true";
        }
        if ("user".equals(username) && "user".equals(password)) {
            role = "USER";
            return "/index.xhtml?faces-redirect=true";
        }
        
        // Login inválido
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou senha inválidos", null));
        return null;
    }

    /**
     * Realiza o logout do usuário.
     * 
     * @return String com a navegação para a página de login
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    /**
     * Verifica se o usuário está autenticado.
     * 
     * @return true se o usuário está autenticado, false caso contrário
     */
    public boolean isLoggedIn() {
        return role != null;
    }

    /**
     * Verifica se o usuário é administrador.
     * 
     * @return true se o usuário é administrador, false caso contrário
     */
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    /**
     * Verifica se o usuário é um usuário comum.
     * 
     * @return true se o usuário é um usuário comum, false caso contrário
     */
    public boolean isUser() {
        return "USER".equals(role);
    }

    // Método para ser chamado no preRenderView das páginas protegidas
    public void checkLogin() throws java.io.IOException {
        if (!isLoggedIn()) {
            jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
    }

    // Método para ser chamado no preRenderView das páginas exclusivas de ADMIN
    public void checkAdmin() throws java.io.IOException {
        if (!isLoggedIn() || !isAdmin()) {
            // Armazenar a mensagem de erro na sessão
            jakarta.faces.context.FacesContext facesContext = jakarta.faces.context.FacesContext.getCurrentInstance();
            facesContext.getExternalContext().getSessionMap().put("errorMessage", "Rota não autorizada para user !");
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/index.xhtml");
        }
    }
    
    // Método para verificar e exibir mensagens de erro da sessão
    public void checkErrorMessages() {
        jakarta.faces.context.FacesContext facesContext = jakarta.faces.context.FacesContext.getCurrentInstance();
        String errorMessage = (String) facesContext.getExternalContext().getSessionMap().get("errorMessage");
        
        if (errorMessage != null) {
            facesContext.addMessage(null, new jakarta.faces.application.FacesMessage(
                    jakarta.faces.application.FacesMessage.SEVERITY_ERROR, errorMessage, null));
            // Limpar a mensagem após exibi-la
            facesContext.getExternalContext().getSessionMap().remove("errorMessage");
        }
    }

    // Getters e Setters
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
}
