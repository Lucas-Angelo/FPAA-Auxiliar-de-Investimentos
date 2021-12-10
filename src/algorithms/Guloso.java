package src.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import src.models.Ativo;
import src.models.Portifolio;

public class Guloso implements IConstrutorDePortifolio{
    private Function<Ativo, Double> criterioGuloso;

    public Guloso(Function<Ativo, Double> criterioGulo){
        this.criterioGuloso = criterioGulo;
    }

    @Override
    public Portifolio ContruirPortifolio(List<Ativo> dados) {
        // clonar vetor de dados
        List<Ativo> ordenadoPorCriterio = new ArrayList<>(dados);
        // ordena o vetor de dados pelo critério guloso
        ordenadoPorCriterio.sort((a,b)->{
            return (int)(this.criterioGuloso.apply(a) - this.criterioGuloso.apply(b));
        });
        // controi portifolio a partir de dados ordenados
        Portifolio portifolio = new Portifolio();
        boolean encontradaMelhorSolucao = false;
        // itera os ativos gerando um portifolio novo, até que não haja mais melhoria em uma mudança de portifólio
        for (int i = 0; i < ordenadoPorCriterio.size() && !encontradaMelhorSolucao; i++) {
            Ativo ativoAnalisado = ordenadoPorCriterio.get(i);
            Portifolio candidato = GeraPortifolioAdaptado(portifolio, ativoAnalisado, 1, ordenadoPorCriterio.get(0));
            
            if (portifolio.getAtivos().size() == 0 || portifolio.getRiscoRetorno() > candidato.getRiscoRetorno())
                portifolio = candidato;
            else
                encontradaMelhorSolucao = true;
        }

        return portifolio;
    }

    private Portifolio GeraPortifolioAdaptado( Portifolio portifolioAntigo, Ativo ativoNovo, double pesoNovo, Ativo ativoDeTroca ){
        if (portifolioAntigo.getAtivos().size()==0){
            Portifolio novoPortifolio = new Portifolio();
            novoPortifolio.Add(ativoNovo, 100);
            return novoPortifolio;
        }
        
        double pesoDeTroca = portifolioAntigo.getAtivos().get(ativoDeTroca);
        if (pesoDeTroca > pesoNovo){
            final double pesoNovoAtivoDeTroca = pesoDeTroca - pesoNovo;
            Portifolio novoPortifolio = new Portifolio();
            portifolioAntigo.getAtivos().entrySet().forEach(entry->{
                double peso = entry.getValue();
                Ativo ativo = entry.getKey();

                if (ativo == ativoDeTroca)
                    novoPortifolio.Add(ativo, pesoNovoAtivoDeTroca);
                else
                    novoPortifolio.Add(ativo, peso);
            });
            novoPortifolio.Add(ativoNovo, pesoNovo);

            return novoPortifolio;
        }
        else
            return portifolioAntigo;
    }

}
