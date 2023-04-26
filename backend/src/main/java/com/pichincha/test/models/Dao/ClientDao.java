package com.pichincha.test.models.Dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pichincha.test.models.Entity.Client;

@Repository
public interface ClientDao extends CrudRepository<Client, Long>{
	
	//GETS
	public List<Client> findAll(); 
	public Client findById(int id); 
	
	//POST/PUT
	public Client save(Client client); 
	
	//DELETE
	public void deleteById(int id); 
}
