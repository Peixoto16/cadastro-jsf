package com.teste.sinerji.application.service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import com.teste.sinerji.application.dto.PessoaDTO;
import com.teste.sinerji.application.mapper.PessoaMapper;
import com.teste.sinerji.domain.entity.Pessoa;
import com.teste.sinerji.infrastructure.repository.PessoaRepository;
import com.teste.sinerji.shared.exception.BusinessException;
import com.teste.sinerji.shared.exception.EntityNotFoundException;

/**
 * Service para operações de negócio relacionadas à Pessoa.
 * 
 * @author Teste Sinerji
 */
@Stateless
public class PessoaService implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private PessoaRepository pessoaRepository;
    
    @Inject
    private PessoaMapper pessoaMapper;
    
    /**
     * Lista todas as pessoas cadastradas.
     * 
     * @return Lista de pessoas DTO
     */
    public List<PessoaDTO> listarTodas() {
        List<Pessoa> pessoas = pessoaRepository.listarTodas();
        return pessoaMapper.toDTOList(pessoas);
    }
    
    /**
     * Busca uma pessoa por ID.
     * 
     * @param id O ID da pessoa
     * @return A pessoa encontrada
     * @throws EntityNotFoundException Se a pessoa não for encontrada
     */
    public PessoaDTO buscarPorId(Long id) throws EntityNotFoundException {
        Optional<Pessoa> pessoaOpt = pessoaRepository.buscarPorId(id);
        Pessoa pessoa = pessoaOpt.orElseThrow(() -> 
            new EntityNotFoundException("Pessoa não encontrada com o ID: " + id));
        return pessoaMapper.toDTO(pessoa);
    }
    
    /**
     * Busca pessoas por nome (busca parcial).
     * 
     * @param nome O nome ou parte do nome para busca
     * @return Lista de pessoas encontradas
     */
    public List<PessoaDTO> buscarPorNome(String nome) {
        List<Pessoa> pessoas = pessoaRepository.buscarPorNome(nome);
        return pessoaMapper.toDTOList(pessoas);
    }
    
    /**
     * Salva uma nova pessoa.
     * 
     * @param dto Dados da pessoa a ser salva
     * @return A pessoa salva
     * @throws BusinessException Se houver erro de validação
     */
    @Transactional
    public PessoaDTO salvar(PessoaDTO dto) throws BusinessException {
        Objects.requireNonNull(dto, "DTO de pessoa não pode ser nulo");

        if (dto.getId() != null) {
            throw new BusinessException("ID deve ser nulo para uma nova pessoa");
        }

        validarPessoa(dto);

        Pessoa pessoa = pessoaMapper.toEntity(dto);
        try {
            pessoa = pessoaRepository.salvar(pessoa);
        } catch (Exception e) {
            verificarCpfDuplicado(e);
            throw e;
        }

        return pessoaMapper.toDTO(pessoa);
    }
    
    /**
     * Atualiza uma pessoa existente.
     * 
     * @param dto Dados atualizados da pessoa
     * @return A pessoa atualizada
     * @throws EntityNotFoundException Se a pessoa não for encontrada
     * @throws BusinessException Se houver erro de validação
     */
    @Transactional
    public PessoaDTO atualizar(PessoaDTO dto) throws EntityNotFoundException, BusinessException {
        Objects.requireNonNull(dto, "DTO de pessoa não pode ser nulo");
        
        if (dto.getId() == null) {
            throw new BusinessException("ID é obrigatório para atualização");
        }
        
        buscarPorId(dto.getId());
        
        validarPessoa(dto);
        
        Pessoa pessoa = pessoaMapper.toEntity(dto);
        try {
            pessoa = pessoaRepository.salvar(pessoa);
        } catch (Exception e) {
            verificarCpfDuplicado(e);
            throw e;
        }
        
        return pessoaMapper.toDTO(pessoa);
    }
    
    /**
     * Remove uma pessoa.
     * 
     * @param id ID da pessoa a ser removida
     * @throws EntityNotFoundException Se a pessoa não for encontrada
     */
    @Transactional
    public void remover(Long id) throws EntityNotFoundException {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        
        Optional<Pessoa> pessoaOpt = pessoaRepository.buscarPorId(id);
        if (!pessoaOpt.isPresent()) {
            throw new EntityNotFoundException("Pessoa não encontrada com o ID: " + id);
        }
        
        pessoaRepository.remover(id);
    }
    
    /**
     * Conta o total de pessoas cadastradas.
     * 
     * @return O número total de pessoas
     */
    public long contarTodas() {
        return pessoaRepository.contarTodas();
    }
    
    /**
     * Valida os dados de uma pessoa.
     * 
     * @param dto A pessoa a ser validada
     * @throws BusinessException Se houver erro de validação
     */
    private void validarPessoa(PessoaDTO dto) throws BusinessException {
        if (dto == null) {
            throw new BusinessException("Dados da pessoa são obrigatórios");
        }

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new BusinessException("Nome é obrigatório");
        }

        if (dto.getCpf() == null || dto.getCpf().trim().isEmpty()) {
            throw new BusinessException("CPF é obrigatório");
        }
        if (!isCpfValido(dto.getCpf())) {
            throw new BusinessException("CPF inválido");
        }

        if (dto.getDataNascimento() == null) {
            throw new BusinessException("Data de nascimento é obrigatória");
        }

        if (dto.getSexo() == null) {
            throw new BusinessException("Sexo é obrigatório");
        }
    }

    /**
     * Verifica se a exceção contém mensagem relacionada a CPF duplicado
     * 
     * @param e A exceção a ser verificada
     * @throws BusinessException Se for detectado CPF duplicado
     */
    private void verificarCpfDuplicado(Exception e) throws BusinessException {
        Throwable cause = e;
        while (cause != null) {
            String msg = cause.getMessage();
            if (msg != null && msg.toLowerCase().contains("cpf")) {
                throw new BusinessException("Já existe um usuário cadastrado com esse CPF.");
            }
            cause = cause.getCause();
        }
    }
    
    /**
     * Validação de CPF baseada no algoritmo oficial
     * 
     * @param cpf O CPF a ser validado
     * @return true se o CPF for válido, false caso contrário
     */
    private boolean isCpfValido(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;
        try {
            int d1 = 0, d2 = 0;
            for (int i = 0; i < 9; i++) {
                int digito = Character.getNumericValue(cpf.charAt(i));
                d1 += digito * (10 - i);
                d2 += digito * (11 - i);
            }
            int resto1 = d1 % 11;
            int dv1 = (resto1 < 2) ? 0 : 11 - resto1;
            d2 += dv1 * 2;
            int resto2 = d2 % 11;
            int dv2 = (resto2 < 2) ? 0 : 11 - resto2;
            return dv1 == Character.getNumericValue(cpf.charAt(9)) && dv2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }
}
