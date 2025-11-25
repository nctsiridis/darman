package io.github.nctsiridis.darman.common;

public class DAR {
	public String source;
	public String owner;
	public String repo;
	public String version;

	public DAR(String source, String owner, String repo, String version) {
		this.source = source;
		this.owner = owner;
		this.repo = repo;
		this.version = version;
	}

	public void display() {
		System.out.println(this.source + "~" + this.owner + "/" + this.repo + "@" + this.version);
	}
}

