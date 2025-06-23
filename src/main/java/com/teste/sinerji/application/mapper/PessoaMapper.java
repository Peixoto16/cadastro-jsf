package com.teste.sinerji.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.teste.sinerji.application.dto.EnderecoDTO;
import com.teste.sinerji.application.dto.PessoaDTO;
import com.teste.sinerji.domain.entity.Endereco;
import com.teste.sinerji.domain.entity.Pessoa;

/**
 * Mapper para conversão entre Pessoa e PessoaDTO.
 * 
 * @author Teste Sinerji
 */
@Stateless
public class PessoaMapper {
    
    @Inject
    private EnderecoMapper enderecoMapper;
    
    /**
     * Converte uma entidade Pessoa para DTO.
     * 
     * @param pessoa A entidade Pessoa
     * @return O DTO correspondente
     */
    public PessoaDTO toDTO(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }
        
        List<EnderecoDTO> enderecosDTO = pessoa.getEnderecos().stream()
                .map(enderecoMapper::toDTO)
                .collect(Collectors.toList());
        
        return PessoaDTO.builder()
                .id(pessoa.getId())
                .nome(pessoa.getNome())
                .cpf(pessoa.getCpf())
                .dataNascimento(pessoa.getDataNascimento())
                .sexo(pessoa.getSexo())
                .enderecos(enderecosDTO)
                .build();
    }
    
    /**
     * Converte um DTO para entidade Pessoa.
     * 
     * @param dto O DTO de Pessoa
     * @return A entidade correspondente
     */
    public Pessoa toEntity(PessoaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Pessoa pessoa = Pessoa.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .dataNascimento(dto.getDataNascimento())
                .sexo(dto.getSexo())
                .build();
        
        if (dto.getEnderecos() != null) {
            List<Endereco> enderecos = dto.getEnderecos().stream()
                    .map(enderecoDTO -> {
                        Endereco endereco = enderecoMapper.toEntity(enderecoDTO);
                        endereco.setPessoa(pessoa);
                        return endereco;
                    })
                    .collect(Collectors.toList());
            
            pessoa.setEnderecos(enderecos);
        }
        
        return pessoa;
    }
    
    /**
     * Converte uma lista de entidades para uma lista de DTOs.
     * 
     * @param pessoas Lista de entidades Pessoa
     * @return Lista de DTOs correspondentes
     */
    public List<PessoaDTO> toDTOList(List<Pessoa> pessoas) {
        if (pessoas == null) {
            return null;
        }
        
        return pessoas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
