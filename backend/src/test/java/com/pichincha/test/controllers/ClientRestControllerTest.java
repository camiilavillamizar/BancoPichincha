package com.pichincha.test.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.pichincha.test.models.Dao.AccountDao;
import com.pichincha.test.models.Dao.ClientDao;
import com.pichincha.test.models.Entity.Account;
import com.pichincha.test.models.Entity.Client;
import com.pichincha.test.models.implement.AccountImpl;
import com.pichincha.test.models.implement.ClientImpl;
import com.pichincha.test.models.service.IAccount;
import com.pichincha.test.models.service.IClient;
import com.pichincha.test.utils.enums.AccountType;
import com.pichincha.test.utils.enums.Gender;

@SpringBootTest
public class ClientRestControllerTest {

	@InjectMocks
	ClientImpl clientService; 
	
	@Mock
	ClientDao clientDao; 
	@Mock
	AccountDao accountDao; 
	
	List<Client> clients = new ArrayList(); 
	Client clientA = new Client(); 
	Client clientB = new Client(); 
	
	@BeforeEach()
	void setUp() {
		
		MockitoAnnotations.initMocks(this);
		

		clientA = new Client(1, 
									"Laura Perez",
									Gender.FEMALE,
									24,
									"10398274",
									"Bogotá, calle 151 # 20 -59",
									"(+57) 319203403",
									"secret", 
									true, 
									new ArrayList()); 
		clientB = new Client(2, 
									"Leonardo Castellanos",
									Gender.MALE,
									30,
									"1392388492",
									"Bogotá, calle 82 # 90 -53",
									"(+57) 3275392001",
									"secret", 
									true, 
									new ArrayList()); 
		clients.add(clientA);
		clients.add(clientB);
		
		when(clientDao.findAll()).thenReturn(clients); 
		when(clientDao.findById(1)).thenReturn(clientA); 
		when(clientDao.findById(2)).thenReturn(clientB); 
		when(clientDao.save(Mockito.any(Client.class))).thenReturn(clientA); 
		
		//ClientA's account to test delete throw exception
		List<Account> accounts = new ArrayList<>();
		Account accountA = new Account(1, new Long(102938), AccountType.AHORROS, 
						BigDecimal.ZERO, true, clientA, new ArrayList<>());
		accounts.add(accountA); 
		when(accountDao.getByClientId(1)).thenReturn(accounts);
		when(accountDao.getByClientId(2)).thenReturn(new ArrayList<>());
	}
	
	@Test
	void getAllTest() {
		List<Client> actualClients = clientService.getAll(); 
		assertEquals(actualClients, clients); 
	}
	@Test
	void getByIdTest() {
		Client client = clientService.getById(1); 
		assertEquals(client.getName(), "Laura Perez");
	}
	@Test
	void postTest() {
		
		Client clientC = new Client(3, 
				"Jairo Serrano",
				Gender.MALE,
				30,
				"1392388492",
				"Bogotá, calle 82 # 90 -53",
				"(+57) 3275392001",
				"secret", 
				true, 
				new ArrayList()); 
 
		Client savedClient = clientService.save(clientC); 
		 
		assertNotEquals(savedClient, clientC);
		assertEquals(savedClient, clientA);
		verify(clientDao, times(1)).save(clientC); 
	}
	@Test
	void updateTest() throws Exception {
		Client clientC = new Client(1, 
				"Laura Perez",
				Gender.FEMALE,
				24,
				"103982723",
				"Bogotá, calle 151 # 20 -59a",
				"(+57) 319203402",
				"secret2", 
				true, 
				new ArrayList()); 
	 
		Client savedClient = clientService.update(clientC); 
		assertNotEquals(savedClient, clientC);
		assertEquals(savedClient, clientA);
		verify(clientDao, times(1)).save(clientC); 
	}

	/**
	 * Can't delete client if has accounts
	 * @throws Exception
	 */
	@Test
	void deleteTest() throws Exception {
		assertThrows(Exception.class, () -> clientService.deleteById(clientA.getId()));
	}
	
	@Test
	void deleteTest2() throws Exception {
		clientService.deleteById(clientB.getId());
		verify(clientDao, times(1)).deleteById(clientB.getId()); 
	}
}
