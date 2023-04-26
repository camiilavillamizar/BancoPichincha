package com.pichincha.test.models.service;

import java.util.List;

import com.pichincha.test.models.Entity.Account;
import com.pichincha.test.models.Entity.Client;

public interface IAccount {

	//CRUD
	public List<Account> getAll(); 
	public Account getById(int id); 
	public List<Account> getByClient(int clientId) throws Exception; 
	public Account save(Account account) throws Exception; 
	public Account update(Account account) throws Exception; 
	public void deleteById(int id) throws Exception; 
	
	//FUNCTIONS
	public void checkIfExists(int id) throws Exception;
	public void checkIfNumberIsValid(Long number) throws Exception;
	public void checkDontHaveTransactions(int id) throws Exception;
}
