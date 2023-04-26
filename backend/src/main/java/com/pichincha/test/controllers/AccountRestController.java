package com.pichincha.test.controllers;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pichincha.test.models.Entity.Account;
import com.pichincha.test.models.Entity.Client;
import com.pichincha.test.models.service.IAccount;

@RestController()
@RequestMapping("api/accounts")
public class AccountRestController {

	@Autowired
	IAccount accountService; 
	
	@GetMapping("")
	public ResponseEntity getAll() {
		
		try {
			return ResponseEntity.status(200).body(accountService.getAll()); 
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request " + e.getMessage()); 
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable int id) {
		
		try {
			return ResponseEntity.status(200).body(accountService.getById(id)); 
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request "+ e.getMessage()); 
		}
	}
	
	@GetMapping("/client/{clientId}")
	public ResponseEntity getByClientId(@PathVariable int clientId) {
		
		try {
			return ResponseEntity.status(200).body(accountService.getByClient(clientId)); 
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request "+ e.getMessage()); 
		}
	}
	
	@PostMapping("")
	public ResponseEntity post(@RequestBody Account account) {
		
		try {
			return ResponseEntity.status(200).body(accountService.save(account)); 
		}catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request "+ e.getMessage()); 
		}
	}
	
	@PutMapping("")
	public ResponseEntity put(@RequestBody Account account) {
		try {
			return ResponseEntity.status(202).body(accountService.update(account)); 
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request " + e.getMessage()); 
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable int id) {
		try {
			accountService.deleteById(id); 
			JSONObject message = new JSONObject(); 
			message.put("message", "Account with ID " + id + " deleted."); 
			return ResponseEntity.status(200).body(message); 
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request "+ e.getMessage()); 
		}
	}
}
