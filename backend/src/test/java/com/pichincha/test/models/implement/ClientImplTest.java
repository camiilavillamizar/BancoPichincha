package com.pichincha.test.models.implement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.pichincha.test.models.Dao.AccountDao;
import com.pichincha.test.models.Dao.ClientDao;
import com.pichincha.test.models.Entity.Account;
import com.pichincha.test.models.Entity.Client;
import com.pichincha.test.models.service.IClient;
import com.pichincha.test.utils.enums.AccountType;
import com.pichincha.test.utils.enums.Gender;

@SpringBootTest
public class ClientImplTest {
	
	@Autowired
	ClientImpl clientService; 
	@MockBean
	ClientDao clientDao; 
	@MockBean
	AccountDao accountDao; 
	
	
	private static Client clientA = new Client(1, "Laura Perez",Gender.FEMALE,24,"10398274","Bogotá, calle 151 # 20 -59",
											"(+57) 319203403","secret", true, new ArrayList()); 
	private static Client clientB = new Client(2, "Leonardo Castellanos",Gender.MALE, 30, "1392388492", "Bogotá, calle 82 # 90 -53",
								"(+57) 3275392001", "secret", true, new ArrayList()); 
	
	List<Client> clients = Arrays.asList(clientA, clientB); 
	
	Account accountA = new Account(1, new Long(102938), AccountType.AHORROS, 
			BigDecimal.ZERO, true, clientA, new ArrayList<>());
	List<Account> accounts = Arrays.asList(accountA); 
	
	@BeforeEach()
	void setUp() {
		
		MockitoAnnotations.initMocks(this);
		
		when(clientDao.findAll()).thenReturn(clients); 
		when(clientDao.findById(1)).thenReturn(clientA); 
		when(clientDao.findById(2)).thenReturn(clientB); 


		when(accountDao.getByClientId(1)).thenReturn(accounts);
		when(accountDao.getByClientId(2)).thenReturn(new ArrayList<>());
	}
	
	@Test
	void testGetAllWithData() {
		List<Client> actualClients = clientService.getAll(); 
		assertEquals("Clients must be equals", actualClients, clients); 
		assertSame(clients, clientService.getAll());
	}

	
	@Test
	void testGetByExistingClient() {
		Client client = clientService.getById(1); 
		assertEquals("Name must be Laura Perez", client.getName(), "Laura Perez");
		assertEquals("Age must be 24", client.getAge(), 24);
		assertEquals("State must be True", client.isState(), true); 
		
	}
	@Test
	void testGetUnexistingClient() {
		when(clientDao.findById(3)).thenReturn(null);
		Client client = clientService.getById(3); 
		assertNull("Client must be null", client); 
	}
	
	@Test
	void testSaveClient() {
		
		Client clientC = new Client(3, "Jairo Serrano",Gender.MALE,30,"1392388492",
				"Bogotá, calle 82 # 90 -53","(+57) 3275392001","secret", true, new ArrayList()); 
		when(clientDao.save(Mockito.any(Client.class))).thenReturn(clientC); 
		try {
			assertEquals(clientService.save(clientC), clientC);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Should save client with correct fields"); 
		}
		verify(clientDao, times(1)).save(clientC); 
	}
	
	@Test
	void testSaveClient_wrongData() {
		
		Client clientC = new Client(3, "Jairo Serrano",Gender.MALE,30,"1392388492",
				"Bogotá, calle 82 # 90 -53","(+57) 3275392001","", true, new ArrayList()); 
		when(clientDao.save(Mockito.any(Client.class))).thenReturn(clientC); 
		
		assertThrows("Must return an exception", Exception.class, () -> { clientService.save(clientC); });
		verify(clientDao, times(0)).save(clientC); 
	}
	
	@Test
	void testUpdateClient() throws Exception {
		Client clientC = new Client(1, "Laura Perez",Gender.FEMALE,24,"103982723",
				"Bogotá, calle 151 # 20 -59a","(+57) 319203402","secret2", true, new ArrayList()); 
		when(clientDao.save(Mockito.any(Client.class))).thenReturn(clientC); 
		assertEquals(clientC, clientService.update(clientC));
		verify(clientDao, times(1)).save(clientC); 
	}
	
	@Test
	void testUpdateClient_wrongData() {
		Client clientC = new Client(1, "Laura Perez",Gender.FEMALE,24,"103982723",
				"Bogotá, calle 151 # 20 -59a","(+57) 319203402","", true, new ArrayList()); 
		when(clientDao.save(Mockito.any(Client.class))).thenReturn(clientC); 
		assertThrows("Must return an exception", Exception.class, () -> { clientService.update(clientC); });
		verify(clientDao, times(0)).save(clientC); 
	}

	
	@Test
	void testDelete_existingClient() throws Exception {
		clientService.deleteById(clientB.getId());
		verify(clientDao, times(1)).deleteById(clientB.getId()); 
	}
	
