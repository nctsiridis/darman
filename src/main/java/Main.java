package io.github.nctsiridis.darman;

import io.github.nctsiridis.darman.clients.GithubClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		GithubClient githubClient = new GithubClient(10);
		App app = new App(githubClient);

		String command = args[0];
		String query = args[1];
		System.out.println("[DEBUG] Command: " + command + ", Query: " + query);

		Pattern pattern = Pattern.compile("^[a-z0-9_-]+~?[*][a-z0-9_-]+/?[*][a-z0-9_-]+@?[*][a-z0-9._-]+$");
		Matcher matcher = pattern.matcher(query);
		
		if (!matcher.find()) {
			System.out.println("'" + query + "' does not match pattern: source~owner/repo@version");
			return;
		}
		String[] elements = query.split("[~/@]");
		String source = elements[0];
		String owner = elements[1];
		String repo = elements[2];
		String version = elements[3];

		if (command.equals("search")) {
			boolean result = app.search(source, owner, repo, version);
		} else if (command.equals("pull")) {
			boolean result = app.pull(source, owner, repo, version);
		} else {
			System.out.println("Invalid Command");
		}
	}
}
