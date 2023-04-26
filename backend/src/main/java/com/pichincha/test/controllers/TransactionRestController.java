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
import org.springframework.web.bind.annotation.RestController;

import com.pichincha.test.models.Entity.Client;
import com.pichincha.test.models.Entity.Transaction;
import com.pichincha.test.models.service.ITransaction;

@RestController()
@RequestMapping("api/transactions")
public class TransactionRestController {
	
	@Autowired
	ITransaction transactionService; 
	
	@GetMapping("")
	public ResponseEntity getAll() {
		
		try {
			return ResponseEntity.status(200).body(transactionService.getAll()); 
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request " + e.getMessage()); 
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable int id) {
		
		try {
			return ResponseEntity.status(200).body(transactionService.getById(id)); 
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request "+ e.getMessage()); 
		}
	}
	
	@PostMapping("")
	public ResponseEntity post(@RequestBody Transaction transaction) {
		
		try {
			return ResponseEntity.status(200).body(transactionService.save(transaction)); 
		}catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request "+ e.getMessage()); 
		}
	}
	
	@PutMapping("")
	public ResponseEntity put(@RequestBody Transaction transaction) {
		try {
			return ResponseEntity.status(202).body(transactionService.update(transaction)); 
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request " + e.getMessage()); 
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable int id) {
		try {
			transactionService.deleteById(id); 
			JSONObject message = new JSONObject(); 
			message.put("message", "Transaction with ID " + id + " deleted."); 
			return ResponseEntity.status(200).body(message); 
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request "+ e.getMessage()); 
		}
	}
}
