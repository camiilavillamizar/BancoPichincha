package com.pichincha.test.models.implement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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
	void getAllTest() {
		List<Account> actualAccounts = accountService.getAll(); 
		assertEquals(accounts, actualAccounts);
	}
	
	@Test
	void getByIdTest() {
		Account account = accountService.getById(1); 
		assertEquals(accountA, account);
	}
	
	@Test
	void postTest() throws Exception {
		Account accountC = new Account(1, new Long(102940), AccountType.CORRIENTE, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		when(accountDao.save(Mockito.any(Account.class))).thenReturn(accountC);
		Account savedAccount = accountService.save(accountC);
		assertEquals(accountC, savedAccount); 
		verify(accountDao, times(1)).save(accountC); 
	}
	/**
	 * Can't save if account number already exists
	 * @throws Exception
	 */
	@Test
	void saveTest2() throws Exception {
		Account accountC = new Account(3, new Long(102938), AccountType.AHORROS, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		
		assertThrows(Exception.class, () -> accountService.save(accountC));
	}
	@Test
	void updateTest() throws Exception {
		Account accountC = new Account(1, new Long(102930), AccountType.AHORROS, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		when(accountDao.save(Mockito.any(Account.class))).thenReturn(accountC);
		Account savedAccount = accountService.update(accountC); 
		assertEquals(accountC, savedAccount); 
		verify(accountDao, times(1)).save(accountC); 
	}
	/**
	 * Can't update if account number already exists
	 * @throws Exception
	 */
	@Test
	void updateTest2() throws Exception {
		Account accountC = new Account(2, new Long(102938), AccountType.AHORROS, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		
		assertThrows(Exception.class, () -> accountService.update(accountC)); 
	}
	
	@Test
	void deleteTest() throws Exception {
		assertThrows(Exception.class, () -> accountService.deleteById(accountA.getId()));
	}
	
	@Test
	void deleteTest2() throws Exception {
		accountService.deleteById(accountB.getId());
		verify(accountDao, times(1)).deleteById(accountB.getId());
	}
	
	@Test
	void checkIfExists() {	
		assertThrows(Exception.class, () -> accountService.checkIfExists(5));
		try {
			accountService.checkIfExists(1);
		} catch (Exception e) {
			fail(); 
		}
		verify(accountDao, times(1)).findById(1); 
	}
	
	@Test
	void checkIfNumberIsValid() {
		assertThrows(Exception.class, () -> accountService.checkIfNumberIsValid(accountA.getNumber()));
		try {
			accountService.checkIfNumberIsValid(new Long(999999));
		}catch(Exception e) {
			fail(); 
		}
		
		verify(accountDao, times(1)).findByNumber(new Long(999999)); 
	}
	
	@Test
	void checkDontHaveTransactions() {
		assertThrows(Exception.class, () -> accountService.checkDontHaveTransactions(accountA.getId()));
		try {
			accountService.checkDontHaveTransactions(accountB.getId());
		} catch(Exception e) {
			fail(); 
		}
		verify(transactionDao, times(1)).getByAccountId(accountB.getId()); 
	}
}
