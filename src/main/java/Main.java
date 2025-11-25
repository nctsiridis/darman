package io.github.nctsiridis.darman;

import io.github.nctsiridis.darman.clients.GithubClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {

		GithubClient githubClient = new GithubClient();
		App app = new App(githubClient);
		String command = args[0];

		if (command.equals("search")) {
			String query = args[1];
			Pattern pattern = Pattern.compile("^[a-z0-9_-]+~[a-z0-9_-]+/[a-z0-9_-]+@[a-z0-9._*-]+$");
			Matcher matcher = pattern.matcher(query);

			String[] elements = query.split("[~/@]");
			String source = elements[0];
			String owner = elements[1];
			String repo = elements[2];
			String version = elements[3];

			if (!matcher.find()) {
				System.out.println("[SEARCH] '" + query + "' does not match pattern 'source~owner/repo@{versionQuery}'");
				return;
			}

			app.search(source, owner, repo, version);

		} else if (command.equals("pull")) {
			String query = args[1];
			Pattern pattern = Pattern.compile("^[a-z0-9_-]+~[a-z0-9_-]+/[a-z0-9_-]+@[a-z0-9._-]+$");
			Matcher matcher = pattern.matcher(query);

			String[] elements = query.split("[~/@]");
			String source = elements[0];
			String owner = elements[1];
			String repo = elements[2];
			String version = elements[3];

			if (!matcher.find()) {
				System.out.println("[PULL] '" + query + "' does not match pattern 'source~owner/repo@version'");
				return;
			}
			
			app.pull(source, owner, repo, version);

		} else {
			System.out.println("Command not recognized");
		}
	}
}
