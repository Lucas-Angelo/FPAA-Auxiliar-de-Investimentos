package src.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Guloso<T> implements IConstrutorDePortifolio<T>{
    private Function<T, Double> criterioGuloso;

    public Guloso(Function<T, Double> criterioGulo){
        this.criterioGuloso = criterioGulo;
    }

    @Override
    public List<T> ContruirPortifolio(List<T> dados, int maximo) {
        // clonar vetor de dados
        List<T> ordenadoPorCriterio = new ArrayList<>(dados);
        // ordena o vetor de dados pelo critério guloso
        ordenadoPorCriterio.sort((a,b)->{
            return (int)(this.criterioGuloso.apply(b) - this.criterioGuloso.apply(a));
        });
        // controi portifolio a partir de dados ordenados
        int quantidade = maximo > ordenadoPorCriterio.size() ? ordenadoPorCriterio.size() : maximo ;
        List<T> portifolio = ordenadoPorCriterio.subList(0, quantidade);

        return portifolio;
    }

}
