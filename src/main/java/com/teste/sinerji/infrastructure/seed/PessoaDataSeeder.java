package com.teste.sinerji.infrastructure.seed;

import com.teste.sinerji.domain.entity.Pessoa;
import com.teste.sinerji.domain.entity.Endereco;
import com.teste.sinerji.domain.enums.Estado;
import com.teste.sinerji.domain.enums.Sexo;
import com.teste.sinerji.infrastructure.repository.PessoaRepository;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.text.SimpleDateFormat;

@Singleton
@Startup
public class PessoaDataSeeder {

    @Inject
    private PessoaRepository pessoaRepository;

    @PostConstruct
    public void init() {
        if (pessoaRepository.contarTodas() == 0) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                // Pessoa 1
                Pessoa p1 = new Pessoa();
                p1.setNome("João Silva");
                p1.setCpf("347.337.210-21");
                p1.setSexo(Sexo.M);
                p1.setDataNascimento(sdf.parse("01/01/1990"));
                Endereco e1 = new Endereco();
                e1.setLogradouro("Praça da Sé");
                e1.setNumero(101);
                e1.setCidade("São Paulo");
                e1.setEstado(Estado.SP);
                e1.setCep("01001-000");
                p1.adicionarEndereco(e1);
                pessoaRepository.salvar(p1);

                // Pessoa 2
                Pessoa p2 = new Pessoa();
                p2.setNome("Maria Oliveira");
                p2.setCpf("333.899.330-77");
                p2.setSexo(Sexo.F);
                p2.setDataNascimento(sdf.parse("02/02/1992"));
                Endereco e2 = new Endereco();
                e2.setLogradouro("Avenida Rio Branco");
                e2.setNumero(202);
                e2.setCidade("Rio de Janeiro");
                e2.setEstado(Estado.RJ);
                e2.setCep("20040-002");
                p2.adicionarEndereco(e2);
                pessoaRepository.salvar(p2);

                // Pessoa 3
                Pessoa p3 = new Pessoa();
                p3.setNome("Carlos Pereira");
                p3.setCpf("813.839.480-38");
                p3.setSexo(Sexo.M);
                p3.setDataNascimento(sdf.parse("03/03/1988"));
                Endereco e3 = new Endereco();
                e3.setLogradouro("Avenida do Contorno");
                e3.setNumero(303);
                e3.setCidade("Belo Horizonte");
                e3.setEstado(Estado.MG);
                e3.setCep("30110-012");
                p3.adicionarEndereco(e3);
                pessoaRepository.salvar(p3);

                // Pessoa 4
                Pessoa p4 = new Pessoa();
                p4.setNome("Ana Souza");
                p4.setCpf("603.164.820-21");
                p4.setSexo(Sexo.F);
                p4.setDataNascimento(sdf.parse("04/04/1995"));
                Endereco e4 = new Endereco();
                e4.setLogradouro("Avenida da França");
                e4.setNumero(404);
                e4.setCidade("Salvador");
                e4.setEstado(Estado.BA);
                e4.setCep("40010-000");
                p4.adicionarEndereco(e4);
                pessoaRepository.salvar(p4);

                // Pessoa 5
                Pessoa p5 = new Pessoa();
                p5.setNome("Lucas Lima");
                p5.setCpf("545.072.120-06");
                p5.setSexo(Sexo.M);
                p5.setDataNascimento(sdf.parse("05/05/1993"));
                Endereco e5 = new Endereco();
                e5.setLogradouro("Rua Frei Vicente do Salvador");
                e5.setNumero(505);
                e5.setCidade("Recife");
                e5.setEstado(Estado.PE);
                e5.setCep("50010-030");
                p5.adicionarEndereco(e5);
                pessoaRepository.salvar(p5);

                // Pessoa 6
                Pessoa p6 = new Pessoa();
                p6.setNome("Fernanda Costa");
                p6.setCpf("757.842.950-71");
                p6.setSexo(Sexo.F);
                p6.setDataNascimento(sdf.parse("06/06/1991"));
                Endereco e6 = new Endereco();
                e6.setLogradouro("Rua Oto de Alencar");
                e6.setNumero(606);
                e6.setCidade("Fortaleza");
                e6.setEstado(Estado.CE);
                e6.setCep("60010-270");
                p6.adicionarEndereco(e6);
                pessoaRepository.salvar(p6);

                // Pessoa 7
                Pessoa p7 = new Pessoa();
                p7.setNome("Bruno Almeida");
                p7.setCpf("835.246.230-00");
                p7.setSexo(Sexo.M);
                p7.setDataNascimento(sdf.parse("07/07/1987"));
                Endereco e7 = new Endereco();
                e7.setLogradouro("Quadra SBN Quadra 1");
                e7.setNumero(707);
                e7.setCidade("Brasília");
                e7.setEstado(Estado.DF);
                e7.setCep("70040-010");
                p7.adicionarEndereco(e7);
                pessoaRepository.salvar(p7);

                // Pessoa 8
                Pessoa p8 = new Pessoa();
                p8.setNome("Juliana Martins");
                p8.setCpf("225.649.100-50");
                p8.setSexo(Sexo.F);
                p8.setDataNascimento(sdf.parse("08/08/1994"));
                Endereco e8 = new Endereco();
                e8.setLogradouro("Rua Desembargador Westphalen");
                e8.setNumero(808);
                e8.setCidade("Curitiba");
                e8.setEstado(Estado.PR);
                e8.setCep("80010-110");
                p8.adicionarEndereco(e8);
                pessoaRepository.salvar(p8);

                // Pessoa 9
                Pessoa p9 = new Pessoa();
                p9.setNome("Ricardo Mendes");
                p9.setCpf("473.230.830-95");
                p9.setSexo(Sexo.M);
                p9.setDataNascimento(sdf.parse("09/09/1990"));
                Endereco e9 = new Endereco();
                e9.setLogradouro("Praça Quinze de Novembro");
                e9.setNumero(909);
                e9.setCidade("Florianópolis");
                e9.setEstado(Estado.SC);
                e9.setCep("88010-400");
                p9.adicionarEndereco(e9);
                pessoaRepository.salvar(p9);

                // Pessoa 10
                Pessoa p10 = new Pessoa();
                p10.setNome("Patrícia Gomes");
                p10.setCpf("587.234.320-55");
                p10.setSexo(Sexo.F);
                p10.setDataNascimento(sdf.parse("10/10/1989"));
                Endereco e10 = new Endereco();
                e10.setLogradouro("Rua Coronel Fernando Machado");
                e10.setNumero(1010);
                e10.setCidade("Porto Alegre");
                e10.setEstado(Estado.RS);
                e10.setCep("90010-320");
                p10.adicionarEndereco(e10);
                pessoaRepository.salvar(p10);

                System.out.println("Seed de pessoas inserido com sucesso!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
