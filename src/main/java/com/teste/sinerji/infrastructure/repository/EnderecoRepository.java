package com.teste.sinerji.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import com.teste.sinerji.domain.entity.Endereco;

/**
 * Repositório para operações de persistência relacionadas à entidade Endereco.
 * 
 * @author Teste Sinerji
 */
@Stateless
public class EnderecoRepository {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Lista todos os endereços cadastrados.
     * 
     * @return Lista de endereços
     */
    public List<Endereco> listarTodos() {
        return em.createQuery("SELECT e FROM Endereco e ORDER BY e.cidade, e.logradouro", Endereco.class)
                .getResultList();
    }
    
    /**
     * Busca um endereço por ID.
     * 
     * @param id O ID do endereço
     * @return Optional contendo o endereço, se encontrado
     */
    public Optional<Endereco> buscarPorId(Long id) {
        try {
            Endereco endereco = em.find(Endereco.class, id);
            return Optional.ofNullable(endereco);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Lista todos os endereços de uma pessoa.
     * 
     * @param pessoaId O ID da pessoa
     * @return Lista de endereços da pessoa
     */
    public List<Endereco> listarPorPessoa(Long pessoaId) {
        TypedQuery<Endereco> query = em.createQuery(
                "SELECT e FROM Endereco e WHERE e.pessoa.id = :pessoaId ORDER BY e.cidade, e.logradouro", 
                Endereco.class);
        query.setParameter("pessoaId", pessoaId);
        return query.getResultList();
    }
    
    /**
     * Busca endereços por cidade (busca parcial).
     * 
     * @param cidade A cidade ou parte da cidade para busca
     * @return Lista de endereços encontrados
     */
    public List<Endereco> buscarPorCidade(String cidade) {
        TypedQuery<Endereco> query = em.createQuery(
                "SELECT e FROM Endereco e WHERE LOWER(e.cidade) LIKE LOWER(:cidade) ORDER BY e.logradouro", 
                Endereco.class);
        query.setParameter("cidade", "%" + cidade + "%");
        return query.getResultList();
    }
    
    /**
     * Salva ou atualiza um endereço.
     * 
     * @param endereco O endereço a ser salvo ou atualizado
     * @return O endereço persistido
     */
    public Endereco salvar(Endereco endereco) {
        if (endereco.getId() == null) {
            em.persist(endereco);
            return endereco;
        } else {
            return em.merge(endereco);
        }
    }
    
    /**
     * Remove um endereço.
     * 
     * @param id O ID do endereço a ser removido
     */
    public void remover(Long id) {
        Endereco endereco = em.getReference(Endereco.class, id);
        em.remove(endereco);
    }
    
    /**
     * Conta o total de endereços cadastrados.
     * 
     * @return O número total de endereços
     */
    public long contarTodos() {
        return em.createQuery("SELECT COUNT(e) FROM Endereco e", Long.class)
                .getSingleResult();
    }
}
