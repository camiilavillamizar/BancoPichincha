package com.pichincha.test.models.implement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
import com.pichincha.test.models.service.IAccount;
import com.pichincha.test.models.service.IClient;
import com.pichincha.test.utils.enums.AccountType;
import com.pichincha.test.utils.enums.Gender;
import com.pichincha.test.utils.enums.TransactionType;

@SpringBootTest
public class TransactionImplTest {
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
	
	 
	Account accountA = new Account();
	Account accountB = new Account();
	Transaction transactionA = new Transaction();
	Transaction transactionB = new Transaction();
	List<Transaction> transactions = new ArrayList<>(); 
	Account newAccount = new Account(); 
	Transaction transactionC = new Transaction(); 
	
	@BeforeEach()
	void setUp() {

		accountA = new Account(1, new Long(102938), AccountType.AHORROS, 
				BigDecimal.ZERO, true, client, new ArrayList<>()); 
		accountB = new Account(2, new Long(102938), AccountType.CORRIENTE, 
				new BigDecimal(100), true, client, new ArrayList<>()); 
		
		
		transactionA = new Transaction(1, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(100), new BigDecimal(100), accountA);  
		transactionB = new Transaction(2, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(100), new BigDecimal(0), accountB); 
		
		transactions =  Arrays.asList(transactionA, transactionB);
		newAccount = new Account(3, new Long(12344), AccountType.AHORROS, 
				new BigDecimal(1000), true, client, new ArrayList<>()); 
		transactionC = new Transaction(3, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(100), new BigDecimal(100), accountB); 
		
		MockitoAnnotations.initMocks(this);

		when(transactionDao.findAll()).thenReturn(transactions); 
		when(transactionDao.findById(1)).thenReturn(transactionA); 
		when(transactionDao.findById(2)).thenReturn(transactionB); 
		when(transactionDao.save(Mockito.any(Transaction.class))).thenReturn(transactionA); 
		when(accountService.getById(1)).thenReturn(accountA); 
		when(accountService.getById(2)).thenReturn(accountB); 
		when(clientService.getById(1)).thenReturn(client);
		when(transactionDao.getLastBalance(1)).thenReturn(BigDecimal.ZERO);
		when(transactionDao.getLastTransaction(1)).thenReturn(transactionA);
		
		
		
		when(accountDao.findById(3)).thenReturn(newAccount); 
		when(accountDao.findById(5)).thenReturn(null); 
		when(transactionDao.findById(3)).thenReturn(transactionC); 
	}
	@Test
	void testGetAll() {
		List<Transaction> actualTransactions = transactionService.getAll(); 
		assertEquals("Transactions must be equals", transactions, actualTransactions);
	}
	
	@Test
	void testGetById_existingTransaction() {
		Transaction transaction = transactionService.getById(1); 
		assertEquals("Transaction must be same", transactionA, transaction);
	}
	
	@Test
	void testGetById_unexistingTransaction() {
		when(transactionDao.findById(3)).thenReturn(null);
		assertNull("Transaction must be null", transactionService.getById(3));
	}
	
	/**
	 * Test we can make a movement whenever is CREDIT
	 * @throws Exception
	 */
	@Test
	void testSave_creditTransaction()  {
		Transaction transactionC = new Transaction(1, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(100), new BigDecimal(101), accountA);
		when(transactionDao.save(Mockito.any(Transaction.class))).thenReturn(transactionC); 
		Transaction savedTransaction;
		try {
			savedTransaction = transactionService.save(transactionC);
			assertEquals("Transaction must be same", transactionC, savedTransaction);
			assertEquals("Balance must be 100", new BigDecimal(100), savedTransaction.getBalance());
			verify(transactionDao, times(1)).save(transactionC); 
		} catch (Exception e) {
			fail("Must does not throw an exception");
		}
		
	}
	
	@Test
	void testSave_debitTransaction() {
		 
		Transaction expectedTransaction = new Transaction(3, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(100), new BigDecimal(900), accountB);
		when(transactionDao.save(Mockito.any(Transaction.class))).thenReturn(expectedTransaction); 
		
		try {
			Transaction savedTransaction = transactionService.save(transactionC);
			assertEquals("Transaction must be same", expectedTransaction, savedTransaction);
			assertEquals("Balance must be 900", new BigDecimal(900), savedTransaction.getBalance());
			verify(transactionDao, times(1)).save(transactionC); 
		} catch (Exception e) {
			fail("Must does not throw an exception");
		}
	}
	
	/**
	 * Test we can't make a movement when is DEBIT and don't have enought money
	 * @throws Exception
	 */
	@Test()
	void testSave_debitTransactionWithoutMoney(){
		Transaction transactionD = new Transaction(4, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(101), new BigDecimal(100), accountB); 
		when(transactionDao.findById(4)).thenReturn(transactionD); 
		
		Exception exception = assertThrows(Exception.class, () -> transactionService.save(transactionD));
		assertTrue("Must throw saldo no disponible", exception.getMessage().equals("SALDO NO DISPONIBLE"));
	}
	
