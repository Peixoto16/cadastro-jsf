package com.teste.sinerji.application.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de Pessoa entre as camadas.
 * 
 * @author Teste Sinerji
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres")
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14, message = "CPF deve ter entre 11 e 14 caracteres")
    private String cpf;
    
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private Date dataNascimento;
    
    @NotNull(message = "Sexo é obrigatório")
    private com.teste.sinerji.domain.enums.Sexo sexo;
    
    @Builder.Default
    private List<EnderecoDTO> enderecos = new ArrayList<>();
    
    /**
     * Calcula a idade da pessoa com base na data de nascimento.
     * 
     * @return A idade em anos
     */
    public Integer getIdade() {
        if (dataNascimento == null) {
            return null;
        }
        
        // Calcula a idade com base na data de nascimento
        long diffInMillies = new Date().getTime() - dataNascimento.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        return (int) (diffInDays / 365.25);
    }
    
    /**
     * Retorna a descrição do sexo (Masculino/Feminino).
     * 
     * @return A descrição do sexo
     */
    public String getSexoDescricao() {
        if ("M".equals(sexo)) {
            return "Masculino";
        } else if ("F".equals(sexo)) {
            return "Feminino";
        }
        return "";
    }
}
