package com.spring.rest.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.rest.model.Person;
import com.spring.rest.service.PersonService;


@RestController
public class PersonController {
	@Autowired
	PersonService personService;

	@GetMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Person> getAll() {

		return personService.getAllPersons();
	}

	@PutMapping(value = "/person")
	public HttpStatus insertPerson(@RequestBody Person person) {
		personService.addPerson(person);
		return HttpStatus.CREATED;
	}


	// Endpoint to upload a file.
	@PostMapping(value = "/upload/direct", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<String> handleFileDirectUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {


		System.out.println("AWSService API : The file received is : " + file.getOriginalFilename());


		Map<String, String> response = new HashMap<>();
		response.put("status", "OK");
		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	// Endpoint that uploads this file to above endpoint using resttemplate.
	@PostMapping("/upload-profile-image")
	public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws IOException {

		System.out.println("file uploaded is : " + file.getOriginalFilename());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body
		= new LinkedMultiValueMap<>();

		ByteArrayResource contentsAsResource = new ByteArrayResource(file.getBytes()) {
	        @Override
	        public String getFilename() {
	            return file.getOriginalFilename(); 
	        }
	    };
		body.add("file", contentsAsResource);

		HttpEntity<MultiValueMap<String, Object>> requestEntity
		= new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> theResponse = restTemplate
				.postForEntity("http://localhost:8080/springrest/upload/direct", requestEntity, String.class);

		System.out.println("The Response is : " + theResponse.getBody());

		Map<String, String> response = new HashMap<>();
		response.put("status", "OK");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}



}

