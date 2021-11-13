package src;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import src.helpers.*;
import src.models.*;

public class App {

    public static final String arquivo;
    public static final ArquivoTextoLeitura leitura;

    static {
        arquivo = "./data.csv";
        leitura = new ArquivoTextoLeitura();
    }

    public static void main(String[] args) {
        List<AtivoRegistro> ativosRegistros = preencherVetorAtivoRegistros();

        Map<String, List<AtivoRegistro>> ativosRegistroDistintos = ativosRegistros.stream()
                .collect(Collectors.groupingBy(AtivoRegistro::getNome));

        List<Ativo> ativos = new ArrayList<Ativo>(ativosRegistroDistintos.size());
        Comparator<AtivoRegistro> comparadorPorData = Comparator.comparing( AtivoRegistro::getData );
        for (var entry : ativosRegistroDistintos.entrySet()) {
            Ativo ativo = new Ativo(entry.getKey());
            AtivoRegistro ativoRegistroMinData = entry.getValue().stream().min(comparadorPorData).get();
            AtivoRegistro ativoRegistroMaxData = entry.getValue().stream().max(comparadorPorData).get();
            ativo.setPrecoDeCompra(ativoRegistroMinData.getPreco());
            ativo.setPrecoDeVenda(ativoRegistroMaxData.getPreco());
            BigDecimal somaDividendos = entry.getValue().stream().map(x -> x.getDividendo()).reduce(BigDecimal.ZERO, BigDecimal::add);
            ativo.setAcumuloDeDividendos(somaDividendos);
            ativos.add(ativo);
        }
        
        for (var ativo : ativos) {
            System.out.println(ativo.calcRetornoEfetivo());
        }
        
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static List<AtivoRegistro> preencherVetorAtivoRegistros() {
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