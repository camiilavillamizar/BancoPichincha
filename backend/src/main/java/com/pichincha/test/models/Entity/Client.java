package com.pichincha.test.models.Entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.pichincha.test.utils.classes.Person;
import com.pichincha.test.utils.enums.Gender;

@Entity
@Table(name = "clients")
public class Client extends Person{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	
	@Column(nullable = false)
	private String password; 
	
	@Column(nullable = false)
	private boolean state;

	@OneToMany(mappedBy = "client", cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	private List<Account> accounts; 
	
	public Client() {}
	
	public Client(int id, String password, boolean state, List<Account> accounts) {
		super();
		this.id = id;
		this.password = password;
		this.state = state;
		this.accounts = accounts;
	}
	public Client(int id, String name, Gender gender, int age, String dni, String address, String phone, String password, boolean state, List<Account> accounts) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.dni = dni;
		this.address = address;
		this.phone = phone;
		this.password = password;
		this.state = state;
		this.accounts = accounts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}


	
	
}
