package com.pichincha.test.models.implement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.pichincha.test.models.Dao.AccountDao;
import com.pichincha.test.models.Dao.ClientDao;
import com.pichincha.test.models.Dao.TransactionDao;
import com.pichincha.test.models.Entity.Account;
import com.pichincha.test.models.Entity.Client;
import com.pichincha.test.models.Entity.Transaction;
import com.pichincha.test.utils.enums.AccountType;
import com.pichincha.test.utils.enums.Gender;
import com.pichincha.test.utils.enums.TransactionType;


@SpringBootTest
public class AccountImplTest {

	@InjectMocks
	AccountImpl accountService; 
	
	@Mock
	AccountDao accountDao; 
	@Mock
	ClientImpl clientService;
	@Mock
	ClientDao clientDao; 
	@Mock
	TransactionDao transactionDao; 
	
	
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
	Account accountB = new Account(2, new Long(102939), AccountType.CORRIENTE, 
			BigDecimal.ZERO, true, client, new ArrayList<>());  
	List<Account> accounts =  Arrays.asList(accountA, accountB); 
	
	Transaction transactionA = new Transaction(1, LocalDateTime.now(), TransactionType.CREDITO, 
			new BigDecimal(100), new BigDecimal(100), accountA); 
	List<Transaction> transactions = Arrays.asList(transactionA); 
	
	
	@BeforeEach()
	void setUp() {
		MockitoAnnotations.initMocks(this);

		accountA.setTransactions(transactions);
		when(accountDao.findAll()).thenReturn(accounts); 
		when(accountDao.findById(1)).thenReturn(accountA); 
		when(accountDao.findById(2)).thenReturn(accountB);
		when(accountDao.save(Mockito.any(Account.class))).thenReturn(accountA);
		when(clientDao.findById(1)).thenReturn(client); 
		
 
		when(transactionDao.getByAccountId(1)).thenReturn(transactions); 
		when(transactionDao.getByAccountId(2)).thenReturn(new ArrayList<>()); 
		when(accountDao.findByNumber(new Long(102938))).thenReturn(accountA); 
		when(accountDao.findByNumber(new Long(999999))).thenReturn(null); 
	}
	
	@Test
	void testGetAll() {
		List<Account> actualAccounts = accountService.getAll(); 
		assertEquals("Accounts must be equals", accounts, actualAccounts);
	}
	
	@Test
	void testGetById_existingAccount() {
		Account account = accountService.getById(1); 
		assertEquals(accountA, account);
	}
	
	@Test
	void testGetById_unexistingAccount() {
		when(accountDao.findById(3)).thenReturn(null); 
		Account account = accountService.getById(3); 
		assertNull(account); 
	}
	
