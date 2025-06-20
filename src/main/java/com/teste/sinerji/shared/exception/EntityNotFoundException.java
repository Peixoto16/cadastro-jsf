package com.teste.sinerji.shared.exception;

import jakarta.ejb.ApplicationException;

/**
 * Exceção para quando uma entidade não é encontrada.
 * 
 * @author Teste Sinerji
 */
@ApplicationException(rollback = true)
public class EntityNotFoundException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
