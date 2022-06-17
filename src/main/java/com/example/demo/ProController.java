package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("data")
public class ProController {

	@GetMapping
	public String check() {
		return "Server working fine ...";
	}
	
	@GetMapping("hostname")
	public String getHostname() {
		try {

			Process process = Runtime.getRuntime().exec("hostname");

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");
				return output.toString();
			} else {
				return "Error running command";
			}

		} catch (Exception e) {
			return "A FUCKING Exception Occured !!";
		}
	}
	
	@GetMapping("systemStatus")
	public ProDTO systemStatus() {
		try {
			ProDTO proDTO = new ProDTO();
			
			proDTO.setHostname(execCommand("hostname"));
			proDTO.setCpu(execCommand("top -b -n1 | grep 'Cpu(s)' | awk '{print $4}'"));
			proDTO.setMemoryTotal(execCommand("free -h | grep 'Mem:' | awk '{print $2}'"));
			proDTO.setMemoryUsed(execCommand("free -h | grep 'Mem:' | awk '{print $3}'"));
			proDTO.setMemoryFree(execCommand("free -h | grep 'Mem:' | awk '{print $4}'"));
			proDTO.setServiceStatus(execCommand("systemctl status mydemo | grep 'Active:' | awk '{print $2}'"));
			
			return proDTO;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping("searchLogs")
	public String searchLogs(@RequestBody SearchDTO search) {
		try {
			// TODO results must be list of objects and includes server names as well
			StringBuilder result = new StringBuilder();
			for(String c : search.getLogLocation()) {
				result.append(execSearch(String.format("cd %s && grep -IRc '%s'",c,search.getSearch())));
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// returns single only 
	public String execCommand(String command) throws Exception{
		
		Process process = Runtime.getRuntime().exec(new String[] {"/bin/bash", "-c", command});

		StringBuilder output = new StringBuilder();

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			output.append(line);
			break;
		}

		int exitVal = process.waitFor();
		if (exitVal == 0) {
			return output.toString();
		} else {
			return "Error running command";
		}

	}
	
	// returns multiple results - use when output is multiline
	public String execSearch(String command) throws Exception{
		
		Process process = Runtime.getRuntime().exec(new String[] {"/bin/bash", "-c", command});

		StringBuilder output = new StringBuilder();

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}

		int exitVal = process.waitFor();
		if (exitVal == 0) {
			return output.toString();
		} else {
			return "Error running command";
		}

	}
}
