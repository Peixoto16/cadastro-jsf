package com.teste.sinerji.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teste.sinerji.application.dto.PessoaDTO;
import com.teste.sinerji.application.mapper.PessoaMapper;
import com.teste.sinerji.domain.entity.Pessoa;
import com.teste.sinerji.domain.enums.Sexo;
import com.teste.sinerji.infrastructure.repository.PessoaRepository;
import com.teste.sinerji.shared.exception.BusinessException;
import com.teste.sinerji.shared.exception.EntityNotFoundException;

/**
 * Testes unitários para o PessoaService.
 * Foco em validações e regras de negócio.
 */
@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private PessoaMapper pessoaMapper;

    @InjectMocks
    private PessoaService pessoaService;

    private PessoaDTO pessoaDTO;
    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        pessoaDTO = new PessoaDTO();
        pessoaDTO.setId(1L);
        pessoaDTO.setNome("João Silva");
        pessoaDTO.setCpf("529.982.247-25"); // CPF válido
        pessoaDTO.setDataNascimento(criarData(1990, 1, 1));
        pessoaDTO.setSexo(Sexo.M);

        pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João Silva");
        pessoa.setCpf("529.982.247-25");
        pessoa.setDataNascimento(criarData(1990, 1, 1));
        pessoa.setSexo(Sexo.M);
    }

    @Test
    @DisplayName("Deve listar todas as pessoas")
    void deveListarTodasAsPessoas() {
        List<Pessoa> pessoas = Arrays.asList(pessoa);
        List<PessoaDTO> pessoasDTO = Arrays.asList(pessoaDTO);
        
        when(pessoaRepository.listarTodas()).thenReturn(pessoas);
        when(pessoaMapper.toDTOList(pessoas)).thenReturn(pessoasDTO);

        List<PessoaDTO> resultado = pessoaService.listarTodas();

        assertEquals(1, resultado.size());
        assertEquals(pessoaDTO, resultado.get(0));
        verify(pessoaRepository).listarTodas();
        verify(pessoaMapper).toDTOList(pessoas);
    }

    @Test
    @DisplayName("Deve buscar pessoa por ID")
    void deveBuscarPessoaPorId() throws EntityNotFoundException {
        when(pessoaRepository.buscarPorId(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaMapper.toDTO(pessoa)).thenReturn(pessoaDTO);

        PessoaDTO resultado = pessoaService.buscarPorId(1L);
        assertEquals(pessoaDTO, resultado);
        verify(pessoaRepository).buscarPorId(1L);
        verify(pessoaMapper).toDTO(pessoa);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pessoa não encontrada por ID")
    void deveLancarExcecaoQuandoPessoaNaoEncontradaPorId() {
        when(pessoaRepository.buscarPorId(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pessoaService.buscarPorId(999L));
        verify(pessoaRepository).buscarPorId(999L);
    }

    @Test
    @DisplayName("Deve salvar pessoa válida")
    void deveSalvarPessoaValida() throws BusinessException {
        PessoaDTO novaPessoaDTO = new PessoaDTO();
        novaPessoaDTO.setNome("Maria Santos");
        novaPessoaDTO.setCpf("52998224725"); // CPF válido
        novaPessoaDTO.setDataNascimento(criarData(1995, 5, 15));
        novaPessoaDTO.setSexo(Sexo.F);

        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome("Maria Santos");
        novaPessoa.setCpf("52998224725");
        novaPessoa.setDataNascimento(criarData(1995, 5, 15));
        novaPessoa.setSexo(Sexo.F);

        Pessoa pessoaSalva = new Pessoa();
        pessoaSalva.setId(2L);
        pessoaSalva.setNome("Maria Santos");
        pessoaSalva.setCpf("52998224725");
        pessoaSalva.setDataNascimento(criarData(1995, 5, 15));
        pessoaSalva.setSexo(Sexo.F);

        PessoaDTO pessoaSalvaDTO = new PessoaDTO();
        pessoaSalvaDTO.setId(2L);
        pessoaSalvaDTO.setNome("Maria Santos");
        pessoaSalvaDTO.setCpf("52998224725");
        pessoaSalvaDTO.setDataNascimento(criarData(1995, 5, 15));
        pessoaSalvaDTO.setSexo(Sexo.F);

        when(pessoaMapper.toEntity(novaPessoaDTO)).thenReturn(novaPessoa);
        when(pessoaRepository.salvar(novaPessoa)).thenReturn(pessoaSalva);
        when(pessoaMapper.toDTO(pessoaSalva)).thenReturn(pessoaSalvaDTO);

        PessoaDTO resultado = pessoaService.salvar(novaPessoaDTO);

        assertEquals(2L, resultado.getId());
        assertEquals("Maria Santos", resultado.getNome());
        verify(pessoaRepository).salvar(novaPessoa);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar pessoa com nome vazio")
    void deveLancarExcecaoAoSalvarPessoaComNomeVazio() {
        PessoaDTO pessoaInvalida = new PessoaDTO();
        pessoaInvalida.setCpf("171.827.200-17");
        pessoaInvalida.setDataNascimento(criarData(1995, 5, 15));
        pessoaInvalida.setSexo(Sexo.F);
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> pessoaService.salvar(pessoaInvalida));
        assertEquals("Nome é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar pessoa com CPF inválido")
    void deveLancarExcecaoAoSalvarPessoaComCpfInvalido() {
        PessoaDTO pessoaInvalida = new PessoaDTO();
        pessoaInvalida.setNome("João Silva");
        pessoaInvalida.setCpf("111.111.111-11"); // CPF inválido
        pessoaInvalida.setDataNascimento(criarData(1990, 1, 1));
        pessoaInvalida.setSexo(Sexo.M);

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> pessoaService.salvar(pessoaInvalida));
        assertEquals("CPF inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar pessoa com data de nascimento nula")
    void deveLancarExcecaoAoSalvarPessoaComDataNascimentoNula() {
        PessoaDTO pessoaInvalida = new PessoaDTO();
        pessoaInvalida.setNome("João Silva");
        pessoaInvalida.setCpf("529.982.247-25");
        pessoaInvalida.setSexo(Sexo.M);
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> pessoaService.salvar(pessoaInvalida));
        assertEquals("Data de nascimento é obrigatória", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar pessoa com sexo nulo")
    void deveLancarExcecaoAoSalvarPessoaComSexoNulo() {
        PessoaDTO pessoaInvalida = new PessoaDTO();
        pessoaInvalida.setNome("João Silva");
        pessoaInvalida.setCpf("529.982.247-25");
        pessoaInvalida.setDataNascimento(criarData(1990, 1, 1));
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> pessoaService.salvar(pessoaInvalida));
        assertEquals("Sexo é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar pessoa existente")
    void deveAtualizarPessoaExistente() throws EntityNotFoundException, BusinessException {
        when(pessoaRepository.buscarPorId(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaMapper.toEntity(pessoaDTO)).thenReturn(pessoa);
        when(pessoaRepository.salvar(pessoa)).thenReturn(pessoa);
        when(pessoaMapper.toDTO(pessoa)).thenReturn(pessoaDTO);
        PessoaDTO resultado = pessoaService.atualizar(pessoaDTO);
        assertEquals(pessoaDTO, resultado);
        verify(pessoaRepository).buscarPorId(1L);
        verify(pessoaRepository).salvar(pessoa);
    }

    @Test
    @DisplayName("Deve remover pessoa existente")
    void deveRemoverPessoaExistente() throws EntityNotFoundException {
        when(pessoaRepository.buscarPorId(1L)).thenReturn(Optional.of(pessoa));
        pessoaService.remover(1L);
        verify(pessoaRepository).buscarPorId(1L);
        verify(pessoaRepository).remover(1L);
    }

    @Test
    @DisplayName("Deve validar CPF correto")
    void deveValidarCpfCorreto() throws Exception {
        java.lang.reflect.Method method = PessoaService.class.getDeclaredMethod("isCpfValido", String.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(pessoaService, "11144477735"));
        assertTrue((Boolean) method.invoke(pessoaService, "52998224725"));
        assertTrue((Boolean) method.invoke(pessoaService, "52998224725"));
    }

    @Test
    @DisplayName("Deve invalidar CPF incorreto")
    void deveInvalidarCpfIncorreto() throws Exception {
        // Arrange - usando reflexão para acessar método privado
        java.lang.reflect.Method method = PessoaService.class.getDeclaredMethod("isCpfValido", String.class);
        method.setAccessible(true);

        assertFalse((Boolean) method.invoke(pessoaService, "111.111.111-11"));
        assertFalse((Boolean) method.invoke(pessoaService, "123.456.789-10"));
        assertFalse((Boolean) method.invoke(pessoaService, ""));
        assertFalse((Boolean) method.invoke(pessoaService, (Object) null));
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
