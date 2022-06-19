package com.struct.maestro;

import java.util.List;

public class AppDTO {

	private List<AppServersDTO> servers;

	public List<AppServersDTO> getServers() {
		return servers;
	}

	public void setServers(List<AppServersDTO> servers) {
		this.servers = servers;
	}
	
}

class AppServersDTO {
	
	private String name;
	private String ip;
	private List<AppListDTO> applications;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public List<AppListDTO> getApplications() {
		return applications;
	}
	public void setApplications(List<AppListDTO> applications) {
		this.applications = applications;
	}
	
	
}

class AppListDTO {
	private String name;
	private String serviceName;
	private List<String> logs;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public List<String> getLogs() {
		return logs;
	}
	public void setLogs(List<String> logs) {
		this.logs = logs;
	}
	
}