package src;

import java.math.*;
import java.util.*;
import java.util.stream.*;

import src.helpers.*;
import src.models.*;

public class App {

    public static final String arquivo;
    public static final ArquivoTextoLeitura leitura;

    public static List<Ativo> ativos;

    static {
        arquivo = "./data.csv";
        leitura = new ArquivoTextoLeitura();
    }

    public static void main(String[] args) {
        List<AtivoRegistro> ativosRegistros = preencherVetorAtivoRegistros();

        Map<String, List<AtivoRegistro>> ativosRegistroDistintos = ativosRegistros.stream()
                .collect(Collectors.groupingBy(AtivoRegistro::getNome));

        ativos = new ArrayList<Ativo>(ativosRegistroDistintos.size());

        Comparator<AtivoRegistro> comparadorPorData = Comparator.comparing( AtivoRegistro::getData );
        for (var entry : ativosRegistroDistintos.entrySet()) {
            Ativo ativo = new Ativo(entry.getKey());

            AtivoRegistro ativoRegistroMinData = entry.getValue().stream().min(comparadorPorData).get();
            AtivoRegistro ativoRegistroMaxData = entry.getValue().stream().max(comparadorPorData).get();
            ativo.setPrecoDeCompra(ativoRegistroMinData.getPreco());
            ativo.setPrecoDeVenda(ativoRegistroMaxData.getPreco());

            BigDecimal somaDividendos = entry.getValue().stream().map(x -> x.getDividendo()).reduce(BigDecimal.ZERO, BigDecimal::add);
            ativo.setAcumuloDeDividendos(somaDividendos);
            ativo.calcRetornoEfetivo();

            BigDecimal[] somaComContagem
            = entry.getValue().stream().map(x -> x.getPreco())
            .filter(bd -> bd != null)
            .map(bd -> new BigDecimal[]{bd, BigDecimal.ONE})
            .reduce((a, b) -> new BigDecimal[]{a[0].add(b[0]), a[1].add(BigDecimal.ONE)})
            .get();
            BigDecimal mediaPreco = somaComContagem[0].divide(somaComContagem[1], MathContext.DECIMAL32);
            ativo.setMediaPreco(mediaPreco);
            
            ArrayList<BigDecimal> somaVariacaoArr = new ArrayList<BigDecimal>();
            for(int z=0; z<entry.getValue().size(); z++) {
                if(z!=0) {
                    BigDecimal calc = new BigDecimal(entry.getValue().get(z).getPreco().subtract(entry.getValue().get(z-1).getPreco()).toString());
                    somaVariacaoArr.add(calc);
                }
            }
            BigDecimal desvioPadrao = Stdev.stddev(somaVariacaoArr, true, MathContext.DECIMAL32);
            ativo.setVolatividade(desvioPadrao);

            ativos.add(ativo);
        }
        
        for (var ativo : ativos) {
            System.out.println(ativo);
        }
        
    }

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
    
}