package UFRJ.sistemaRecuperacaoInformacao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.elasticsearch.search.SearchHit;
import org.json.JSONArray;

import UFRJ.sistemaRecuperacaoInformacao.utils.FormataTexto;

public class App
{
	private static ElasticSearchAction elasticSearchAction;
	
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException
	{
		elasticSearchAction = new ElasticSearchAction();
		
		popularElasticSearch();
		List<String> consultas = popularConsultas();
		
		SearchHit[] resultadoConsulta;
		System.out.println("realizando consulta");
		int i = 0;
		int j = 1;
		
		FileWriter fileWriter = new FileWriter("resultado2.txt");
	    PrintWriter printWriter = new PrintWriter(fileWriter);
		
		for(String consulta : consultas)
		{
			resultadoConsulta = elasticSearchAction.realizarConsulta(consulta);
			for(SearchHit hit : resultadoConsulta)
			{
				System.out.println(i + " Q0 "+ hit.getSource().get("DOCNO") +" " + j +" " + String.valueOf(hit.getScore()) + " victor_gustavo");
				i++;
				printWriter.print(i + " Q0 "+ hit.getSource().get("DOCNO") +" " + j +" " + String.valueOf(hit.getScore()) + " victor_gustavo\n");
			}
			j++;
			i=0;
		}
		printWriter.close();
		
	}
	
	private static List<String> popularConsultas() throws IOException
	{
		List<String> linhas = new ArrayList<>();
		List<String> documentos = new ArrayList<>();
		List<String> stopWords = Stream.of(" a "," o "," e "," é "," de "," do "," no "," são "," em "," na "," da ").collect(Collectors.toList());
		try (Stream<String> stream = Files.lines(Paths.get("/home/victor/Downloads/consultas.txt"), Charset.forName("ISO8859_1")))
		{
			linhas = stream.collect(Collectors.toList());
			for (String linha : linhas)
			{
				if (linha.contains("<PT-title>"))
				{
					for(String stopWord : stopWords)
					{
						if(linha.contains(stopWord))
							linha = linha.replace(stopWord, " ");
					}
					documentos.add(linha.replace("<PT-title>", "").replace("</PT-title>", ""));
				}
			}
		}
		return documentos;
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
		elasticSearchAction.adicionarIndice(ja);
	}
}