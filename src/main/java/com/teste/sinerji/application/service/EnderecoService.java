package com.teste.sinerji.application.service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import com.teste.sinerji.application.dto.EnderecoDTO;
import com.teste.sinerji.application.mapper.EnderecoMapper;
import com.teste.sinerji.domain.entity.Endereco;
import com.teste.sinerji.domain.entity.Pessoa;
import com.teste.sinerji.infrastructure.repository.EnderecoRepository;
import com.teste.sinerji.infrastructure.repository.PessoaRepository;
import com.teste.sinerji.shared.exception.BusinessException;
import com.teste.sinerji.shared.exception.EntityNotFoundException;

/**
 * Service para operações de negócio relacionadas a Endereço.
 * 
 * @author Teste Sinerji
 */
@Stateless
public class EnderecoService implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private EnderecoRepository enderecoRepository;
    
    @Inject
    private PessoaRepository pessoaRepository;
    
    @Inject
    private EnderecoMapper enderecoMapper;
    
    /**
     * Lista todos os endereços cadastrados.
     * 
     * @return Lista de endereços DTO
     */
    public List<EnderecoDTO> listarTodos() {
        List<Endereco> enderecos = enderecoRepository.listarTodos();
        return enderecoMapper.toDTOList(enderecos);
    }
    
    /**
     * Busca um endereço por ID.
     * 
     * @param id O ID do endereço
     * @return O endereço encontrado
     * @throws EntityNotFoundException Se o endereço não for encontrado
     */
    public EnderecoDTO buscarPorId(Long id) throws EntityNotFoundException {
        Optional<Endereco> enderecoOpt = enderecoRepository.buscarPorId(id);
        Endereco endereco = enderecoOpt.orElseThrow(() -> 
            new EntityNotFoundException("Endereço não encontrado com o ID: " + id));
        return enderecoMapper.toDTO(endereco);
    }
    
    /**
     * Lista todos os endereços de uma pessoa.
     * 
     * @param pessoaId O ID da pessoa
     * @return Lista de endereços da pessoa
     * @throws EntityNotFoundException Se a pessoa não for encontrada
     */
    public List<EnderecoDTO> listarPorPessoa(Long pessoaId) throws EntityNotFoundException {
        if (!pessoaRepository.buscarPorId(pessoaId).isPresent()) {
            throw new EntityNotFoundException("Pessoa não encontrada com o ID: " + pessoaId);
        }
        
        List<Endereco> enderecos = enderecoRepository.listarPorPessoa(pessoaId);
        return enderecoMapper.toDTOList(enderecos);
    }
    
    /**
     * Busca endereços por cidade (busca parcial).
     * 
     * @param cidade A cidade ou parte da cidade para busca
     * @return Lista de endereços encontrados
     */
    public List<EnderecoDTO> buscarPorCidade(String cidade) {
        List<Endereco> enderecos = enderecoRepository.buscarPorCidade(cidade);
        return enderecoMapper.toDTOList(enderecos);
    }
    
    /**
     * Salva um novo endereço.
     * 
     * @param dto Dados do endereço a ser salvo
     * @return O endereço salvo
     * @throws BusinessException Se houver erro de validação
     * @throws EntityNotFoundException Se a pessoa não for encontrada
     */
    @Transactional
    public EnderecoDTO salvar(EnderecoDTO dto) throws BusinessException, EntityNotFoundException {
        Objects.requireNonNull(dto, "DTO de endereço não pode ser nulo");
        
        if (dto.getId() != null) {
            throw new BusinessException("ID deve ser nulo para um novo endereço");
        }
        
        validarEndereco(dto);
        
        if (dto.getPessoaId() == null) {
            throw new BusinessException("ID da pessoa é obrigatório");
        }
        
        Optional<Pessoa> pessoaOpt = pessoaRepository.buscarPorId(dto.getPessoaId());
        if (!pessoaOpt.isPresent()) {
            throw new EntityNotFoundException("Pessoa não encontrada com o ID: " + dto.getPessoaId());
        }
        
        Endereco endereco = enderecoMapper.toEntity(dto);
        endereco = enderecoRepository.salvar(endereco);
        
        return enderecoMapper.toDTO(endereco);
    }
    
    /**
     * Atualiza um endereço existente.
     * 
     * @param dto Dados atualizados do endereço
     * @return O endereço atualizado
     * @throws EntityNotFoundException Se o endereço ou a pessoa não for encontrado
     * @throws BusinessException Se houver erro de validação
     */
    @Transactional
    public EnderecoDTO atualizar(EnderecoDTO dto) throws EntityNotFoundException, BusinessException {
        Objects.requireNonNull(dto, "DTO de endereço não pode ser nulo");
        
        if (dto.getId() == null) {
            throw new BusinessException("ID é obrigatório para atualização");
        }
        
        buscarPorId(dto.getId());
        
        validarEndereco(dto);
        
        if (dto.getPessoaId() == null) {
            throw new BusinessException("ID da pessoa é obrigatório");
        }
        
        Optional<Pessoa> pessoaOpt = pessoaRepository.buscarPorId(dto.getPessoaId());
        if (!pessoaOpt.isPresent()) {
            throw new EntityNotFoundException("Pessoa não encontrada com o ID: " + dto.getPessoaId());
        }
        
        Endereco endereco = enderecoMapper.toEntity(dto);
        endereco = enderecoRepository.salvar(endereco);
        
        return enderecoMapper.toDTO(endereco);
    }
    
    /**
     * Remove um endereço.
     * 
     * @param id ID do endereço a ser removido
     * @throws EntityNotFoundException Se o endereço não for encontrado
     */
    @Transactional
    public void remover(Long id) throws EntityNotFoundException {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        
        Optional<Endereco> enderecoOpt = enderecoRepository.buscarPorId(id);
        if (!enderecoOpt.isPresent()) {
            throw new EntityNotFoundException("Endereço não encontrado com o ID: " + id);
        }
        
        enderecoRepository.remover(id);
    }
    
    /**
     * Conta o total de endereços cadastrados.
     * 
     * @return O número total de endereços
     */
    public long contarTodos() {
        return enderecoRepository.contarTodos();
    }
    
    /**
     * Valida os dados de um endereço.
     * 
     * @param dto O endereço a ser validado
     * @throws BusinessException Se houver erro de validação
     */
    private void validarEndereco(EnderecoDTO dto) throws BusinessException {
        if (dto == null) {
            throw new BusinessException("Dados do endereço são obrigatórios");
        }
        
        if (dto.getEstado() == null) {
            throw new BusinessException("Estado é obrigatório");
        }
        
        if (dto.getCidade() == null || dto.getCidade().trim().isEmpty()) {
            throw new BusinessException("Cidade é obrigatória");
        }
        
        if (dto.getLogradouro() == null || dto.getLogradouro().trim().isEmpty()) {
            throw new BusinessException("Logradouro é obrigatório");
        }
        
        if (dto.getNumero() == null) {
            throw new BusinessException("Número é obrigatório");
        }
        
        if (dto.getCep() == null || dto.getCep().trim().isEmpty()) {
            throw new BusinessException("CEP é obrigatório");
        }
        
        String cep = dto.getCep().replaceAll("[^0-9]", "");
        if (cep.length() != 8) {
            throw new BusinessException("CEP deve conter 8 dígitos");
        }
    }
}
