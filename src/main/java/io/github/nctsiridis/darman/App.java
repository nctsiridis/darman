package io.github.nctsiridis.darman;

import io.github.nctsiridis.darman.clients.GithubClient;

public class App {
	private GithubClient githubClient;

	public App(GithubClient githubClient) {
		githubClient = githubClient;
	}

	// DAR Standard: source~owner/repo@tag
	public boolean search(String source, String owner, String repo, String tag) {
		switch (source) {
			case "github":
				System.out.println("TODO github");
				if (owner.charAt(0) == '*' && repo.charAt(0) == '*') {
					// Search both
					//githubClient.queryRepoOwner(repo.substring(1), owner.substring(1));
				} else if (owner.charAt(0) == '*') {
					// Query by owner, enumerate
					//githubClient.queryOwnerMatchRepo();
					// 
				} else if (repo.charAt(0) == '*') {
					// Query by repo
					//githubClient.queryRepoMatchOwner();
				} else {
					// Direct search
					//githubClient.matchRepoOwner
				}

				// Filter by tag
			// TODO add more
		}
		return false;
	}

	public boolean pull(String source, String owner, String repo, String tag) {
		//
		System.out.println("TODO pull");
		return false;
	}
}
