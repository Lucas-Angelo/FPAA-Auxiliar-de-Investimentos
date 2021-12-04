package src.algorithms;

import java.util.List;

public interface IConstrutorDePortifolio<T> {
    public List<T> ContruirPortifolio(List<T> dados, int maximo);
}
