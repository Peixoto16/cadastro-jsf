package com.teste.sinerji.application.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mapear a resposta da API ViaCEP.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViaCepDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    
    @JsonProperty("localidade")
    private String cidade;
    
    @JsonProperty("uf")
    private String estado;
    
    private String ibge;
    private String gia;
    private String ddd;
    private String siafi;
    
    private boolean erro;
}
