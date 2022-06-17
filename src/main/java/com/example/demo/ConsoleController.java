package com.example.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class ConsoleController {

	@Value("#{'${maestro.serversList}'.split(',')}")
	List<String> serversList;
	
	@GetMapping
	public String index(Model model) {
		
		RestTemplate restTemplate = new RestTemplate();
		List<ProDTO> outputList = new ArrayList<>();
		
		for(String server : serversList) {
			ResponseEntity<ProDTO> output = restTemplate.getForEntity(String.format("%s/data/systemStatus",server), ProDTO.class);
			outputList.add(output.getBody());
		}
				
		model.addAttribute("outputs", outputList);
		
		return "index";
	}
	
	@GetMapping("config")
	public String config(Model model) {
		
		ConfigDTO configDTO = new ConfigDTO();

		try {
			File configFile = new File("config.json");
			configFile.createNewFile();
			BufferedReader br = new BufferedReader(new FileReader(configFile));

			String x;
			StringBuilder sb = new StringBuilder();
			while((x = br.readLine()) != null) {
				sb.append(x);
			}

			model.addAttribute("config", sb.toString());
			
			br.close();
			configDTO.setConfig(sb.toString());
			model.addAttribute("config", configDTO);

			return "config";
		} catch (Exception e) {
			e.printStackTrace();
			configDTO.setConfig("Error reading config file !");
			model.addAttribute("config", configDTO);
			return "config";

		}
	}
	
	@PostMapping("config")
	public String config(@ModelAttribute("config") ConfigDTO configDTO, Model model) {
		

		try {
			File configFile = new File("config.json");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(configFile));

			bw.write(configDTO.getConfig());

			
			bw.close();

			model.addAttribute("config", configDTO);

			return "config";
		} catch (Exception e) {
			e.printStackTrace();
			configDTO.setConfig("Error reading config file !");
			model.addAttribute("config", configDTO);
			return "config";

		}
	}
	
	@GetMapping("search")
	public String searchIndex(Model model) {
		model.addAttribute("search", new SearchDTO());

		return "search";
	}
	
	@PostMapping("search")
	public String searchLogs(@ModelAttribute("search") SearchDTO search, Model model) {
		
		RestTemplate restTemplate = new RestTemplate();
		List<String> outputList = new ArrayList<>();
		
		for(String server : serversList) {
			HttpEntity<SearchDTO> request = new HttpEntity<>(search);

			String output = restTemplate.postForObject(String.format("%s/data/searchLogs",server), request, String.class);

			outputList.add(output);
		}
				
		model.addAttribute("searchResults", outputList);
		
		return "search";
	}
}
