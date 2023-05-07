package com.pichincha.test.models.implement;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pichincha.test.models.Dao.AccountDao;
import com.pichincha.test.models.Dao.TransactionDao;
import com.pichincha.test.models.Entity.Account;
import com.pichincha.test.models.Entity.Client;
import com.pichincha.test.models.service.IAccount;
import com.pichincha.test.models.service.IClient;

@Service
@Transactional
public class AccountImpl implements IAccount{

	
	@Autowired
	AccountDao accountDao; 
	
	@Autowired
	IClient clientService; 
	
	@Autowired
	TransactionDao transactionDao; 
	
	
	public AccountImpl(AccountDao accountDao) {
		this.accountDao = accountDao; 
	}
	
	@Override
	public List<Account> getAll() {
		return accountDao.findAll(); 
	}

	@Override
	public Account getById(int id) {
		return accountDao.findById(id); 
	}

	@Override
	public List<Account> getByClient(int clientId) throws Exception {
		clientService.checkIfExists(clientId);
		return accountDao.findByClient(clientService.getById(clientId)); 
		
	}

	@Override
	public Account save(Account account) throws Exception {
		clientService.checkIfExists(account.getClient().getId());
		checkValidFields(account);
		checkIfNumberIsValid(account.getNumber()); 
		return accountDao.save(account); 
	}

	@Override
	public Account update(Account account) throws Exception {
		checkIfExists(account.getId());
		Account actualAccount = getById(account.getId()); 
		if(!actualAccount.getNumber().equals(account.getNumber())) {
			checkIfNumberIsValid(account.getNumber()); 			
		}
		checkValidFields(account);
		if(actualAccount.getTransactions().size() != 0 && account.getBalance().compareTo(actualAccount.getBalance()) != 0) {
			throw new Exception("CAN'T EDIT BALANCE, ACCOUNT HAS TRANSACTIONS"); 
		}
		return accountDao.save(account);
	}

	@Override
	public void deleteById(int id) throws Exception {
		checkIfExists(id);
		checkDontHaveTransactions(id);
		accountDao.deleteById(id);
	}

	@Override
	public void checkIfExists(int id) throws Exception {
		
		if(getById(id) == null)
			throw new Exception("Account "+ id + " does not exist"); 
		
	}
	
	public void checkValidFields(Account account) throws Exception{
		if(account.getNumber() < 0) throw new Exception("INVALID NUMBER"); 
		if(account.getBalance().compareTo(BigDecimal.ZERO) < 0) throw new Exception("INVALID BALANCE"); 
		
	}

	@Override
	public void checkIfNumberIsValid(Long number) throws Exception {
		
		if(accountDao.findByNumber(number) != null)
			throw new Exception("ACCOUNT NUMBER ALREADY EXISTS"); 
		
	}

	@Override
	public void checkDontHaveTransactions(int id) throws Exception {
		if(transactionDao.getByAccountId(id).size() != 0) {
			throw new Exception("ACCOUNT " + id + " HAS TRANSACTIONS"); 
		}
		
	}

}
