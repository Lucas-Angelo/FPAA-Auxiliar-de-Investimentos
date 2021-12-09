package src.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import src.models.Ativo;

public class Guloso implements IConstrutorDePortifolio{
    private Function<Ativo, Double> criterioGuloso;

    public Guloso(Function<Ativo, Double> criterioGulo){
        this.criterioGuloso = criterioGulo;
    }

    @Override
    public Portifolio ContruirPortifolio(List<Ativo> dados) {
        int maximo = 5;
        // clonar vetor de dados
        List<Ativo> ordenadoPorCriterio = new ArrayList<>(dados);
        // ordena o vetor de dados pelo critério guloso
        ordenadoPorCriterio.sort((a,b)->{
            return (int)(this.criterioGuloso.apply(b) - this.criterioGuloso.apply(a));
        });
        // controi portifolio a partir de dados ordenados
        int quantidade = maximo > ordenadoPorCriterio.size() ? ordenadoPorCriterio.size() : maximo ;
        List<Ativo> portifolio = ordenadoPorCriterio.subList(0, quantidade);

        return null;
    }

}
