package com.pichincha.test.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pichincha.test.models.Dao.AccountDao;
import com.pichincha.test.models.Dao.ClientDao;
import com.pichincha.test.models.Dao.TransactionDao;
import com.pichincha.test.models.Entity.Account;
import com.pichincha.test.models.Entity.Client;
import com.pichincha.test.models.Entity.Transaction;
import com.pichincha.test.models.implement.AccountImpl;
import com.pichincha.test.models.implement.ClientImpl;
import com.pichincha.test.models.implement.TransactionImpl;
import com.pichincha.test.models.service.IAccount;
import com.pichincha.test.models.service.IClient;
import com.pichincha.test.utils.enums.AccountType;
import com.pichincha.test.utils.enums.Gender;
import com.pichincha.test.utils.enums.TransactionType;

@SpringBootTest
public class TransactionRestControllerTest {

	@InjectMocks
	TransactionImpl transactionService; 

	@Mock
	TransactionDao transactionDao;
	
	@Mock
	ClientDao clientDao; 
	
	@Mock
	AccountDao accountDao; 
	
	@Mock 
	IAccount accountService; 
	
	@Mock
	IClient clientService; 

	
	List<Transaction> transactions = new ArrayList<>(); 
	Transaction transactionA = new Transaction(); 
	Transaction transactionB = new Transaction(); 
	
	Client client = new Client(1, 
			"Laura Perez",
			Gender.FEMALE,
			24,
			"10398274",
			"Bogotá, calle 151 # 20 -59",
			"(+57) 319203403",
			"secret", 
			true, 
			new ArrayList());
	
	Account accountA = new Account(1, new Long(102938), AccountType.AHORROS, 
			BigDecimal.ZERO, true, client, new ArrayList<>()); 
	Account accountB = new Account(1, new Long(102938), AccountType.CORRIENTE, 
			BigDecimal.ZERO, true, client, new ArrayList<>()); 
	
	@BeforeEach()
	void setUp() {

		MockitoAnnotations.initMocks(this);
		transactionA = new Transaction(1, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(100), new BigDecimal(100), accountA); 
		
		transactionB = new Transaction(1, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(100), new BigDecimal(100), accountB); 
		
		transactions.add(transactionA); 
		transactions.add(transactionB); 
		
		when(transactionDao.findAll()).thenReturn(transactions); 
		when(transactionDao.findById(1)).thenReturn(transactionA); 
		when(transactionDao.save(Mockito.any(Transaction.class))).thenReturn(transactionA); 
		when(accountService.getById(1)).thenReturn(accountA); 
		when(accountService.getById(2)).thenReturn(accountB); 
		when(clientService.getById(1)).thenReturn(client);
		when(transactionDao.getLastBalance(1)).thenReturn(BigDecimal.ZERO);
		when(transactionDao.getLastTransaction(1)).thenReturn(transactionA);
	}
	@Test
	void getAllTest() {
		List<Transaction> actualTransactions = transactionService.getAll(); 
		assertEquals(transactions, actualTransactions);
	}
	
	@Test
	void getByIdTest() {
		Transaction transaction = transactionService.getById(1); 
		assertEquals(transactionA, transaction);
	}
	
	/**
	 * Test we can make a movement whenever is CREDIT
	 * @throws Exception
	 */
	@Test
	void postTest() throws Exception {
		Transaction transactionC = new Transaction(1, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(100), new BigDecimal(101), accountA);
		Transaction savedTransaction = transactionService.save(transactionC);
		
		assertNotEquals(savedTransaction, transactionC);
		assertEquals(savedTransaction, transactionA); 
		verify(transactionDao, times(1)).save(transactionC); 
	}
	
	/**
	 * Test we can't make a movement when is DEBIT and don't have enought money
	 * @throws Exception
	 */
	@Test()
	void postTest2() throws Exception {
		Transaction transactionC = new Transaction(1, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(100), new BigDecimal(100), accountB); 
		
		assertThrows(Exception.class, () -> transactionService.save(transactionC));

	}
	
	@Test
	void updateTest() throws Exception {
		Transaction transactionC = new Transaction(1, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(100), new BigDecimal(100), accountA); 
		Transaction savedTransaction = transactionService.update(transactionC); 
		assertNotEquals(transactionC, savedTransaction); 
		assertEquals(savedTransaction, transactionA); 
		verify(transactionDao, times(1)).save(transactionC); 
	}
	
	/**
	 * Should throw exception if is not last transaction
	 * @throws Exception
	 */
	@Test
	void updateTest2() throws Exception {
		Transaction transactionC = new Transaction(3, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(100), new BigDecimal(100), accountB);
		
		when(transactionDao.getLastTransaction(2)).thenReturn(transactionC); 
		assertThrows(Exception.class, () -> transactionService.update(transactionC));
	}
	@Test
	void deleteTest() throws Exception {
		transactionService.deleteById(transactionA.getId());
		verify(transactionDao, times(1)).deleteById(transactionA.getId());
	}
	
	
}
