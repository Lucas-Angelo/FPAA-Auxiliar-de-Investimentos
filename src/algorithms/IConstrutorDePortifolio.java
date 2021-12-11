package src.algorithms;

import java.util.List;

import src.models.Ativo;
import src.models.Portifolio;

public interface IConstrutorDePortifolio {
    /**
     * função que recebe uma lista de ativos e retorna um portifólio de ativos
     * as classes que implementam essa interface definem a forma de obtenção 
     * desse portifólio para encontrar um bom/ótimo resutado
     * @param dados lista de ativos para analisar
     * @return portifolio gerado
     */
    public Portifolio ContruirPortifolio(List<Ativo> dados);
}
