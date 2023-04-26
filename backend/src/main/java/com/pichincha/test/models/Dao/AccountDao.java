package com.pichincha.test.models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pichincha.test.models.Entity.Account;
import com.pichincha.test.models.Entity.Client;

@Repository
public interface AccountDao extends CrudRepository<Account, Long>{
	
	//GETS
	public List<Account> findAll(); 
	public Account findById(int id); 
	public Account findByNumber(Long number); 
	public List<Account> findByClient(Client client); 
	//POST/PUT
	public Account save(Account account); 
	//DELETE
	public void deleteById(int id); 
	
	@Query(value = "SELECT * FROM accounts\r\n"
			+ "where clientId = :clientId ; ", nativeQuery = true)
	public List<Account> getByClientId(@Param("clientId") int id);

}
 