package com.teste.sinerji.domain.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.teste.sinerji.domain.enums.Estado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidade que representa um endereço no sistema.
 * 
 * @author Teste Sinerji
 */
@Entity
@Table(name = "endereco")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Endereco implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Estado é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 2, nullable = false)
    private Estado estado;
    
    @NotBlank(message = "Cidade é obrigatória")
    @Size(min = 2, max = 100, message = "Cidade deve ter entre 2 e 100 caracteres")
    @Column(name = "cidade", length = 100, nullable = false)
    private String cidade;
    
    @NotBlank(message = "Logradouro é obrigatório")
    @Size(min = 5, max = 100, message = "Logradouro deve ter entre 5 e 100 caracteres")
    @Column(name = "logradouro", length = 100, nullable = false)
    private String logradouro;
    
    @NotNull(message = "Número é obrigatório")
    @Column(name = "numero")
    private Integer numero;
    
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 00000-000")
    @Column(name = "cep", length = 9, nullable = false)
    private String cep;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", nullable = false)
    @NotNull(message = "Pessoa é obrigatória")
    @ToString.Exclude
    private Pessoa pessoa;
    
    /**
     * Retorna o endereço completo formatado.
     * 
     * @return String com o endereço completo
     */
    @Transient
    public String getEnderecoCompleto() {
        return String.format("%s, %d - %s/%s - CEP: %s", 
            logradouro, numero, cidade, estado != null ? estado.name() : "", cep);
    }
}
