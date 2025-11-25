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
import java.net.URL;
import java.io.*;
import java.util.zip.*;

public class GithubClient {
	private HttpClient client;
	private String baseUrl;

	public GithubClient() {
		this.client = HttpClient.newHttpClient();
		this.baseUrl = "https://api.github.com";
	}

	public static void unzip(File zipFile, File destDir) throws IOException {
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				File outFile = new File(destDir, entry.getName());

				if (entry.isDirectory()) {
					outFile.mkdirs();
				} else {
					outFile.getParentFile().mkdirs();

					try (FileOutputStream fos = new FileOutputStream(outFile)) {
						byte[] buffer = new byte[4096];
						int len;
						while ((len = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
					}
				}
				zis.closeEntry();
			}
		}
	}

	public static void downloadFile(String fileUrl, String savePath) throws Exception {
		URL url = new URL(fileUrl);
		InputStream in = url.openStream();
		FileOutputStream fos = new FileOutputStream(savePath);
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1) {
			fos.write(buffer, 0, bytesRead);
		}
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
			DAR newDar = new DAR("github", owner, repo, item.get("name").asText());
			result.add(newDar);
		});
		return result;
	}

	public void pull(String owner, String repo, String version) throws Exception {
		String fileUrl = "https://github.com/" + owner + "/" + repo + "/releases/tag/" + version;
		String zipPath = "temp.zip";
		this.downloadFile(fileUrl, zipPath);
		this.unzip(
			new File(zipPath), 
			new File(System.getProperty("user.home") + "/daroot/github~" + owner + "/" + repo + "@" + version)
		);
	}
}