	@Test
	void testSave_newAccount() {
		Account accountC = new Account(1, new Long(102940), AccountType.CORRIENTE, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		when(accountDao.save(Mockito.any(Account.class))).thenReturn(accountC);
		try {
			Account savedAccount = accountService.save(accountC);
			assertEquals(accountC, savedAccount); 
			verify(accountDao, times(1)).save(accountC); 
		} catch (Exception e) {
			fail("Must do not throw any exception"); 
		}
	}

	@Test
	void testSave_wrongFields() {
		Account accountC = new Account(1, new Long(-102940), AccountType.CORRIENTE, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		Exception exception = assertThrows("Must throw an exception", Exception.class, () -> accountService.save(accountC)); 
		assertTrue("Must be an invalid number exception", exception.getMessage().equals("INVALID NUMBER"));  
	}
	/**
	 * Can't save if account number already exists
	 * @throws Exception
	 */
	@Test
	void testSave_existingAccountNumber() throws Exception {
		Account accountC = new Account(3, new Long(102938), AccountType.AHORROS, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		
		Exception exception = assertThrows(Exception.class, () -> accountService.save(accountC));
		assertTrue("Exception must be account number already exists", exception.getMessage().equals("ACCOUNT NUMBER ALREADY EXISTS")); 
		
	}
	
	@Test
	void testUpdate_exixtingAccount() throws Exception {
		Account accountC = new Account(1, new Long(102930), AccountType.AHORROS, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		when(accountDao.save(Mockito.any(Account.class))).thenReturn(accountC);
		Account savedAccount = accountService.update(accountC); 
		assertEquals("Must be same account", accountC, savedAccount); 
		verify(accountDao, times(1)).save(accountC); 
	}
	/**
	 * Can't update if account number already exists
	 * @throws Exception
	 */
	@Test
	void testUpdate_accountWithExistingNumber() throws Exception {
		Account accountC = new Account(2, new Long(102938), AccountType.AHORROS, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		
		Exception exception = assertThrows("Must throw exception", Exception.class, () -> accountService.update(accountC)); 
		assertTrue("Exception must be account number already exists", exception.getMessage().equals("ACCOUNT NUMBER ALREADY EXISTS")); 
	}
	
	@Test
	void testUpdate_unexistingAccount() {
		Account accountC = new Account(5, new Long(102930), AccountType.AHORROS, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		when(accountDao.findById(5)).thenReturn(null); 
		Exception exception = assertThrows("Must return an exception", Exception.class, () -> accountService.update(accountC)); 
		assertTrue("Must throw not found account", exception.getMessage().contains("does not exist")); 
	}
	
	@Test
	void testUpdate_changeBalanceWithTransactions() {
		Account updatedAccountA = new Account(1, new Long(1000), AccountType.AHORROS, 
				new BigDecimal(100), true, client, new ArrayList<>());
		Exception exception = assertThrows("Must return an exception", Exception.class, () -> accountService.update(updatedAccountA)); 
		assertTrue("Exception must be account has transactions", exception.getMessage().contains("HAS TRANSACTIONS")); 
	}
	@Test
	void testDelete_accountWithTransactions() throws Exception {
		Exception exception = assertThrows(Exception.class, () -> accountService.deleteById(accountA.getId()));
		assertTrue("Exception must be accoubt has transactions", exception.getMessage().contains("HAS TRANSACTIONS")); 
	}
	
	@Test
	void testDelete_accountWithoutTransactions() throws Exception {
		accountService.deleteById(accountB.getId());
		verify(accountDao, times(1)).deleteById(accountB.getId());
	}
	
	@Test
	void testCheckIfExists_unexistingAccount() {	
		when(accountDao.findById(5)).thenReturn(null); 
		Exception exception = assertThrows(Exception.class, () -> accountService.checkIfExists(5));
		assertTrue("Exception must be does not exist", exception.getMessage().contains("does not exist")); 
	}
	
	@Test
	void testCheckIfExists_existingAccount() {
		try {
			accountService.checkIfExists(1);
			verify(accountDao, times(1)).findById(1); 
		} catch (Exception e) {
			fail("Must doesn't throw an exception"); 
		}
	}
	
	@Test
	void testCheckIfNumberValid_validNumber() {
		try {
			accountService.checkIfNumberIsValid(new Long(999999));
			verify(accountDao, times(1)).findByNumber(new Long(999999)); 
		}catch(Exception e) {
			fail("Must do not have exceptions"); 
		}
		
		
	}
	@Test
	void testCheckIfNumberValid_invalidNumber() {
		Exception exception = assertThrows(Exception.class, () -> accountService.checkIfNumberIsValid(accountA.getNumber()));
		assertTrue("Must throw account number already exists", exception.getMessage().equals("ACCOUNT NUMBER ALREADY EXISTS"));
	}
	
	@Test
	void testCheckDontHaveTransactions_withTransactions() {
		 Exception exception = assertThrows(Exception.class, () -> accountService.checkDontHaveTransactions(accountA.getId()));
		 assertTrue("Must throw account has transactions exception",exception.getMessage().contains("HAS TRANSACTIONS"));
	}
	
	@Test
	void checkDontHaveTransactions() {
		try {
			accountService.checkDontHaveTransactions(accountB.getId());
			verify(transactionDao, times(1)).getByAccountId(accountB.getId());
		} catch(Exception e) {
			fail("Must do not throw exception"); 
		}
	}
}
