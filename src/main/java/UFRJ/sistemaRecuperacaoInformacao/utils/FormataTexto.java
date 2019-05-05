package UFRJ.sistemaRecuperacaoInformacao.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;

public class FormataTexto
{
	private static final String PATH_LISTA_STOPWORDS = "/home/victor/projetos/sistemaRecuperacaoInformacao/src/main/java/UFRJ/sistemaRecuperacaoInformacao/stopWords.txt";
	private static final String REGEX_SEPARAR_TOKENS = "[\\s\\.,!?]";

	public static List<String> formataTextoDocumento(String documento)
	{
		return Arrays.asList(documento.split(" "));
	}

	public static List<String> separarFraseEmListaPalavras(String frase)
	{
		List<String> stopWords = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get(PATH_LISTA_STOPWORDS), Charset.forName("ISO8859_1")))
		{
			stopWords = stream.collect(Collectors.toList());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		List<String> listaPalavras = Arrays.asList(frase.split(REGEX_SEPARAR_TOKENS));
		List<String> saida = new ArrayList<>();
		Boolean deveAdicionar = true;
		for (String palavra : listaPalavras)
		{
			for (String stopWord : stopWords)
			{
				if (palavra.trim().toLowerCase().equals(stopWord.trim().toLowerCase()))
				{
					deveAdicionar = false;
					break;
				}
				deveAdicionar = true;
			}
			if (deveAdicionar && !palavra.equals(""))
				saida.add(palavra.trim().toLowerCase());
		}
		return saida;
	}

	public static JSONArray montarJson(List<String> documentos)
	{
		List<Map<String, String>> conteudoJson = new ArrayList<Map<String, String>>();
		Map<String, String> preparaJson = null;

		String inicioDOCNO = "<DOCNO>";
		String fimDOCNO = "</DOCNO>";

		String inicioDOCID = "<DOCID>";
		String fimDOCID = "</DOCID>";

		String inicioDATE = "<DATE>";
		String fimDATE = "</DATE>";

		String inicioTEXT = "<TEXT>";
		String fimTEXT = "</TEXT>";

		Pattern p = Pattern.compile(Pattern.quote(inicioDOCNO) + "(.*?)" + Pattern.quote(fimDOCNO) + ".*?"
				+ Pattern.quote(inicioDOCID) + "(.*?)" + Pattern.quote(fimDOCID) + ".*?" + Pattern.quote(inicioDATE)
				+ "(.*?)" + Pattern.quote(fimDATE) + ".*?" + Pattern.quote(inicioTEXT) + "(.*?)"
				+ Pattern.quote(fimTEXT));

		int i = 1;
		
		for (String documento : documentos)
		{
			Matcher m = p.matcher(documento);

			while (m.find())
			{
				preparaJson = new HashMap<>();
				preparaJson.put("DOCNO", m.group(1));
				preparaJson.put("DOCID", m.group(2));
				preparaJson.put("DATE", m.group(3));
				preparaJson.put("TEXT", separarFraseEmListaPalavras(m.group(4)).toString());
				conteudoJson.add(preparaJson);
			}
			System.out.println("Formate JSON " + i);
			i++;
		}

		JSONArray saida = new JSONArray(conteudoJson);
		return saida;
	}
}