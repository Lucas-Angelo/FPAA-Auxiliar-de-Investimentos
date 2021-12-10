package src.algorithms;

import java.util.List;

import src.models.Ativo;
import src.models.Portifolio;

public interface IConstrutorDePortifolio {
    public Portifolio ContruirPortifolio(List<Ativo> dados);
}
