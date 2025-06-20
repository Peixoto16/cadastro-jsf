package com.teste.sinerji.application.service;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import jakarta.ejb.Stateless;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.sinerji.application.dto.ViaCepDTO;
import com.teste.sinerji.shared.exception.BusinessException;

/**
 * Serviço para consulta de CEP via API ViaCEP.
 */
@Stateless
public class CepService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/%s/json/";
    
    // Cache simples para evitar consultas repetidas ao mesmo CEP
    private final Map<String, ViaCepDTO> cepCache = new HashMap<>();
    
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Consulta um CEP na API ViaCEP.
     * 
     * @param cep O CEP a ser consultado (pode conter formatação)
     * @return DTO com os dados do endereço
     * @throws BusinessException Se ocorrer erro na consulta ou CEP inválido
     */
    public ViaCepDTO consultarCep(String cep) throws BusinessException {
        if (cep == null || cep.trim().isEmpty()) {
            throw new BusinessException("CEP não informado");
        }
        
        // Remove caracteres não numéricos
        String cepLimpo = cep.replaceAll("\\D", "");
        
        // Valida o formato do CEP (8 dígitos)
        if (cepLimpo.length() != 8) {
            throw new BusinessException("CEP deve conter 8 dígitos");
        }
        
        // Verifica se o CEP já está em cache
        if (cepCache.containsKey(cepLimpo)) {
            return cepCache.get(cepLimpo);
        }
        
        try {
            // Formata a URL com o CEP
            String url = String.format(VIA_CEP_URL, cepLimpo);
            
            // Cria a requisição HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .build();
            
            // Executa a requisição
            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());
            
            // Verifica se a requisição foi bem-sucedida (código 200)
            if (response.statusCode() != 200) {
                throw new BusinessException("Erro ao consultar CEP: " + response.statusCode());
            }
            
            // Converte a resposta JSON para objeto
            ViaCepDTO viaCepDTO = objectMapper.readValue(response.body(), ViaCepDTO.class);
            
            // Verifica se o CEP foi encontrado
            if (viaCepDTO.isErro()) {
                throw new BusinessException("CEP não encontrado");
            }
            
            // Armazena no cache
            cepCache.put(cepLimpo, viaCepDTO);
            
            return viaCepDTO;
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao consultar CEP: " + e.getMessage());
        }
    }
    
    /**
     * Limpa o cache de CEPs.
     */
    public void limparCache() {
        cepCache.clear();
    }
}
