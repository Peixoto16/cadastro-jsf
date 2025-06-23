package com.teste.sinerji.application.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.teste.sinerji.application.dto.PessoaDTO;
import com.teste.sinerji.application.mapper.PessoaMapper;
import com.teste.sinerji.domain.enums.Sexo;
import com.teste.sinerji.infrastructure.repository.PessoaRepository;
import com.teste.sinerji.shared.exception.BusinessException;
import com.teste.sinerji.shared.exception.EntityNotFoundException;

/**
 * Testes de integração para o PessoaService.
 * Utiliza banco de dados H2 em memória para testar a integração entre serviço e repositório.
 */
class PessoaServiceIT {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private PessoaService pessoaService;
    private PessoaRepository pessoaRepository;
    private PessoaMapper pessoaMapper;

    @BeforeAll
    static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("TestePU");
    }

    @AfterAll
    static void tearDownClass() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        
        pessoaRepository = new PessoaRepository();
        setEntityManager(pessoaRepository, em);
        
        pessoaMapper = new PessoaMapper();
        
        pessoaService = new PessoaService();
        setPessoaRepository(pessoaService, pessoaRepository);
        setPessoaMapper(pessoaService, pessoaMapper);
        
        em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        
        if (em.isOpen()) {
            em.close();
        }
        if (em.isOpen()) {
            em.close();
        }
    }

    @Test
    @DisplayName("Deve salvar e recuperar pessoa")
    void deveSalvarERecuperarPessoa() throws BusinessException, EntityNotFoundException {
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.setNome("João da Silva");
        pessoaDTO.setCpf("529.982.247-25"); // CPF válido
        pessoaDTO.setDataNascimento(criarData(1990, 1, 1));
        pessoaDTO.setSexo(Sexo.M);

        PessoaDTO pessoaSalva = pessoaService.salvar(pessoaDTO);
        
        em.getTransaction().commit();
        em.getTransaction().begin();
        
        PessoaDTO pessoaRecuperada = pessoaService.buscarPorId(pessoaSalva.getId());

        assertNotNull(pessoaSalva.getId());
        assertEquals("João da Silva", pessoaRecuperada.getNome());
        assertEquals("529.982.247-25", pessoaRecuperada.getCpf());
        assertEquals(criarData(1990, 1, 1), pessoaRecuperada.getDataNascimento());
        assertEquals(Sexo.M, pessoaRecuperada.getSexo());
    }

    @Test
    @DisplayName("Deve listar todas as pessoas")
    void deveListarTodasAsPessoas() throws BusinessException {
        for (int i = 1; i <= 3; i++) {
            PessoaDTO pessoaDTO = new PessoaDTO();
            pessoaDTO.setNome("Pessoa " + i);
            pessoaDTO.setCpf("52998224" + i + "25"); // CPF válido com variação
            pessoaDTO.setDataNascimento(criarData(1990, i, i));
            pessoaDTO.setSexo(i % 2 == 0 ? Sexo.F : Sexo.M);
            
            pessoaService.salvar(pessoaDTO);
        }
        
        em.getTransaction().commit();
        em.getTransaction().begin();

        List<PessoaDTO> pessoas = pessoaService.listarTodas();
        assertEquals(3, pessoas.size());
    }

    @Test
    @DisplayName("Deve buscar pessoas por nome")
    void deveBuscarPessoasPorNome() throws BusinessException {
        PessoaDTO pessoa1 = new PessoaDTO();
        pessoa1.setNome("Maria Oliveira");
        pessoa1.setCpf("529.982.247-25");
        pessoa1.setDataNascimento(criarData(1990, 1, 1));
        pessoa1.setSexo(Sexo.F);
        pessoaService.salvar(pessoa1);
        
        PessoaDTO pessoa2 = new PessoaDTO();
        pessoa2.setNome("Maria Silva");
        pessoa2.setCpf("171.827.200-17");
        pessoa2.setDataNascimento(criarData(1985, 5, 10));
        pessoa2.setSexo(Sexo.F);
        pessoaService.salvar(pessoa2);
        
        PessoaDTO pessoa3 = new PessoaDTO();
        pessoa3.setNome("Ana Santos");
        pessoa3.setCpf("070.672.730-86");
        pessoa3.setDataNascimento(criarData(1995, 8, 15));
        pessoa3.setSexo(Sexo.F);
        pessoaService.salvar(pessoa3);
        
        em.getTransaction().commit();
        em.getTransaction().begin();

        List<PessoaDTO> pessoasSilva = pessoaService.buscarPorNome("Silva");
        assertEquals(1, pessoasSilva.size());
        assertTrue(pessoasSilva.stream().anyMatch(p -> p.getNome().equals("Maria Silva")));
    }

    @Test
    @DisplayName("Deve atualizar pessoa existente")
    void deveAtualizarPessoaExistente() throws BusinessException, EntityNotFoundException {
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.setNome("João Pereira");
        pessoaDTO.setCpf("070.672.730-86");
        pessoaDTO.setDataNascimento(criarData(1980, 10, 15));
        pessoaDTO.setSexo(Sexo.M);
        
        PessoaDTO pessoaSalva = pessoaService.salvar(pessoaDTO);
        
        em.getTransaction().commit();
        em.getTransaction().begin();
        
        pessoaSalva.setNome("Nome Atualizado");
        pessoaSalva.setDataNascimento(criarData(1990, 2, 2));

        pessoaService.atualizar(pessoaDTO);
        
        em.getTransaction().commit();
        em.getTransaction().begin();
        
        PessoaDTO pessoaRecuperada = pessoaService.buscarPorId(pessoaSalva.getId());

        assertEquals("Nome Atualizado", pessoaRecuperada.getNome());
        assertEquals(criarData(1990, 2, 2), pessoaRecuperada.getDataNascimento());
        assertEquals("529.982.247-25", pessoaRecuperada.getCpf());
    }

    @Test
    @DisplayName("Deve remover pessoa existente")
    void deveRemoverPessoaExistente() throws BusinessException, EntityNotFoundException {
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.setNome("Pessoa para Remover");
        pessoaDTO.setCpf("529.982.247-25");
        pessoaDTO.setDataNascimento(criarData(1990, 1, 1));
        pessoaDTO.setSexo(Sexo.M);
        
        PessoaDTO pessoaSalva = pessoaService.salvar(pessoaDTO);
        
        em.getTransaction().commit();
        em.getTransaction().begin();
        
        assertNotNull(pessoaService.buscarPorId(pessoaSalva.getId()));
        
        pessoaService.remover(pessoaSalva.getId());
        
        em.getTransaction().commit();
        em.getTransaction().begin();

        assertThrows(EntityNotFoundException.class, () -> pessoaService.buscarPorId(pessoaSalva.getId()));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar pessoa com CPF duplicado")
    void deveLancarExcecaoAoTentarSalvarPessoaComCpfDuplicado() throws BusinessException {
        PessoaDTO pessoa1 = new PessoaDTO();
        pessoa1.setNome("Pessoa 1");
        pessoa1.setCpf("529.982.247-25");
        pessoa1.setDataNascimento(criarData(1990, 1, 1));
        pessoa1.setSexo(Sexo.M);
        
        pessoaService.salvar(pessoa1);
        
        em.getTransaction().commit();
        em.getTransaction().begin();
        
        PessoaDTO pessoa2 = new PessoaDTO();
        pessoa2.setNome("Pessoa 2");
        pessoa2.setCpf("529.982.247-25"); // Mesmo CPF
        pessoa2.setDataNascimento(criarData(1995, 5, 5));
        pessoa2.setSexo(Sexo.F);

        assertThrows(Exception.class, () -> pessoaService.salvar(pessoa2));
    }
    
    private void setEntityManager(PessoaRepository repository, EntityManager entityManager) {
        try {
            java.lang.reflect.Field field = PessoaRepository.class.getDeclaredField("em");
            field.setAccessible(true);
            field.set(repository, entityManager);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar EntityManager no repositório", e);
        }
    }
    
    private void setPessoaRepository(PessoaService service, PessoaRepository repository) {
        try {
            java.lang.reflect.Field field = PessoaService.class.getDeclaredField("pessoaRepository");
            field.setAccessible(true);
            field.set(service, repository);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar PessoaRepository no serviço", e);
        }
    }
    
    private void setPessoaMapper(PessoaService service, PessoaMapper mapper) {
        try {
            java.lang.reflect.Field field = PessoaService.class.getDeclaredField("pessoaMapper");
            field.setAccessible(true);
            field.set(service, mapper);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar PessoaMapper no serviço", e);
        }
    }
    
    /**
     * Método auxiliar para criar uma data a partir de ano, mês e dia
     */
    private Date criarData(int ano, int mes, int dia) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(ano, mes - 1, dia, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
