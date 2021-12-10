package src.models;

import java.util.HashMap;
import java.util.Map;

public class Portifolio {
    // mapa de ativos Ativo:Peso
    private Map<Ativo, Double> ativos;
    
    public Portifolio(){
        ativos = new HashMap<>();
    }

    public void Add(Ativo ativo, double peso){
        ativos.put(ativo, peso);
    }

    public double getRetorno(){
        double somaRetorno = ativos.entrySet().stream().map( (var entry) -> {
            Ativo ativo = entry.getKey();
            double peso = entry.getValue();

            return ativo.getRetornoEfetivo().doubleValue() * (peso  / 100);
        }).reduce(Double::sum).orElse(0.0);

        return somaRetorno;
    }

    public double getRisco(){
        double somaRisco = ativos.entrySet().stream().map( (var entry) -> {
            Ativo ativo = entry.getKey();
            double peso = entry.getValue();

            return ativo.getRiscoNormalizado().doubleValue() * (peso / 100);
        }).reduce(Double::sum).orElse(0.0);

        double risco = somaRisco / ativos.size();
        return risco;
    }

    public double getRiscoRetorno(){
        double somaRiscoRetorno = ativos.entrySet().stream().map( (var entry) -> {
            Ativo ativo = entry.getKey();
            double peso = entry.getValue();

            return ativo.getRiscoRetorno().doubleValue() * (peso/100);
        }).reduce(Double::sum).orElse(0.0);

        double riscoRetorno = somaRiscoRetorno / ativos.size();
        return riscoRetorno;
    }

    public double getPesoTotal(){
        return ativos.values()
            .stream()
            .reduce(Double::sum)
            .orElse(0.0);
    }

    public Map<Ativo, Double> getAtivos() {
        return ativos;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nPortifolio felas: " + this.ativos.size() + " ativos");
        stringBuilder.append("\nRetorno Esperado: " + this.getRetorno());
        stringBuilder.append("\nRisco Esperado: " + this.getRisco());
        stringBuilder.append("\nRelação Risco/Retorno: " + this.getRiscoRetorno());
        stringBuilder.append("\nAtivos: ");
        this.ativos.entrySet().forEach( entry -> {
            Ativo ativo = entry.getKey();
            double peso = entry.getValue();

            stringBuilder.append( "\n  "+ peso +"% - " + ativo );
        });

        return stringBuilder.toString();
    }

}
