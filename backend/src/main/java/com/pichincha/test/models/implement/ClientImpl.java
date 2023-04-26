package com.pichincha.test.models.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pichincha.test.models.Dao.AccountDao;
import com.pichincha.test.models.Dao.ClientDao;
import com.pichincha.test.models.Entity.Client;
import com.pichincha.test.models.service.IAccount;
import com.pichincha.test.models.service.IClient;

@Service
@Transactional
public class ClientImpl implements IClient{

	
	@Autowired
	ClientDao clientDao; 
	
	@Autowired
	AccountDao accountDao; 
	
	//------------- CRUD----------------
	@Override
	public List<Client> getAll() {
		return clientDao.findAll(); 
	}

	@Override
	public Client getById(int id) {
		return clientDao.findById(id); 
	}

	@Override
	public Client save(Client client) {
		return clientDao.save(client); 
	}

	@Override
	public Client update(Client client) throws Exception {
		checkIfExists(client.getId()); 
		return clientDao.save(client);
	}

	@Override
	public void deleteById(int id) throws Exception {
		checkIfExists(id);
		checkDontHaveAccounts(id); 
		clientDao.deleteById(id);
	}

	//----------OTHER FUNCTIONS
	@Override
	public void checkIfExists(int id) throws Exception {
		
		if(getById(id) == null) 
			throw new Exception("CLIENT " + id + " does not exist"); 
	}

	@Override
	public void checkDontHaveAccounts(int id) throws Exception {
		if(accountDao.getByClientId(id).size() != 0) {
			throw new Exception("CLIENT "+ id + " HAS ACCOUNTS"); 
		}
	}

}
