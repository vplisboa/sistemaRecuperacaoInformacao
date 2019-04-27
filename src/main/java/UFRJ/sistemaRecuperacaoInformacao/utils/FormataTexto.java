package UFRJ.sistemaRecuperacaoInformacao.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

public class FormataTexto
{
	
	public static List<String> formataTextoDocumento(String documento)
	{
		return Arrays.asList(documento.split(" "));
	}
	
	public static JSONArray montarJson(String documento)
	{
		List<Map<String,String>> conteudoJson = new ArrayList<Map<String,String>>();
		Map<String,String> json;
		
		String inicioDOCNO = "<DOCNO>";
		String fimDOCNO = "</DOCNO>";
		
		String inicioDOCID = "<DOCID>";
		String fimDOCID = "</DOCID>";
		
		String inicioDATE = "<DATE>";
		String fimDATE = "</DATE>";
		
		String inicioTEXT = "<TEXT>\n";
		String fimTEXT = "\n</TEXT>";

		Pattern p = Pattern.compile(Pattern.quote(inicioDOCNO) + "(.*?)" + Pattern.quote(fimDOCNO) 
									+ "\\s+.*?"+Pattern.quote(inicioDOCID) + "(.*?)" + Pattern.quote(fimDOCID)
									+ "\\s+.*?"+Pattern.quote(inicioDATE) + "(.*?)" + Pattern.quote(fimDATE)
									+ "\\s+.*?"+Pattern.quote(inicioTEXT) + "(.*?)" + Pattern.quote(fimTEXT));
		Matcher m = p.matcher(documento);
		
		while (m.find())
		{
			json = new HashMap<>();
			json.put("DOCNO",m.group(1));
			json.put("DOCID",m.group(2));
			json.put("DATE",m.group(3));
			json.put("TEXT",formataTextoDocumento(m.group(4)).toString());
			conteudoJson.add(json);
		}

		JSONArray ja = new JSONArray(conteudoJson);
		return ja;
	}
}