package io.github.nctsiridis.darman.clients;

import io.github.nctsiridis.darman.common.DAR;
//import io.github.nctsiridis.darman.common.Utils;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class GithubClient {
	private HttpClient client;
	private String baseUrl;

	public GithubClient() {
		this.client = HttpClient.newHttpClient();
		this.baseUrl = "https://api.github.com";
	}

	public ArrayList<DAR> search(String owner, String repo) throws Exception {
		ArrayList<DAR> result = new ArrayList<DAR>();
		URI uri = new URI(this.baseUrl + "/repos/" + owner + "/" + repo + "/tags");
		HttpRequest request = HttpRequest.newBuilder()
			.uri(uri).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(response.body());
		if (node.getNodeType() != JsonNodeType.ARRAY) {
			throw new Exception("Unexpected response");
		}
		node.valueStream().forEach(item -> {
			if (item.getNodeType() != JsonNodeType.OBJECT) return;
			DAR newDar = new DAR("Github", owner, repo, item.get("name").asText());
			result.add(newDar);
		});
		return result;
	}
}
