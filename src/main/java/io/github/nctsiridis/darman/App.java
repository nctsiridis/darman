package io.github.nctsiridis.darman;

import io.github.nctsiridis.darman.clients.GithubClient;
import io.github.nctsiridis.darman.common.DAR;

import java.util.ArrayList;
import org.apache.commons.text.similarity.LevenshteinDistance;
import java.lang.Math;

public class App {
	private GithubClient githubClient;
	private LevenshteinDistance ld;

	public App(GithubClient githubClient) {
		this.githubClient = githubClient;
		this.ld = new LevenshteinDistance();
	}

	// DAR Standard: source~owner/repo@tag
	public void search(String source, String owner, String repo, String versionMatch) {
		switch (source) {
			case "github":
				try {
					ArrayList<DAR> results = githubClient.search(owner, repo);
					boolean doFilter = !(versionMatch.length() == 1 && versionMatch.charAt(0) == '*');
					for (DAR result: results) {
						if (doFilter) {
							int distance = this.ld.apply(versionMatch, result.version);
							double indicator = 
								(double)distance / 
								(double)Math.max(versionMatch.length(), result.version.length());
							if (indicator > 0.5) continue; // TODO make configurable
						}
						result.display();
					}
				} catch (Exception e) {
					System.out.println("[EXCEPTION]: " + e.getMessage());
				}
		}
	}

	public void pull(String source, String owner, String repo, String version) {
		switch (source) {
			case "github":
				try {
					githubClient.pull(owner, repo, version);
				} catch (Exception e) {
					System.out.println("[EXCEPTION]: " + e.getMessage());
				}
		}
	}
}
