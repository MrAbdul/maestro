package com.struct.maestro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ConsoleController {

	@GetMapping
	public String index(Model model) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			List<ProDTO> outputList = new ArrayList<>();
			File configFile = new File("config.json");
			BufferedReader br = new BufferedReader(new FileReader(configFile));
			String x = br.lines().collect(Collectors.joining());
			br.close();

			AppDTO appDTO = new ObjectMapper().readValue(x, new TypeReference<AppDTO>(){});
			for(AppServersDTO a : appDTO.getServers()) {
				try {
				String url = String.format("%s/data/systemStatus",a.getIp());
				ResponseEntity<ProDTO> output = restTemplate.getForEntity(url, ProDTO.class);
				outputList.add(output.getBody());
				} catch (ResourceAccessException e) {
					ProDTO p = new ProDTO();
					p.setHostname(a.getName());
					p.setCpu(null);
					outputList.add(p);
				}
			}

			model.addAttribute("outputs", outputList);

			return "index";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("outputs", null);
			return "index";
		}
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
		try {
			RestTemplate restTemplate = new RestTemplate();

			List<String> outputList = new ArrayList<>();

			File configFile = new File("config.json");

			BufferedReader br = new BufferedReader(new FileReader(configFile));

			String x = br.lines().collect(Collectors.joining());

			AppDTO appDTO = new ObjectMapper().readValue(x, new TypeReference<AppDTO>(){});

			List<String> allLogs = new ArrayList<String>();

			for(AppServersDTO a : appDTO.getServers()) {

				List<AppListDTO> al = a.getApplications().stream().collect(Collectors.toList());

				for(AppListDTO q : al) {
					allLogs.addAll(q.getLogs());
				}

				search.setLogLocation(allLogs); 

				HttpEntity<SearchDTO> request = new HttpEntity<>(search);

				SearchDTO output = restTemplate.postForObject(String.format("%s/data/searchLogs",a.getIp()), request, SearchDTO.class);

				// Will prevent adding null results
				if(output != null)
					Optional.ofNullable(
							output.getResults()
							.stream()
							.map(m->a.getName()+":"+m)
							.collect(Collectors.toList()))
					.ifPresent(outputList::addAll);
				else
					outputList = null;

			}

			br.close();
			model.addAttribute("searchResults", outputList);

			return "search";
		} catch (Exception e) {
			e.printStackTrace();
			return "search";
		}
	}
	
	
	@GetMapping("applications")
	public String applications(@ModelAttribute("services") ServiceDTO services, Model model) {
		
		try {
			RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

			List<ServiceDTO> applications = new ArrayList<ServiceDTO>();

			File configFile = new File("config.json");

			BufferedReader br = new BufferedReader(new FileReader(configFile));

			String x = br.lines().collect(Collectors.joining());

			AppDTO appDTO = new ObjectMapper().readValue(x, new TypeReference<AppDTO>(){});

			for(AppServersDTO a : appDTO.getServers()) {

				List<AppListDTO> al = a.getApplications().stream().collect(Collectors.toList());
				

				for(AppListDTO q : al) {
					ServiceDTO s = new ServiceDTO();
					s.setName(q.getServiceName());
					HttpEntity<ServiceDTO> request = new HttpEntity<>(s);
					try {
						ServiceDTO output = restTemplate.postForObject(String.format("%s/data/serviceshealth",a.getIp()), request, ServiceDTO.class);
						output.setServer(a.getName());
						output.setIp(a.getIp());
						output.setName(q.getName());
						applications.add(output);
					} catch (ResourceAccessException e) {
						ServiceDTO output = new ServiceDTO();
						output.setServer(a.getName());
						output.setIp(null);
						applications.add(output);
					}
				}

			}

			br.close();
			model.addAttribute("applications", applications);

			return "applications";
		} catch (ResourceAccessException e) {
			model.addAttribute("applications", null);
			return "applications";		
		} catch (Exception e) {
			e.printStackTrace();
			return "applications";
		}
	}
	
	private SimpleClientHttpRequestFactory getClientHttpRequestFactory() 
	{
	    SimpleClientHttpRequestFactory clientHttpRequestFactory
	                      = new SimpleClientHttpRequestFactory();
	    //Connect timeout
	    clientHttpRequestFactory.setConnectTimeout(5_000);
	     
	    //Read timeout
	    clientHttpRequestFactory.setReadTimeout(5_000);
	    return clientHttpRequestFactory;
	}
	
	@PostMapping("applications")
	public String appStart(@ModelAttribute("search") SearchDTO search, Model model, @RequestParam("action") String action,
			@RequestParam("ip") String ip) {

		String command = action.split(":")[0];

		RestTemplate restTemplate = new RestTemplate();
		
		ServiceDTO s = new ServiceDTO();
		s.setName(action.split(":")[1]);
		s.setCommand(command);
		
		HttpEntity<ServiceDTO> request = new HttpEntity<>(s);

		String output = restTemplate.postForObject(String.format("%s/data/servicecontrol",ip), request, String.class);

		System.out.println(output);
		
		return "redirect:/applications";
	}
	
}
