package com.teste.sinerji.shared.exception;

import jakarta.ejb.ApplicationException;

/**
 * Exceção para erros de regras de negócio.
 * 
 * @author Teste Sinerji
 */
@ApplicationException(rollback = true)
public class BusinessException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
