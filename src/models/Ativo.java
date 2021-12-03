package src.models;

import java.math.BigDecimal;
import java.math.MathContext;

public class Ativo {

    private static Integer ids;

    private Integer id;
    private String nome;

    private BigDecimal precoDeVenda;
    private BigDecimal precoDeCompra;
    private BigDecimal acumuloDeDividendos;
    
    // O retorno total em % que o ativo rendeu
    private BigDecimal retornoEfetivo;

    // RETORNO ESPERADO: Média de preços do ativo
    private BigDecimal mediaPreco;

    // RISCO DO ATIVO: Desvio padrão dos preços do ativo, disperção dos dados em realação a média de preço
    private BigDecimal volatividade;

    static {
        ids = 0;
    }

    public void init(String nome) {
        this.id = ids;
        this.nome = nome;
    }

    public Ativo() {
        ids++;
    }

    public Ativo(String nome) {
        init(nome);
        ids++;
    }

    public void calcRetornoEfetivo() {
        BigDecimal retornoEfetivo = BigDecimal.ZERO;
        retornoEfetivo = ((this.precoDeVenda.add(this.acumuloDeDividendos)).subtract(this.precoDeCompra)).divide(this.precoDeCompra, MathContext.DECIMAL32);
        this.retornoEfetivo = retornoEfetivo;
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

    public BigDecimal getPrecoDeVenda() {
        return this.precoDeVenda;
    }

    public void setPrecoDeVenda(BigDecimal precoDeVenda) {
        this.precoDeVenda = precoDeVenda;
    }

    public BigDecimal getPrecoDeCompra() {
        return this.precoDeCompra;
    }

    public void setPrecoDeCompra(BigDecimal precoDeCompra) {
        this.precoDeCompra = precoDeCompra;
    }

    public BigDecimal getAcumuloDeDividendos() {
        return this.acumuloDeDividendos;
    }

    public void setAcumuloDeDividendos(BigDecimal acumuloDeDividendos) {
        this.acumuloDeDividendos = acumuloDeDividendos;
    }

    public BigDecimal getRetornoEfetivo() {
        return this.retornoEfetivo.multiply(BigDecimal.valueOf(100));
    }

    public void setRetornoEfetivo(BigDecimal retornoEfetivo) {
        this.retornoEfetivo = retornoEfetivo;
    }

    public BigDecimal getVolatividade() {
        return this.volatividade;
    }

    public void setVolatividade(BigDecimal volatividade) {
        this.volatividade = volatividade;
    }

    public BigDecimal getMediaPreco() {
        return this.mediaPreco;
    }

    public void setMediaPreco(BigDecimal mediaPreco) {
        this.mediaPreco = mediaPreco;
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
        if (!(obj instanceof Ativo))
            return false;
        Ativo other = (Ativo) obj;
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
            " id='" + this.getId() + "'" +
            ", nome='" + this.getNome() + "'" +
            ", precoDeVenda='" + this.getPrecoDeVenda() + "'" +
            ", precoDeCompra='" + this.getPrecoDeCompra() + "'" +
            ", acumuloDeDividendos='" + this.getAcumuloDeDividendos() + "'" +
            ", retornoEfetivo='" + this.getRetornoEfetivo() + "'" +
            ", mediaPreco='" + this.getMediaPreco() + "'" +
            ", volatividade='" + this.getVolatividade() + "'" +
            "}";
    }
    
}
