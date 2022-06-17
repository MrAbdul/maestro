package com.example.demo;

import java.io.Serializable;

public class ConfigDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String config;

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}
	
}
