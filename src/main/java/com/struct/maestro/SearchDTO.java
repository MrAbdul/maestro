package com.struct.maestro;

import java.util.List;

public class SearchDTO {

	private String search;
	private List<String> logLocation;
	private List<String> results;

	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<String> getLogLocation() {
		return logLocation;
	}

	public void setLogLocation(List<String> logLocation) {
		this.logLocation = logLocation;
	}

	public List<String> getResults() {
		return results;
	}

	public void setResults(List<String> results) {
		this.results = results;
	}

	
	
	
}
