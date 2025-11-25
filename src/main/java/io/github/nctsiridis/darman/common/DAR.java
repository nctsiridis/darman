package io.github.nctsiridis.darman.common;

public class DAR {
	public String source;
	public String owner;
	public String repo;
	public String tag;

	public DAR(String source, String owner, String repo, String tag) {
		this.source = source;
		this.owner = owner;
		this.repo = repo;
		this.tag = tag;
	}

	public void display() {
		System.out.println(this.source + "~" + this.owner + "/" + this.repo + "@" + this.tag);
	}
}

