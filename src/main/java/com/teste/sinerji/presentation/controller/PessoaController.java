package com.teste.sinerji.presentation.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.teste.sinerji.application.dto.EnderecoDTO;
import com.teste.sinerji.application.dto.PessoaDTO;
import com.teste.sinerji.application.dto.ViaCepDTO;
import com.teste.sinerji.application.service.CepService;
import com.teste.sinerji.application.service.PessoaService;
import com.teste.sinerji.domain.enums.Estado;
import com.teste.sinerji.domain.enums.Sexo;
import com.teste.sinerji.shared.exception.BusinessException;
import com.teste.sinerji.shared.exception.EntityNotFoundException;

import lombok.Getter;
import lombok.Setter;

/**
 * Controller para operações relacionadas à Pessoa.
 * 
 * @author Teste Sinerji
 */
@Named
@ViewScoped
public class PessoaController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private PessoaService pessoaService;
    
    @Inject
    private CepService cepService;
    
    @Getter @Setter
    private PessoaDTO pessoa;
    
    @Getter @Setter
    private EnderecoDTO endereco;
    
    @Getter @Setter
    private List<PessoaDTO> pessoas;
    
    @Getter @Setter
    private List<PessoaDTO> pessoasFiltradas;
    
    // Filtros
    @Getter @Setter
    private String filtroNome;
    
    @Getter @Setter
    private String filtroCidade;
    
    @Getter @Setter
    private Estado filtroEstado;
    
    @Getter @Setter
    private Sexo filtroSexo;
    
    @Getter @Setter
    private boolean modoEdicao;
    
    @Getter @Setter
    private boolean modoEdicaoEndereco;

    @Getter
    private BarChartModel pessoasPorEstadoModel;
    
    @PostConstruct
    public void init() {
        novaPessoa();
        carregarPessoas();
        limparFiltros();
        criarGraficoPessoasPorEstado();
    }
    
    /**
     * Carrega todas as pessoas.
     */
    private void carregarPessoas() {
        try {
            pessoas = pessoaService.listarTodas();
            pessoasFiltradas = new ArrayList<>(pessoas);
            // Atualiza o gráfico quando carregar pessoas
            criarGraficoPessoasPorEstado();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar pessoas: " + e.getMessage());
            pessoas = new ArrayList<>();
            pessoasFiltradas = new ArrayList<>();
        }
    }
    
    /**
     * Prepara o formulário para nova pessoa.
     */
    public void novaPessoa() {
        limparFormulario();
        modoEdicao = false;
    }
    
    /**
     * Prepara o formulário para edição de pessoa.
     * 
     * @param pessoa A pessoa a ser editada
     */
    public void editarPessoa(PessoaDTO pessoa) {
        this.pessoa = pessoa;
        modoEdicao = true;
    }
    
    /**
     * Salva uma pessoa (nova ou editada).
     */
    public void salvarPessoa() {
        try {
            if (modoEdicao) {
                pessoaService.atualizar(pessoa);
                adicionarMensagemSucesso("Pessoa atualizada com sucesso!");
                limparFormulario();
                carregarPessoas();
            } else {
                pessoaService.salvar(pessoa);
                adicionarMensagemSucesso("Pessoa cadastrada com sucesso! Agora cadastre o endereço.");
                carregarPessoas(); // Atualiza a lista imediatamente
                modoEdicao = true;
                modoEdicaoEndereco = true;
                // Não limpar formulário, não sair da tela
                return;
            }
        } catch (BusinessException e) {
            adicionarMensagemErro(e.getMessage());
        } catch (EntityNotFoundException e) {
            adicionarMensagemErro(e.getMessage());
        } catch (Exception e) {
            adicionarMensagemErro("Erro inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Remove uma pessoa.
     * 
     * @param pessoa A pessoa a ser removida
     */
    public void removerPessoa(PessoaDTO pessoa) {
        try {
            pessoaService.remover(pessoa.getId());
            adicionarMensagemSucesso("Pessoa removida com sucesso!");
            carregarPessoas();
            
        } catch (EntityNotFoundException e) {
            adicionarMensagemErro(e.getMessage());
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao remover pessoa: " + e.getMessage());
        }
    }
    
    /**
     * Prepara o formulário para novo endereço.
     */
    public void novoEndereco() {
        if (pessoa == null || pessoa.getId() == null) {
            adicionarMensagemErro("Cadastre e salve a pessoa antes de adicionar um endereço.");
            return;
        }
        endereco = new EnderecoDTO();
        endereco.setPessoaId(pessoa.getId());
        modoEdicaoEndereco = false;
    }
    
    /**
     * Prepara o formulário para edição de endereço.
     * 
     * @param endereco O endereço a ser editado
     */
    public void editarEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
        modoEdicaoEndereco = true;
    }
    
    /**
     * Remove um endereço da lista.
     * 
     * @param endereco O endereço a ser removido
     */
    public void removerEndereco(EnderecoDTO endereco) {
        pessoa.getEnderecos().remove(endereco);
    }
    
    /**
     * Adiciona ou atualiza um endereço na lista.
     */
    public void salvarEndereco() {
        try {
            if (pessoa == null || pessoa.getId() == null) {
                adicionarMensagemErro("Cadastre e salve a pessoa antes de adicionar um endereço.");
                return;
            }
            if (!modoEdicaoEndereco) {
                pessoa.getEnderecos().add(endereco);
            }
            
            endereco = new EnderecoDTO();
            endereco.setPessoaId(pessoa.getId());
            
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao salvar endereço: " + e.getMessage());
        }
    }
    
    /**
     * Filtra a lista de pessoas com base nos critérios definidos.
     */
    public void filtrarPessoas() {
        try {
            pessoasFiltradas = pessoas.stream()
                .filter(p -> filtroNome == null || filtroNome.isEmpty() || 
                         (p.getNome() != null && p.getNome().toLowerCase().contains(filtroNome.toLowerCase())))
                .filter(p -> filtroSexo == null || 
                         (p.getSexo() != null && p.getSexo().equals(filtroSexo)))
                .filter(p -> {
                    if (filtroCidade == null || filtroCidade.isEmpty()) {
                        return true;
                    }
                    if (p.getEnderecos() == null || p.getEnderecos().isEmpty()) {
                        return false;
                    }
                    return p.getEnderecos().stream()
                        .anyMatch(e -> e.getCidade() != null && 
                                 e.getCidade().toLowerCase().contains(filtroCidade.toLowerCase()));
                })
                .filter(p -> {
                    if (filtroEstado == null) {
                        return true;
                    }
                    if (p.getEnderecos() == null || p.getEnderecos().isEmpty()) {
                        return false;
                    }
                    return p.getEnderecos().stream()
                        .anyMatch(e -> e.getEstado() != null && e.getEstado().equals(filtroEstado));
                })
                .collect(Collectors.toList());

            // Atualiza o gráfico com base nos dados filtrados
            criarGraficoPessoasPorEstado();

            adicionarMensagemSucesso("Filtro aplicado com sucesso. " + 
                                    pessoasFiltradas.size() + " pessoa(s) encontrada(s).");
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao filtrar pessoas: " + e.getMessage());
        }
    }
    
    /**
     * Limpa o filtro de pesquisa (compatibilidade com versão antiga).
     */
    public void limparFiltro() {
        limparFiltros();
    }
    
    /**
     * Limpa todos os filtros e restaura a lista completa.
     */
    public void limparFiltros() {
        filtroNome = null;
        filtroCidade = null;
        filtroEstado = null;
        filtroSexo = null;
        
        if (pessoas != null) {
            pessoasFiltradas = new ArrayList<>(pessoas);
        } else {
            pessoasFiltradas = new ArrayList<>();
        }
    }
    
    /**
     * Cancela a operação atual e limpa o formulário.
     */
    public void cancelar() {
        limparFormulario();
    }
    
    /**
     * Limpa o formulário.
     */
    private void limparFormulario() {
        pessoa = new PessoaDTO();
        pessoa.setEnderecos(new ArrayList<>());
        endereco = new EnderecoDTO();
        modoEdicao = false;
        modoEdicaoEndereco = false;
    }
    
    /**
     * Adiciona mensagem de sucesso.
     * 
     * @param mensagem A mensagem
     */
    private void adicionarMensagemSucesso(String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem));
    }
    
    /**
     * Adiciona mensagem de erro.
     * 
     * @param mensagem A mensagem
     */
    private void adicionarMensagemErro(String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", mensagem));
    }
    
    /**
     * Retorna a lista de sexos para o select.
     * 
     * @return Array de sexos
     */
    public Sexo[] getSexos() {
        return Sexo.values();
    }
    
    /**
     * Retorna a lista de estados para o select.
     * 
     * @return Array de estados
     */
    public Estado[] getEstados() {
        return Estado.values();
    }

    /**
     * Retorna a quantidade de aniversariantes do mês atual
     */
    public int getAniversariantesDoMes() {
        int count = 0;
        java.util.Calendar hoje = java.util.Calendar.getInstance();
        int mesAtual = hoje.get(java.util.Calendar.MONTH) + 1; // +1 porque Calendar.MONTH é 0-based
        
        for (PessoaDTO pessoa : pessoas) {
            if (pessoa.getDataNascimento() != null) {
                java.util.Calendar nascimento = java.util.Calendar.getInstance();
                nascimento.setTime(pessoa.getDataNascimento());
                if (nascimento.get(java.util.Calendar.MONTH) + 1 == mesAtual) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Retorna a porcentagem de homens cadastrados
     */
    public double getPorcentagemHomens() {
        if (pessoas == null || pessoas.isEmpty()) return 0.0;
        long totalHomens = pessoas.stream()
            .filter(p -> p.getSexo() != null && p.getSexo().name().equalsIgnoreCase("M"))
            .count();
        return (totalHomens * 100.0) / pessoas.size();
    }

    /**
     * Retorna a porcentagem de mulheres cadastradas
     */
    public double getPorcentagemMulheres() {
        if (pessoas == null || pessoas.isEmpty()) return 0.0;
        long totalMulheres = pessoas.stream()
            .filter(p -> p.getSexo() != null && p.getSexo().name().equalsIgnoreCase("F"))
            .count();
        return (totalMulheres * 100.0) / pessoas.size();
    }

    /**
     * Cria o gráfico de barras mostrando a quantidade de pessoas por estado.
     */
    public void criarGraficoPessoasPorEstado() {
        pessoasPorEstadoModel = new BarChartModel();
        ChartSeries serie = new ChartSeries();
        serie.setLabel("Pessoas");

        Map<String, Integer> contagemPorEstado = new HashMap<>();

        // Inicializa o mapa com todos os estados (mesmo os que não têm pessoas)
        for (Estado estado : Estado.values()) {
            contagemPorEstado.put(estado.getNome(), 0);
        }

        // Conta pessoas por estado na lista filtrada
        if (pessoasFiltradas != null) {
            for (PessoaDTO pessoa : pessoasFiltradas) {
                if (pessoa.getEnderecos() != null && !pessoa.getEnderecos().isEmpty()) {
                    Estado estado = pessoa.getEnderecos().get(0).getEstado();
                    if (estado != null) {
                        String nomeEstado = estado.getNome();
                        contagemPorEstado.put(nomeEstado, contagemPorEstado.getOrDefault(nomeEstado, 0) + 1);
                    }
                }
            }
        }

        // Adiciona dados ao gráfico (apenas estados com pessoas)
        for (Map.Entry<String, Integer> entry : contagemPorEstado.entrySet()) {
            // Só adiciona estados que têm pelo menos uma pessoa
            if (entry.getValue() > 0) {
                serie.set(entry.getKey(), entry.getValue());
            }
        }

        // Configura o dataset
        pessoasPorEstadoModel.addSeries(serie);
        pessoasPorEstadoModel.setTitle("Pessoas por Estado");
        pessoasPorEstadoModel.setLegendPosition("ne");
        pessoasPorEstadoModel.setShowPointLabels(true);
    }


    /**
     * Busca endereço pelo CEP e preenche os campos do formulário.
     */
    public void buscarEnderecoPorCep() {
        try {
            if (endereco == null || endereco.getCep() == null || endereco.getCep().trim().isEmpty()) {
                return;
            }
            
            ViaCepDTO viaCepDTO = cepService.consultarCep(endereco.getCep());
            
            // Preenche os campos do endereço com os dados retornados pela API
            endereco.setLogradouro(viaCepDTO.getLogradouro());
            endereco.setCidade(viaCepDTO.getCidade());
            
            // Converte a UF para o enum Estado
            try {
                Estado estado = Estado.valueOf(viaCepDTO.getEstado());
                endereco.setEstado(estado);
            } catch (IllegalArgumentException e) {
                // Se não conseguir converter, deixa o estado em branco
                adicionarMensagemErro("Estado não reconhecido: " + viaCepDTO.getEstado());
            }
            
            adicionarMensagemSucesso("Endereço encontrado com sucesso!");
            
        } catch (BusinessException e) {
            adicionarMensagemErro(e.getMessage());
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao buscar CEP: " + e.getMessage());
        }
    }
}
