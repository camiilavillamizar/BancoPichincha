package com.pichincha.test.models.Dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pichincha.test.models.Entity.Transaction;

@Repository
public interface TransactionDao extends CrudRepository<Transaction, Long>{

	//GETS
	public List<Transaction> findAll(); 
	public Transaction findById(int id); 
	
	//POST/PUT
	public Transaction save(Transaction transaction); 
	
	//DELETE
	public void deleteById(int id);
	
	//QUERIES
	@Query(value = "SELECT balance FROM transactions\r\n"
			+ "where accountId = :accountId \r\n"
			+ "order by date desc\r\n"
			+ "limit 1;", nativeQuery = true)
	public BigDecimal getLastBalance(@Param("accountId") int accountId); 
	
	@Query(value = "SELECT * FROM transactions\r\n"
			+ "where accountId = :accountId ; ", nativeQuery = true)
	public List<Transaction> getByAccountId(@Param("accountId") int accountId); 
	
	@Query(value = "CALL getLastTransaction( :accountId ) ; ", nativeQuery = true)
	public Transaction getLastTransaction(@Param("accountId") int accountId); 
	
}
