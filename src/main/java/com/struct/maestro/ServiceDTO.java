package com.struct.maestro;

public class ServiceDTO {

	private String server;
	private String name;
	private String command;
	private String status;
	private String mainPID;
	private String ip;
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMainPID() {
		return mainPID;
	}
	public void setMainPID(String mainPID) {
		this.mainPID = mainPID;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