	@Test
	void testSave_wrongAmount() {
		Transaction transactionC = new Transaction(1, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(-100), new BigDecimal(101), accountA);
		
		Exception exception = assertThrows("Must return an exception", Exception.class, () -> transactionService.save(transactionC)); 
		assertTrue("Must throw invalid amount", exception.getMessage().equals("INVALID AMOUNT"));
	}
	
	@Test
	void testUpdate_creditMov(){
		Transaction transactionC = new Transaction(1, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(200), new BigDecimal(100), accountA); 
		
		Transaction expectedTransaction = new Transaction(1, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(200), new BigDecimal(200), accountA); 
		
		when(transactionDao.save(Mockito.any(Transaction.class))).thenReturn(expectedTransaction);
		
		Transaction savedTransaction;
		try {
			savedTransaction = transactionService.update(transactionC);
			assertEquals("Transaction must be equal",expectedTransaction, savedTransaction); 
			verify(transactionDao, times(1)).save(transactionC); 
		} catch (Exception e) {
			fail("Must doesn't throw an exception"); 
		} 
	}
	
	@Test
	void testUpdate_debitMov(){
		
		Transaction transactionCToUpdate =  new Transaction(3, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(150), new BigDecimal(100), accountB); 
		Transaction expectedTransaction = new Transaction(3, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(150), new BigDecimal(850), accountB); 
		when(transactionDao.save(Mockito.any(Transaction.class))).thenReturn(expectedTransaction); 
		when(transactionDao.getLastTransaction(2)).thenReturn(transactionC);
		try {
			Transaction savedTransaction = transactionService.update(transactionCToUpdate);
			assertEquals("Transaction must be equal", expectedTransaction, savedTransaction); 
			verify(transactionDao, times(1)).save(transactionCToUpdate); 
		} catch (Exception e) {
			fail("Must doesn't throw an exception"); 
		} 
		
	}
	
	/**
	 * Should throw exception if is not last transaction
	 * @throws Exception
	 */
	@Test
	void testUpdate_notLastTransaction(){
		when(transactionDao.findById(2)).thenReturn(transactionB); 
		when(transactionDao.getLastTransaction(2)).thenReturn(transactionC); 
		Exception exception = assertThrows(Exception.class, () -> transactionService.update(transactionB));
		assertTrue("Exception must throw only can edit last transaction", exception.getMessage().equals("ONLY CAN EDIT LAST TRANSACTION")); 
	}
	
	@Test
	void testDelete_existingTransaction() {
		try {
			transactionService.deleteById(transactionA.getId());
			verify(transactionDao, times(1)).deleteById(transactionA.getId());
		} catch (Exception e) {
			fail("Must not throw an exception"); 
		}
	}
	
	@Test
	void testDelete_unexistingTransaction() {
		when(transactionDao.findById(5)).thenReturn(null); 
		Exception exception = assertThrows("Must throw an exception", Exception.class, () -> transactionService.deleteById(5)); 
		assertTrue("Exception must be does not exist", exception.getMessage().contains("does not exist")); 
	}
	
	
	@Test
	void testCheckIfExists_existingTransaction() {
		try {
			transactionService.checkIfExists(1);
		} catch (Exception e) {
			fail(); 
		}
		verify(transactionDao, times(1)).findById(1); 
	}

	@Test
	void testCheckIfExists_unxistingTransaction() {
		when(transactionDao.findById(5)).thenReturn(null); 
		assertThrows(Exception.class, () -> transactionService.checkIfExists(5));
	}
	
	@Test////////////////////
	void testGetLastBalance() {
		try {
			BigDecimal balance = transactionService.getLastBalance(1); 
			verify(transactionDao, times(1)).getLastBalance(1); 
			assertEquals("Balance must be 0", BigDecimal.ZERO, balance); 
		}catch(Exception e) {
			fail("Must doesn't throw an exception"); 
		}
	}
	
	
	@Test
	void testSumAmountToBalance_creditMov() {
		
		BigDecimal lastBalance = new BigDecimal(0); 
		try {
			BigDecimal actualBalance = transactionService.sumAmountToBalance(transactionA, lastBalance);
			assertEquals("Balance must be 100", new BigDecimal(100), actualBalance); 
		} catch (Exception e) {
			fail("Must doesn't throw an exception"); 
		} 
		
	}
	
	@Test
	void testSumAmountToBalance_debitMov() {
		BigDecimal lastBalance = new BigDecimal(100); 
		try {
			BigDecimal actualBalance = transactionService.sumAmountToBalance(transactionB, lastBalance);
			assertEquals("Balance must be 0", BigDecimal.ZERO, actualBalance); 
		} catch (Exception e) {
			fail("Must doesn't throw an exception"); 
		} 
	}
	
	@Test
	void testGetCompleteObject() {
		Transaction transaction = transactionService.getCompleteObject(transactionA); 
		assertSame("Must be same transaction ", transactionA, transaction); 	
	}
	
	@Test
	void testCheckCanEdit_lastTransaction() {
		
		transactionC.setDate(LocalDateTime.now().plusSeconds(1));
		when(transactionDao.getLastTransaction(2)).thenReturn(transactionC); 
		try {
			transactionService.checkCanEdit(transactionC);
			verify(transactionDao, times(1)).getLastTransaction(transactionC.getAccount().getId()); 
		}catch(Exception e) {
			fail("Must not throw an exception"); 
		}
	}
	
