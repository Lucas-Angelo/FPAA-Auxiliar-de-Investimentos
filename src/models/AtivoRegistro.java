package src.models;

import java.time.LocalDate;

public class AtivoRegistro {

    private static Integer ids;
    
    private Integer id;
    private String nome;
    private LocalDate data;
    private double preco;
    private double valor;
    private double dividendo; 

    static {
        ids=0;
    }

    public void init(String nome, String data, double preco, double valor, double dividendo) {
        this.id = ids;
        this.nome = nome;
        this.data = LocalDate.parse(data);
        this.preco = preco;
        this.valor = valor;
        this.dividendo = dividendo;
    }

    public AtivoRegistro() {
        ids++;
    }

    public AtivoRegistro(String nome, String data, double preco, double valor, double dividendo) {
        init(nome, data, preco, valor, dividendo);
        ids++;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return this.data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getPreco() {
        return this.preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getValor() {
        return this.valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getDividendo() {
        return this.dividendo;
    }

    public void setDividendo(double dividendo) {
        this.dividendo = dividendo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AtivoRegistro))
            return false;
        AtivoRegistro other = (AtivoRegistro) obj;
        if (this.id == null)
            if (other.id != null)
                return false;
            else if (!this.id.equals(other.id))
                return false;
        return true;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nome='" + getNome() + "'" +
            ", data='" + getData() + "'" +
            ", preco='" + getPreco() + "'" +
            ", valor='" + getValor() + "'" +
            ", dividendo='" + getDividendo() + "'" +
            "}";
    }
}
