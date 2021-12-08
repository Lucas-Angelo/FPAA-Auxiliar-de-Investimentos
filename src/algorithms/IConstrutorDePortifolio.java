package src.algorithms;

import java.util.List;

import src.models.Ativo;

public interface IConstrutorDePortifolio {
    public Portifolio ContruirPortifolio(List<Ativo> dados);
}