	@Test
	void testCheckCanEdit_noLastTransaction() {
		transactionC.setDate(LocalDateTime.now().plusSeconds(1));
		when(transactionDao.getLastTransaction(2)).thenReturn(transactionC); 
		Exception exception = assertThrows(Exception.class, () -> transactionService.checkCanEdit(transactionB));
		assertTrue("Exception must be only can edit last transaction", exception.getMessage().equals("ONLY CAN EDIT LAST TRANSACTION")); 
	}
	
	@Test
	void testRecalculateBalance_creditMov() {
		//Account's balance: 1000
		//Transaction: amount 100,  balance 1100
		//updated Transaction: amount: 200, balance: 1200
		Transaction transactionD = new Transaction(4, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(100), new BigDecimal(1100), accountB); 
		Transaction transactionDUpdated = new Transaction(4, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(200), new BigDecimal(100), accountB); 
		when(transactionDao.findById(4)).thenReturn(transactionD); 
		BigDecimal expectedBalance = new BigDecimal(1200); 
		try {
			assertEquals("Balance must be 1200", expectedBalance, transactionService.recalculateBalance(transactionDUpdated));
		} catch (Exception e) {
			fail("Must do not throw an exception"); 
		} 
		
	}
	
	@Test
	void testRecalculateBalance_creditToDebitMov() {
		//Account's balance: 1000
		//Transaction: amount 100,  balance 1100 credit 
		//updated Transaction: amount: 200, balance: 800 debit
		Transaction transactionD = new Transaction(4, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(100), new BigDecimal(1100), accountB); 
		Transaction transactionDUpdated = new Transaction(4, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(200), new BigDecimal(1100), accountB); 
		when(transactionDao.findById(4)).thenReturn(transactionD); 
		BigDecimal expectedBalance = new BigDecimal(800); 
		try {
			assertEquals("Balance must be 800", expectedBalance, transactionService.recalculateBalance(transactionDUpdated));
		} catch (Exception e) {
			fail("Must do not throw an exception"); 
		} 
	}
	
	@Test 
	void testRecalculateBalance_creditToDebitMovWithoutMoney() {
		//Account's balance: 1000
		//Transaction: amount 100,  balance 1100 credit 
		//updated Transaction: amount: 2000, balance: -1000 debit
		Transaction transactionD = new Transaction(4, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(100), new BigDecimal(1100), accountB); 
		Transaction transactionDUpdated = new Transaction(4, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(2000), new BigDecimal(1100), accountB); 
		when(transactionDao.findById(4)).thenReturn(transactionD); 
		
		Exception exception = assertThrows("Must throws an exception", Exception.class, () -> transactionService.recalculateBalance(transactionDUpdated));
		assertTrue("Exception must be saldo no disponible", exception.getMessage().equals("SALDO NO DISPONIBLE")); 
	}
	
	@Test
	void testRecalculateBalance_debitMov() {
		//Account's balance: 1000
		//Transaction: amount 100,  balance 900
		//updated Transaction: amount: 200, balance: 800
		transactionC.setBalance(new BigDecimal(900));
		Transaction transactionCtoUpdate = new Transaction(3, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(200), new BigDecimal(900), accountB); 
		BigDecimal expectedBalance = new BigDecimal(800); 
		try {
			assertEquals("Balance must be 800", expectedBalance, transactionService.recalculateBalance(transactionCtoUpdate));
		} catch (Exception e) {
			fail("Must don't throw any exception"); 
		} 
	}
	
	@Test
	void testRecalculateBalance_debitMovWithoutMoney() {
		//Account's balance: 1000
		//Transaction: amount 100,  balance 900
		//updated Transaction: amount: 2000, balance: -100
		transactionC.setBalance(new BigDecimal(900));
		Transaction transactionCtoUpdate = new Transaction(3, LocalDateTime.now(), TransactionType.DEBITO, 
				new BigDecimal(2000), new BigDecimal(900), accountB); 
		Exception exception = assertThrows("Must throws an exception", Exception.class, () -> transactionService.recalculateBalance(transactionCtoUpdate));
		assertTrue("Exception must be saldo no disponible", exception.getMessage().equals("SALDO NO DISPONIBLE"));
	}
	@Test
	void testRecalculateBalance_devitTocreditMov() {
		
		//Account's balance: 1000
		//Transaction: amount 100,  balance 900 debit
		//updated Transaction: amount: 200, balance: 1200 credit
		transactionC.setBalance(new BigDecimal(900));
		Transaction transactionCtoUpdate = new Transaction(3, LocalDateTime.now(), TransactionType.CREDITO, 
				new BigDecimal(200), new BigDecimal(900), accountB); 
		BigDecimal expectedBalance = new BigDecimal(1200); 
		try {
			assertEquals("Balance must be 800", expectedBalance, transactionService.recalculateBalance(transactionCtoUpdate));
		} catch (Exception e) {
			fail("Must don't throw any exception"); 
		}  
	}
}
