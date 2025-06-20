package com.teste.sinerji.domain.enums;

import lombok.Getter;

/**
 * Enumeração para representar o sexo de uma pessoa.
 * 
 * @author Teste Sinerji
 */
@Getter
public enum Sexo {
    
    M("Masculino"),
    F("Feminino");
    
    private final String descricao;
    
    Sexo(String descricao) {
        this.descricao = descricao;
    }
    
    public static Sexo fromCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        
        for (Sexo sexo : Sexo.values()) {
            if (sexo.name().equals(codigo)) {
                return sexo;
            }
        }
        
        throw new IllegalArgumentException("Código de sexo inválido: " + codigo);
    }
}
