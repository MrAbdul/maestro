package com.example.demo;

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
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping("search")
	public String searchIndex(Model model) {
		model.addAttribute("search", new SearchDTO());

		return "search";
	}
	
	@PostMapping("search")
	public String searchLogs(@ModelAttribute("search") SearchDTO search, Model model) {
		
		System.out.println("=========>"+search.getSearch());
		RestTemplate restTemplate = new RestTemplate();
		List<String> outputList = new ArrayList<>();
		
		for(String server : serversList) {
			HttpEntity<SearchDTO> request = new HttpEntity<>(search);

			String output = restTemplate.postForObject(String.format("%s/data/searchLogs",server), request, String.class);

			outputList.add(output);
			System.out.println(output);
		}
				
		model.addAttribute("searchResults", outputList);
		
		return "search";
	}
}
