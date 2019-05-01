package UFRJ.sistemaRecuperacaoInformacao;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.json.JSONArray;

public class ElasticSearchAction
{
	private Settings settings;
	
	private TransportClient client;
	
	public ElasticSearchAction() throws UnknownHostException
	{
		settings = Settings.builder()
		        .put("cluster.name", "docker-cluster").build();
		
		client = new PreBuiltTransportClient(settings)
		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		
	}
	
	public void adicionarIndice(JSONArray ja) throws IOException
	{
		
		client.prepareIndex("documentos", "documento",ja.getJSONObject(1).get("DOCID").toString())
		        .setSource(jsonBuilder()
		                    .startObject()
		                        .field("DOCID", ja.getJSONObject(1).get("DOCID"))
		                        .field("DOCNO", ja.getJSONObject(1).get("DOCNO"))
		                        .field("DATE", ja.getJSONObject(1).get("DATE"))
		                        .field("TEXT", ja.getJSONObject(1).get("TEXT"))
		                    .endObject()
		                  )
		        .get();
	}
	
	public void atualizarIndice(JSONArray ja) throws IOException, InterruptedException, ExecutionException
	{

		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("documento");
		updateRequest.type("3");
		updateRequest.id("AWp0ei-j-NePops-Ed__");
		updateRequest.doc(jsonBuilder()
                .startObject()
                    .field("DOCID", ja.getJSONObject(1).get("DOCID"))
                    .field("DOCNO", ja.getJSONObject(1).get("DOCNO"))
                    .field("DATE", ja.getJSONObject(1).get("DATE"))
                    .field("TEXT", ja.getJSONObject(1).get("TEXT"))
                .endObject()
              );
		client.update(updateRequest).get();
	}
	
	public void deletarIndice(String indice) throws UnknownHostException
	{
		client.prepareDelete("documento", "3", indice).get();
	}
	
	public void realizarConsulta()
	{
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("param_gender", "male");
		
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
		        .setScript("{\n" + 
		        		"    \"query\": {\n" + 
		        		"        \"multi_match\" : {\n" + 
		        		"            \"query\" : \"ano\",\n" + 
		        		"            \"fields\": [\"DATE\",\"TEXT\"],\n" + 
		        		"            \"fuzziness\": \"AUTO\"\n" + 
		        		"        }\n" + 
		        		"    },\n" + 
		        		"    \"_source\": [\"DOCID\",\"DOCNO\",\"DATE\",\"TEXT\"],\n" + 
		        		"    \"size\": 100\n" + 
		        		"}")
		        .setScriptType(ScriptType.INLINE)    
		        .setRequest(new SearchRequest())                   
		        .get()                                             
		        .getResponse();
		
		SearchHit[] hits = sr.getHits().getHits();
		System.out.println(hits[0].getScore());
		System.out.println(hits[0].getSource().get("DOCID"));
		System.out.println(hits[0].getSource().get("DOCNO"));
		System.out.println(hits[0].getSource().get("TEXT"));
	}
}