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