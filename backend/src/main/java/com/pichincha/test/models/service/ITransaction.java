package com.pichincha.test.models.service;

import java.math.BigDecimal;
import java.util.List;

import com.pichincha.test.models.Entity.Transaction;

public interface ITransaction {

	//CRUD
	public List<Transaction> getAll(); 
	public Transaction getById(int id); 
	public Transaction save(Transaction transaction) throws Exception; 
	public Transaction update(Transaction transaction) throws Exception; 
	public void deleteById(int id) throws Exception;
	
	//OTHER FUNCTIONS
	public void checkIfExists(int id) throws Exception; 
	public BigDecimal getLastBalance(int accountId) throws Exception; 
	public BigDecimal sumAmountToBalance(Transaction transaction, BigDecimal lastBalance) throws Exception; 
	public Transaction getCompleteObject(Transaction transaction); 
	public void checkCanEdit(Transaction transaction) throws Exception; 
	public BigDecimal recalculateBalance(Transaction transaction) throws Exception; 
}
