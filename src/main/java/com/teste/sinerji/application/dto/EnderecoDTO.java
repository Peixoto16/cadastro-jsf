package com.teste.sinerji.application.dto;

import java.io.Serializable;

import com.teste.sinerji.domain.enums.Estado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de Endereço entre as camadas.
 * 
 * @author Teste Sinerji
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    @NotNull(message = "Estado é obrigatório")
    private Estado estado;
    
    @NotBlank(message = "Cidade é obrigatória")
    @Size(min = 2, max = 100, message = "Cidade deve ter entre 2 e 100 caracteres")
    private String cidade;
    
    @NotBlank(message = "Logradouro é obrigatório")
    @Size(min = 5, max = 100, message = "Logradouro deve ter entre 5 e 100 caracteres")
    private String logradouro;
    
    @NotNull(message = "Número é obrigatório")
    private Integer numero;
    
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 00000-000")
    private String cep;

    public void setCep(String cep) {
        if (cep != null) {
            this.cep = cep.replace(".", "");
        } else {
            this.cep = null;
        }
    }

    public String getCep() {
        return this.cep;
    }
    
    private Long pessoaId;
    
    /**
     * Retorna o endereço completo formatado.
     * 
     * @return String com o endereço completo
     */
    public String getEnderecoCompleto() {
        return String.format("%s, %d - %s/%s - CEP: %s", 
            logradouro, numero, cidade, estado != null ? estado.name() : "", cep);
    }
}
