package com.example.demo;

import java.io.Serializable;

public class ProDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String hostname;
	private String cpu;
	private String memoryTotal;
	private String memoryUsed;
	private String memoryFree;
	private String serviceStatus;
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getMemoryTotal() {
		return memoryTotal;
	}
	public void setMemoryTotal(String memoryTotal) {
		this.memoryTotal = memoryTotal;
	}
	public String getMemoryUsed() {
		return memoryUsed;
	}
	public void setMemoryUsed(String memoryUsed) {
		this.memoryUsed = memoryUsed;
	}
	public String getMemoryFree() {
		return memoryFree;
	}
	public void setMemoryFree(String memoryFree) {
		this.memoryFree = memoryFree;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	
	
}
