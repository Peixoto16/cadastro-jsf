package com.teste.sinerji.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.teste.sinerji.application.dto.EnderecoDTO;
import com.teste.sinerji.domain.entity.Endereco;
import com.teste.sinerji.domain.entity.Pessoa;

/**
 * Mapper para conversão entre Endereco e EnderecoDTO.
 * 
 * @author Teste Sinerji
 */
@Stateless
public class EnderecoMapper {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Converte uma entidade Endereco para DTO.
     * 
     * @param endereco A entidade Endereco
     * @return O DTO correspondente
     */
    public EnderecoDTO toDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .estado(endereco.getEstado())
                .cidade(endereco.getCidade())
                .logradouro(endereco.getLogradouro())
                .numero(endereco.getNumero())
                .cep(endereco.getCep())
                .pessoaId(endereco.getPessoa() != null ? endereco.getPessoa().getId() : null)
                .build();
    }
    
    /**
     * Converte um DTO para entidade Endereco.
     * 
     * @param dto O DTO de Endereco
     * @return A entidade correspondente
     */
    public Endereco toEntity(EnderecoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Endereco endereco = Endereco.builder()
                .id(dto.getId())
                .estado(dto.getEstado())
                .cidade(dto.getCidade())
                .logradouro(dto.getLogradouro())
                .numero(dto.getNumero())
                .cep(dto.getCep())
                .build();
        
        // Se tiver ID da pessoa, busca a entidade Pessoa
        if (dto.getPessoaId() != null) {
            Pessoa pessoa = em.find(Pessoa.class, dto.getPessoaId());
            endereco.setPessoa(pessoa);
        }
        
        return endereco;
    }
    
    /**
     * Converte uma lista de entidades para uma lista de DTOs.
     * 
     * @param enderecos Lista de entidades Endereco
     * @return Lista de DTOs correspondentes
     */
    public List<EnderecoDTO> toDTOList(List<Endereco> enderecos) {
        if (enderecos == null) {
            return null;
        }
        
        return enderecos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
