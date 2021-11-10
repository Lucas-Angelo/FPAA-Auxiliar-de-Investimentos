package src;

import java.util.ArrayList;
import java.util.List;

import src.helpers.*;
import src.models.*;

public class App {

    public static final String arquivo;
    public static final ArquivoTextoLeitura leitura;

    static {
        arquivo = "./data.csv";
        leitura = new ArquivoTextoLeitura();
    }

    public static void main(String[] args) {
        List<Ativo> ativos = preencherVetorAtivo();

        System.out.println(ativos);
    }

	public static List<Ativo> preencherVetorAtivo() {
		List<Ativo> ativos = new ArrayList<>();

		leitura.abrirArquivo(arquivo);

		leitura.ler(); // Remove o cabecalho
        String linha = new String();
		while (linha != null) {
            linha = leitura.ler();
			String[] dadosDaLinha = linha.split(",", 5); // Dividir os dados da linha (5 COLUNAS)

            if(linha!=null)
                ativos.add(new Ativo(dadosDaLinha[0], dadosDaLinha[1], Double.parseDouble(dadosDaLinha[2]), Double.parseDouble(dadosDaLinha[3]), Double.parseDouble(dadosDaLinha[4])));

            linha = leitura.ler();
		}

		leitura.fecharArquivo();

		return ativos;
	}
}