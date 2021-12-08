package src.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import src.models.Ativo;

public class ForcaBruta implements IConstrutorDePortifolio {

    public Portifolio ContruirPortifolio(List<Ativo> dados) {

        LinkedList<List<Double>> permutacoesPeso = this.PermutarPorcentagens(new HashMap<>(), dados.size(), 100);
        System.out.println(permutacoesPeso.size());
        // Gera portifolios
        List<Portifolio> lstPortifolios = new ArrayList<>(permutacoesPeso.size());
        do {
            List<Double> permutaDePeso = permutacoesPeso.removeFirst();
            Portifolio portifolio = new Portifolio();
            for (int i = 0; i < permutaDePeso.size(); i++) {
                double peso = permutaDePeso.get(i);
                Ativo ativo = dados.get(i);

                if (peso > 0)
                    portifolio.Add(ativo, peso);
            }

            lstPortifolios.add(portifolio);
        } while (permutacoesPeso.size()>0);

        Portifolio melhorPortifolio = 
            lstPortifolios
                .stream()
                .reduce(( melhor, p ) -> {
                    if (melhor != null)
                        return (melhor.getRiscoRetorno() > p.getRiscoRetorno()) ? p : melhor;
                    else
                        return p;
                }).get();

        return melhorPortifolio;
    }

    private LinkedList<List<Double>> PermutarPorcentagens( Map<String, List<List<Double>>> permutacoesAtuais, int quantidade, double max ){
        if (quantidade == 0)
            return new LinkedList<>();
        if (quantidade == 1){
            LinkedList<List<Double>> permutas = new LinkedList<>();
            List<Double> permuta = new ArrayList<>(1);
            permuta.add(max);
            permutas.add(permuta);
            
            return permutas;
        }
            
        LinkedList<List<Double>> lstPermutacoes = new LinkedList<List<Double>>();
        for (double i = 0; i <= max; i++) {
            var permutas = GetPermutarPorcentagens(permutacoesAtuais, quantidade - 1, max - i);
            if (quantidade == 1){
                List<Double> distribuicaoDePorcentagem = new ArrayList<>(quantidade);
                distribuicaoDePorcentagem.add(i);

                lstPermutacoes.add(distribuicaoDePorcentagem);
            }
            else{
                final double current = i;
                permutas.forEach( permuta -> {
                    List<Double> distribuicaoDePorcentagem = new ArrayList<>(quantidade);
                    distribuicaoDePorcentagem.add(current);
                    distribuicaoDePorcentagem.addAll(permuta);
                    //System.out.print(quantidade + ": ");
                    //System.out.println(permuta);

                    lstPermutacoes.add(distribuicaoDePorcentagem);
                });
            }

        }
        return lstPermutacoes;
    }

    private List<List<Double>> GetPermutarPorcentagens( Map<String, List<List<Double>>> permutacoesAtuais, int quantidade, double max ){
        if (permutacoesAtuais == null){
            permutacoesAtuais = new HashMap<>();
            return new LinkedList<>();
        }
        else{
            String key = quantidade + "_" + max;
            var atual = permutacoesAtuais.get(key);
            if ( atual == null ){
                var permutas = PermutarPorcentagens(permutacoesAtuais, quantidade, max);
                permutacoesAtuais.put(key, permutas);
                return permutas;
            }
            else{
                return atual;
            }
        }
    }

    
}
