package com.teste.sinerji.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.teste.sinerji.domain.enums.Sexo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidade que representa uma pessoa no sistema.
 * 
 * @author Teste Sinerji
 */
@Entity
@Table(name = "pessoa")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pessoa implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres")
    @Column(name = "nome", length = 150, nullable = false)
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14, message = "CPF deve ter entre 11 e 14 caracteres")
    @Column(name = "cpf", length = 14, nullable = false, unique = true)
    private String cpf;
    
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    @Column(name = "idade")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    
    @NotNull(message = "Sexo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", length = 2, nullable = false)
    private Sexo sexo;
    
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Endereco> enderecos = new ArrayList<>();
    
    /**
     * Adiciona um endereço à pessoa e estabelece o relacionamento bidirecional.
     * 
     * @param endereco O endereço a ser adicionado
     * @return A própria pessoa para permitir chamadas encadeadas
     */
    public Pessoa adicionarEndereco(Endereco endereco) {
        enderecos.add(endereco);
        endereco.setPessoa(this);
        return this;
    }
    
    /**
     * Remove um endereço da pessoa.
     * 
     * @param endereco O endereço a ser removido
     * @return A própria pessoa para permitir chamadas encadeadas
     */
    public Pessoa removerEndereco(Endereco endereco) {
        enderecos.remove(endereco);
        endereco.setPessoa(null);
        return this;
    }
}