	@Test
	void testDelete_unexistingClient() throws Exception{
		when(clientDao.findById(3)).thenReturn(null);
		Exception exception = assertThrows("Must return an exception", Exception.class, () -> clientService.deleteById(3)); 
		assertTrue("Exception must say client does not exist", exception.getMessage().contains("does not exist")); 
		
	}
	
	/**
	 * Can't delete client if has accounts
	 * @throws Exception
	 */
	@Test
	void testDelete_clientWithAccounts() throws Exception {
		Exception exception = assertThrows("Must throw exception",Exception.class, () -> clientService.deleteById(clientA.getId()));
		assertTrue("Exception must be client has accounts", exception.getMessage().contains("HAS ACCOUNTS")); 
	}
	
	@Test
	void testValidateFields_wrongPassword() {
		Client clientC = new Client(3, "Jairo Serrano",Gender.MALE,30,"1392388492",
				"Bogotá, calle 82 # 90 -53","(+57) 3275392001","", true, new ArrayList()); 
		Exception exception = assertThrows("Must throw exception", Exception.class , () -> {
			clientService.validateFields(clientC);			
		});
		
		assertTrue("Must be invalid password exception", exception.getMessage().equals("INVALID PASSWORD")); 
	}
	
	@Test
	void testValidateFields_wrongName() {
		Client clientC = new Client(3, "",Gender.MALE,30,"1392388492",
				"Bogotá, calle 82 # 90 -53","(+57) 3275392001","1234", true, new ArrayList()); 
		Exception exception = assertThrows("Must throw exception", Exception.class , () -> {
			clientService.validateFields(clientC);			
		});
		
		assertTrue("Must be invalid name exception", exception.getMessage().equals("INVALID NAME")); 
	}
	@Test
	void testValidateFields_wrongAge() {
		Client clientC = new Client(3, "Jairo Serrano",Gender.MALE,-30,"1392388492",
				"Bogotá, calle 82 # 90 -53","(+57) 3275392001","1234", true, new ArrayList()); 
		Exception exception = assertThrows("Must throw exception", Exception.class , () -> {
			clientService.validateFields(clientC);			
		});
		
		assertTrue("Must be invalid age exception", exception.getMessage().equals("INVALID AGE")); 
	}
	
	@Test
	void testValidateFields_wrongDni() {
		Client clientC = new Client(3, "Jairo Serrano",Gender.MALE,30,"",
				"Bogotá, calle 82 # 90 -53","(+57) 3275392001","1234", true, new ArrayList()); 
		Exception exception = assertThrows("Must throw exception", Exception.class , () -> {
			clientService.validateFields(clientC);			
		});
		assertTrue("Must be invalid dni exception", exception.getMessage().equals("INVALID DNI")); 
	}
	
	@Test
	void testValidateFields_wrongAdress() {
		Client clientC = new Client(3, "Jairo Serrano",Gender.MALE,30,"1392388492",
				"","(+57) 3275392001","1234", true, new ArrayList()); 
		Exception exception = assertThrows("Must throw exception", Exception.class , () -> {
			clientService.validateFields(clientC);			
		});
		assertTrue("Must be invalid address exception", exception.getMessage().equals("INVALID ADDRESS")); 
	}
	
	@Test
	void testValidateFields_wrongPhone() {
		Client clientC = new Client(3, "Jairo Serrano",Gender.MALE,30,"1392388492",
				"Bogotá, calle 82 # 90 -53","(+57)","1234", true, new ArrayList()); 
		Exception exception = assertThrows("Must throw exception", Exception.class , () -> {
			clientService.validateFields(clientC);			
		});
		assertTrue("Must be invalid phone exception", exception.getMessage().equals("INVALID PHONE")); 
	}
	
	@Test
	void checkIfExists_existingClient() {
		try {
			clientService.checkIfExists(1);
			verify(clientDao, times(1)).findById(1); 
		} catch (Exception e) {
			fail("Must pass findById function"); 
		}
	}
	
	@Test
	void checkIfExists_unexistingClient() {
		when(clientDao.findById(5)).thenReturn(null); 
		assertThrows(Exception.class, () -> clientService.checkIfExists(5));
	}
	
	@Test
	void checkDontHaveAccounts_clientWithAccounts() {
		Exception exception = assertThrows("Must throw exception", Exception.class, () -> clientService.checkDontHaveAccounts(1)); 
		assertTrue("Exception must be has accounts", exception.getMessage().contains("HAS ACCOUNTS")); 
	}
	@Test
	void checkDontHaveAccounts_clientWithoutAccounts() {
		try {
			clientService.checkDontHaveAccounts(2);
			verify(accountDao, times(1)).getByClientId(2);
		} catch (Exception e) {
			fail("Must do not throw any exception"); 
		}
	}

}
