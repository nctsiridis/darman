package io.github.nctsiridis.darman;

import io.github.nctsiridis.darman.clients.GithubClient;
import io.github.nctsiridis.darman.common.DAR;

import java.util.ArrayList;

public class App {
	private GithubClient githubClient;

	public App(GithubClient githubClient) {
		this.githubClient = githubClient;
	}

	// DAR Standard: source~owner/repo@tag
	public boolean search(String source, String owner, String repo, String version) {
		switch (source) {
			case "github":
				try {
					ArrayList<DAR> results = githubClient.search(owner, repo);
					for (DAR result: results) {
						result.display();
					}
				} catch (Exception e) {
					System.out.println("Exception occurred during Github search: " + e.getMessage());
				}
		}
		return false;
	}

	public boolean pull(String source, String owner, String repo, String tag) {
		//
		System.out.println("TODO pull");
		return false;
	}
}
