package src;

import java.io.File;
import java.math.*;
import java.util.*;
import java.util.stream.*;

import src.algorithms.Aleatorio;
import src.algorithms.ForcaBruta;
import src.algorithms.Guloso;
import src.algorithms.IConstrutorDePortifolio;
import src.helpers.*;
import src.models.*;

public class App {

    public static String arquivo;
    public static final ArquivoTextoLeitura leitura;
    public static Scanner sc;

    public static List<Ativo> ativos;

    static {
        sc = new Scanner(System.in);
        arquivo = "./data.csv";
        leitura = new ArquivoTextoLeitura();
    }

    /**
     * Função principal do programa que irá chamar mapeamento dos registros de ativos, chamar cálculo dos ativos e rodar os algoritmos
     */
    public static void main(String[] args) {
        System.out.println("Você deseja selecionar um arquivo data customizado? y/n");
        String res = sc.nextLine();
        File diretorio = new File("./data.csv");
        if(res.equals("y")) {
            System.out.println("Insira o diretório do arquivo data (Exemplo: C:\\Users\\Admin\\Downloads\\data.csv):");
            diretorio = new File(sc.nextLine());
        }
        arquivo = diretorio.toString();
        System.out.println("Arquivo data utilizado: " + arquivo);

        List<AtivoRegistro> ativosRegistros = preencherVetorAtivoRegistros(); // Leitura de dados do .csv

        Map<String, List<AtivoRegistro>> ativosRegistroDistintos = ativosRegistros.stream()
                .collect(Collectors.groupingBy(AtivoRegistro::getNome)); // Distingue os ativos pelo nome do registro do ativo

        mapeiaAtivosEEfetuaCalculos(ativosRegistroDistintos); // Faz os cálculos para cada ativo distinto pelo nome

        rodarAlgortimosPorCLI(); // Roda os algortimos por uma interface CLI
    }

    /**
     * Função responsável por mapear os registro dos ativos do arquivo data.csv e retornos para poder criar os ativos em si
     */
    private static List<AtivoRegistro> preencherVetorAtivoRegistros() {
        List<AtivoRegistro> ativos = new ArrayList<>();

        leitura.abrirArquivo(arquivo);

        leitura.ler();
        String linha = leitura.ler(); // Remove o cabecalho
        while (linha != null) {
            String[] dadosDaLinha = linha.split(",", 5); // Dividir os dados da linha (5 COLUNAS)

            if (linha != null)
                ativos.add(new AtivoRegistro(dadosDaLinha[0], dadosDaLinha[1], new BigDecimal(dadosDaLinha[2]),
                        new BigDecimal(dadosDaLinha[3]), new BigDecimal(dadosDaLinha[4])));

            linha = leitura.ler();
        }
        
        leitura.fecharArquivo();

        return ativos;
    }

