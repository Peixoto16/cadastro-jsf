package com.teste.sinerji.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import com.teste.sinerji.domain.entity.Pessoa;

/**
 * Repositório para operações de persistência relacionadas à entidade Pessoa.
 * 
 * @author Teste Sinerji
 */
@Stateless
public class PessoaRepository {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Lista todas as pessoas cadastradas.
     * 
     * @return Lista de pessoas
     */
    public List<Pessoa> listarTodas() {
        return em.createQuery("SELECT p FROM Pessoa p ORDER BY p.nome", Pessoa.class)
                .getResultList();
    }
    
    /**
     * Busca uma pessoa por ID.
     * 
     * @param id O ID da pessoa
     * @return Optional contendo a pessoa, se encontrada
     */
    public Optional<Pessoa> buscarPorId(Long id) {
        try {
            Pessoa pessoa = em.find(Pessoa.class, id);
            return Optional.ofNullable(pessoa);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Busca pessoas por nome (busca parcial).
     * 
     * @param nome O nome ou parte do nome para busca
     * @return Lista de pessoas encontradas
     */
    public List<Pessoa> buscarPorNome(String nome) {
        TypedQuery<Pessoa> query = em.createQuery(
                "SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(:nome) ORDER BY p.nome", 
                Pessoa.class);
        query.setParameter("nome", "%" + nome + "%");
        return query.getResultList();
    }
    
    /**
     * Salva ou atualiza uma pessoa.
     * 
     * @param pessoa A pessoa a ser salva ou atualizada
     * @return A pessoa persistida
     */
    public Pessoa salvar(Pessoa pessoa) {
        if (pessoa.getId() == null) {
            em.persist(pessoa);
            return pessoa;
        } else {
            return em.merge(pessoa);
        }
    }
    
    /**
     * Remove uma pessoa.
     * 
     * @param id O ID da pessoa a ser removida
     */
    public void remover(Long id) {
        Pessoa pessoa = em.getReference(Pessoa.class, id);
        em.remove(pessoa);
    }
    
    /**
     * Conta o total de pessoas cadastradas.
     * 
     * @return O número total de pessoas
     */
    public long contarTodas() {
        return em.createQuery("SELECT COUNT(p) FROM Pessoa p", Long.class)
                .getSingleResult();
    }
}
