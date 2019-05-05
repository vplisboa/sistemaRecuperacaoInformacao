package UFRJ.sistemaRecuperacaoInformacao;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import org.json.JSONObject;

public class ElasticSearchAction
{
	private Settings settings;

	private TransportClient client;

	public ElasticSearchAction() throws UnknownHostException {
		settings = Settings.builder().put("cluster.name", "docker-cluster").build();

		client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
	}

	public void adicionarIndice(JSONArray ja) throws IOException
	{
		for (int i = 0; i < ja.length(); i++)
		{
			client.prepareIndex("documentos", "documento", String.valueOf(i))
					.setSource(jsonBuilder().startObject().field("DOCID", ja.getJSONObject(i).get("DOCID"))
							.field("DOCNO", ja.getJSONObject(i).get("DOCNO"))
							.field("DATE", ja.getJSONObject(i).get("DATE"))
							.field("TEXT", ja.getJSONObject(i).get("TEXT")).endObject())
					.get();
			System.out.println("Documento " + String.valueOf(i) + " adicionado");
		}
	}

	public void atualizarIndice(JSONObject ja, String id) throws IOException, InterruptedException, ExecutionException
	{

		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("documento");
		updateRequest.type("3");
		updateRequest.id(id);
		updateRequest.doc(jsonBuilder().startObject()
				.field("DOCID", ja.get("DOCID"))
				.field("DOCNO", ja.get("DOCNO"))
				.field("DATE", ja.get("DATE"))
				.field("TEXT", ja.get("TEXT")).endObject());
		client.update(updateRequest).get();
	}

	public void deletarIndice(String indice) throws UnknownHostException
	{
		client.prepareDelete("documento", "3", indice).get();
	}

	public SearchHit[] realizarConsulta(String consulta)
	{
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
				.setScript("{\n" + "    \"query\": {\n" + "        \"multi_match\" : {\n"
						+ "            \"query\" : \" "+ consulta +"\",\n" + "            \"fields\": [\"TEXT\"],\n"
						+ "            \"fuzziness\": \"AUTO\"\n" + "        }\n" + "    },\n"
						+ "    \"_source\": [\"DOCID\",\"DOCNO\",\"DATE\",\"TEXT\"],\n" + "    \"size\": 100\n" + "}")
				.setScriptType(ScriptType.INLINE).setRequest(new SearchRequest()).get().getResponse();

		SearchHit[] hits = sr.getHits().getHits();
		
		System.out.println("foram encontrados " + hits.length + " resultados");
		System.out.println(hits[0].getScore());
		System.out.println(hits[0].getSource().get("DOCID"));
		System.out.println(hits[0].getSource().get("DOCNO"));
		System.out.println(hits[0].getSource().get("TEXT"));
		
		return hits;
	}
}