    /**
     * Define os valores do ativo e efetua os cálculos de cada ativo por meio dos registros do arquivo data.csv
     * Define o Valor de Compra e Valor de Venda do ativo
     * Calcula o Acumulo de Dividendos do ativo
     * Calcula o Retorno Efetivo do ativo
     * Calcula a Média de Preço do ativo
     * Calcula a Volatividade (Desvio padrão) do ativo
     * Calcula o Risco Normalizado do ativo
     * Calcula o Risco Retorno do ativo
     */
    private static void mapeiaAtivosEEfetuaCalculos(Map<String, List<AtivoRegistro>> ativosRegistroDistintos) {
        ativos = new ArrayList<Ativo>(ativosRegistroDistintos.size());

        Comparator<AtivoRegistro> comparadorPorData = Comparator.comparing( AtivoRegistro::getData ); // Usado para comparar datas
        // Definir e calculos dados de cada um dos ativos neste for
        for (var entry : ativosRegistroDistintos.entrySet()) {
            Ativo ativo = new Ativo(entry.getKey());

            AtivoRegistro ativoRegistroMinData = entry.getValue().stream().min(comparadorPorData).get();
            AtivoRegistro ativoRegistroMaxData = entry.getValue().stream().max(comparadorPorData).get();
            // Foi julgado que o preço de compra é o do primeiro registro de ativo e preço de venda é o valor do último registro do ativo
            ativo.setPrecoDeCompra(ativoRegistroMinData.getPreco()); // Definindo Preço de Compra
            ativo.setPrecoDeVenda(ativoRegistroMaxData.getPreco()); // Definindo Preço de venda

            BigDecimal somaDividendos = entry.getValue().stream().map(x -> x.getDividendo()).reduce(BigDecimal.ZERO, BigDecimal::add);
            ativo.setAcumuloDeDividendos(somaDividendos); // Definindo Acumulo de Dividendos
            ativo.calcRetornoEfetivo(); // Definindo Retorno Efetivo

            BigDecimal[] somaComContagem
            = entry.getValue().stream().map(x -> x.getPreco())
            .filter(bd -> bd != null)
            .map(bd -> new BigDecimal[]{bd, BigDecimal.ONE})
            .reduce((a, b) -> new BigDecimal[]{a[0].add(b[0]), a[1].add(BigDecimal.ONE)})
            .get();
            BigDecimal mediaPreco = somaComContagem[0].divide(somaComContagem[1], MathContext.DECIMAL32);
            ativo.setMediaPreco(mediaPreco); // Definindo Média de Preço
            
            ArrayList<BigDecimal> somaVariacaoArr = new ArrayList<BigDecimal>();
            for(int z=0; z<entry.getValue().size(); z++) {
                if(z!=0) {
                    BigDecimal calc = new BigDecimal(entry.getValue().get(z).getPreco().subtract(entry.getValue().get(z-1).getPreco()).toString());
                    somaVariacaoArr.add(calc);
                }
            }
            BigDecimal desvioPadrao = Stdev.stddev(somaVariacaoArr, true, MathContext.DECIMAL32);
            ativo.setVolatividade(new BigDecimal(desvioPadrao.toString(), MathContext.DECIMAL32)); // Definindo Volatividade

            ativo.calcRiscoNormalizado(); // Calculando e definindo o Risco Normalizado 
            
            ativo.calcRiscoRetorno(); // Calculando e definindo o Risco Retorno

            if (ativo.getRetornoEfetivo().compareTo(BigDecimal.ZERO) > 0) // SE O RETORNO EFETIVO FOR NEGATIVO DESACARTAMOS POIS NÃO TEM MOTIVO DE USÁ-LO
                ativos.add(ativo); // Salvando Ativo preenchido e calculado
            }
    }

    /**
     * Solicita quais ativos deseja-se utilizar nos algortimos (RECOMENDÁVEL NO MÁXIMO 5 ATIVOS)
     * Inserir 'y' (yes) para adicionar o ativo
     * Roda os algoritmos de Força Bruta, Guloso e Aleatório.
     * Comunicação e retornos por command line interface.
     */
    private static void rodarAlgortimosPorCLI() {
        List<Ativo> ativosUsados = new ArrayList<Ativo>();
        
        for(int i=0; i<ativos.size(); i++) {
            System.out.println("\nVocê deseja adicionar o seguinte ativo?");
            System.out.println(ativos.get(i));
            System.out.println("Escreva y/n");
            String res = sc.nextLine();
            if(res.equals("y"))
                ativosUsados.add(ativos.get(i));
        }
        sc.close();

        System.out.println("\nAtivos que serão utilizados (Recomendado até 5): " + ativosUsados.size());
        for (var ativo : ativosUsados) {
            System.out.println(ativo);
        }

        if(ativosUsados.size()==0) // Deve utilizar no mínimo 1 ativo
            ativosUsados.add(ativos.get(0));
        
        System.out.println("\n\n\nPortifolio força bruta: ");
        var dateinic = new Date();
        IConstrutorDePortifolio portiller = new ForcaBruta();
        Portifolio portifolio = portiller.ContruirPortifolio(ativosUsados);
        System.out.println(portifolio);
        System.out.println("\n");
        System.out.println(dateinic);
        System.out.println(new Date());

        System.out.println("\n\n\nPortifolio Guloso: ");
        dateinic = new Date();
        portiller = new Guloso( ativo -> ativo.getRiscoRetorno().doubleValue() );
        portifolio = portiller.ContruirPortifolio(ativosUsados);
        System.out.println(portifolio);
        System.out.println("\n");
        System.out.println(dateinic);
        System.out.println(new Date());

        System.out.println("\n\n\nPortifolio Aleatório: ");
        dateinic = new Date();
        portiller = new Aleatorio();
        portifolio = portiller.ContruirPortifolio(ativosUsados);
        System.out.println(portifolio);
        System.out.println("\n");
        System.out.println(dateinic);
        System.out.println(new Date());
    }
    
}