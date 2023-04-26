package com.pichincha.test.models.service;

import java.util.List;

import com.pichincha.test.models.Entity.Client;

public interface IClient {
	
	//CRUD
	public List<Client> getAll(); 
	public Client getById(int id); 
	public Client save(Client client); 
	public Client update(Client client) throws Exception; 
	public void deleteById(int id) throws Exception; 
	
	//FUNCTIONS
	public void checkIfExists(int id) throws Exception;
	public void checkDontHaveAccounts(int id)throws Exception;
}
