package com.teste.sinerji.domain.enums;

import lombok.Getter;

/**
 * Enumeração para representar os estados brasileiros.
 * 
 * @author Teste Sinerji
 */
@Getter
public enum Estado {
    
    AC("Acre"),
    AL("Alagoas"),
    AP("Amapá"),
    AM("Amazonas"),
    BA("Bahia"),
    CE("Ceará"),
    DF("Distrito Federal"),
    ES("Espírito Santo"),
    GO("Goiás"),
    MA("Maranhão"),
    MT("Mato Grosso"),
    MS("Mato Grosso do Sul"),
    MG("Minas Gerais"),
    PA("Pará"),
    PB("Paraíba"),
    PR("Paraná"),
    PE("Pernambuco"),
    PI("Piauí"),
    RJ("Rio de Janeiro"),
    RN("Rio Grande do Norte"),
    RS("Rio Grande do Sul"),
    RO("Rondônia"),
    RR("Roraima"),
    SC("Santa Catarina"),
    SP("São Paulo"),
    SE("Sergipe"),
    TO("Tocantins");
    
    private final String nome;
    
    Estado(String nome) {
        this.nome = nome;
    }
    
    public static Estado fromSigla(String sigla) {
        if (sigla == null) {
            return null;
        }
        
        for (Estado estado : Estado.values()) {
            if (estado.name().equals(sigla)) {
                return estado;
            }
        }
        
        throw new IllegalArgumentException("Sigla de estado inválida: " + sigla);
    }
}
