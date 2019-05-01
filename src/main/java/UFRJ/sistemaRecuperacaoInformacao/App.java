package UFRJ.sistemaRecuperacaoInformacao;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;

import UFRJ.sistemaRecuperacaoInformacao.utils.FormataTexto;

public class App
{
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException
	{
		String arquivo = "/home/victor/Downloads/colecao_teste/FSP.940103.sgml";

		List<String> linhas = new ArrayList<>();
		List<String> documentos = new ArrayList<>();

		try (Stream<String> stream = Files.lines(Paths.get(arquivo), Charset.forName("ISO8859_1")))
		{
			linhas = stream.collect(Collectors.toList());
			System.out.println(linhas.size());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		StringBuilder documento = new StringBuilder();
		for (String linha : linhas)
		{
			if (linha.equals("</DOC>"))
			{
				documento.append(linha);
				documentos.add(documento.toString());
				documento = new StringBuilder();
			}
			else
			{
				documento.append(linha);
			}
		}

		JSONArray ja = FormataTexto.montarJson(documentos);
		
		ElasticSearchAction elasticSearchAction = new ElasticSearchAction();
		
		//ElasticSearchAction.adicionarIndice(ja);
		//ElasticSearchAction.atualizarIndice(ja);
		//ElasticSearchAction.deletarIndice("AWp0ei-j-NePops-Ed__");
		elasticSearchAction.realizarConsulta();
	}
}