package src.algorithms;

import java.util.List;
import java.util.Random;

import src.models.Ativo;
import src.models.Portifolio;

public class Aleatorio implements IConstrutorDePortifolio {

    @Override
    public Portifolio ContruirPortifolio(List<Ativo> dados) {
        Portifolio portifolio = new Portifolio();
        double max = 100;
        for (int i = 0; i < dados.size() && max > 0; i++) {
            Ativo ativo = dados.get(i);
            // se for o último pega o que sobrar
            double peso = (i == dados.size() - 1) ? max : GetRandom(0, max) ;
            max -= peso;

            portifolio.Add(ativo, peso);
        }

        return portifolio;
    }

    private double GetRandom(double min, double max){
        return new Random().nextInt((int)max + 1) + min;
    }
    
}
