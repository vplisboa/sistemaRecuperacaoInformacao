package UFRJ.sistemaRecuperacaoInformacao.utils;

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
	
	private static final String REGEX_SEPARAR_TOKENS = "[\\s\\.,!?]";
	
	public static List<String> formataTextoDocumento(String documento)
	{
		return Arrays.asList(documento.split(" "));
	}
	
	public List<String> separarFraseEmListaPalavras(String frase)
	{
		List<String> stopWords = Stream.of("a","o","e","é","de","do","no","são").collect(Collectors.toList());
		List<String> listaPalavras = Arrays.asList(frase.split(REGEX_SEPARAR_TOKENS));
		List<String> saida = new ArrayList<>();
		Boolean deveAdicionar = true;
		for (String palavra : listaPalavras)
		{
			for (String stopWord : stopWords)
			{
				if(palavra.toLowerCase().equals(stopWord.toLowerCase()))
				{
					deveAdicionar = false;
					break;
				}
				deveAdicionar = true;
			}
			if(deveAdicionar && !palavra.equals(""))
				saida.add(palavra.toLowerCase());
		}
		return saida;
	}
	
	public static JSONArray montarJson(List<String> documentos)
	{
		List<Map<String,String>> conteudoJson = new ArrayList<Map<String,String>>();
		Map<String,String> preparaJson = null;
		
		String inicioDOCNO = "<DOCNO>";
		String fimDOCNO = "</DOCNO>";
		
		String inicioDOCID = "<DOCID>";
		String fimDOCID = "</DOCID>";
		
		String inicioDATE = "<DATE>";
		String fimDATE = "</DATE>";
		
		String inicioTEXT = "<TEXT>";
		String fimTEXT = "</TEXT>";

		Pattern p = Pattern.compile(Pattern.quote(inicioDOCNO) + "(.*?)" + Pattern.quote(fimDOCNO) 
									+ ".*?"+Pattern.quote(inicioDOCID) + "(.*?)" + Pattern.quote(fimDOCID)
									+ ".*?"+Pattern.quote(inicioDATE) + "(.*?)" + Pattern.quote(fimDATE)
									+ ".*?"+Pattern.quote(inicioTEXT) + "(.*?)" + Pattern.quote(fimTEXT));

		
		for (String documento : documentos)
		{
			Matcher m = p.matcher(documento);
			
			
			while (m.find())
			{
				preparaJson = new HashMap<>();
				preparaJson.put("DOCNO",m.group(1));
				preparaJson.put("DOCID",m.group(2));
				preparaJson.put("DATE",m.group(3));
				preparaJson.put("TEXT",formataTextoDocumento(m.group(4)).toString());
				conteudoJson.add(preparaJson);
			}
		}
		
		JSONArray saida = new JSONArray(conteudoJson);
		return saida;
	}
}