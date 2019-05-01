package UFRJ.sistemaRecuperacaoInformacao;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
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
		popularElasticSearch();
		popularConsultas();
	}
	
	private static void popularConsultas() throws IOException
	{
		List<String> linhas = new ArrayList<>();
		List<String> documentos = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get("/home/victor/Downloads/consultas.txt"), Charset.forName("ISO8859_1")))
		{
			linhas = stream.collect(Collectors.toList());
			for (String linha : linhas)
			{
				if (linha.contains("<PT-title>"))
				{
					documentos.add(linha.replace("<PT-title>", "").replace("</PT-title>", ""));
				}
			}
		}
	}

	private static void popularElasticSearch() throws IOException
	{
		File repositorio = new File("/home/victor/Downloads/colecao_teste");
		
		List<String> linhas = new ArrayList<>();
		List<String> documentos = new ArrayList<>();
		for(File arquivo : repositorio.listFiles())
		{
			try (Stream<String> stream = Files.lines(Paths.get(arquivo.getPath()), Charset.forName("ISO8859_1")))
			{
				linhas = stream.collect(Collectors.toList());
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
		}
		
		JSONArray ja = FormataTexto.montarJson(documentos);
		ElasticSearchAction elasticSearchAction = new ElasticSearchAction();
		
		elasticSearchAction.adicionarIndice(ja);
		elasticSearchAction.realizarConsulta();
	}
}