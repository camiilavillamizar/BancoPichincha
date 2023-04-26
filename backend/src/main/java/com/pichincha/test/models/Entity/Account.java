package com.pichincha.test.models.Entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pichincha.test.utils.enums.AccountType;

@Entity
@Table(name = "accounts")
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Long number; 
	
	@Enumerated(EnumType.STRING)
	private AccountType type; 
	private BigDecimal balance; 
	private boolean state; 
	
	@Transient
	private String clientName; 
	
	@Transient
	private int clientId;
	
	@JsonBackReference(value="client")
	@ManyToOne
	@JoinColumn(name = "clientId", nullable= false)
	private Client client;
	
	@JsonBackReference(value = "transactions")
	@OneToMany(mappedBy = "account", cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	private List<Transaction> transactions; 
	
	public Account() {}
	public Account(int id, Long number, AccountType type, BigDecimal balance, boolean state, Client client, List<Transaction> transactions) {
		super();
		this.id = id;
		this.number = number;
		this.type = type;
		this.balance = balance;
		this.state = state;
		this.client = client;
		this.transactions = transactions; 
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getClientName() {
		return client.getName();
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public int getClientId() {
		return client.getId();
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	} 
	
	
}
