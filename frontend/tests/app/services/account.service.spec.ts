import { TestBed } from '@angular/core/testing';

import { AccountService } from '../../../src/app/services/account.service';
import { HttpClientModule } from '@angular/common/http';
import { Client } from '../../../src/app/interfaces/client.interfaces';
import { Account } from '../../../src/app/interfaces/account.interfaces';

describe('AccountService', () => {

  let client: Client = {
    id: 1,
    name: 'JOSE LEMA',
    gender: 'MALE',
    age: 29,
    dni: '8851774',
    address: 'OTAVALO SN Y PRINCIPAL',
    phone: '098254785',
    password: '1234',
    state: true
  }
  let account : Account = {
    number: 12345,
    id: 0,
    balance: 100,
    type: 'AHORROS',
    state: false,
    clientName: '',
    clientId: 0, 
    client: client
  }

  beforeEach(async() => 
      TestBed.configureTestingModule({
        imports: [
          HttpClientModule
        ]
      })
  );

  it('should be created', () => {
    const service = TestBed.get(AccountService)
    expect(service).toBeTruthy();
  });

  test('should return accounts', (done) => {
    const service = TestBed.get(AccountService)
    service.getAll().subscribe( accounts => {

      expect(accounts.length).toBeGreaterThan(0); 
      expect(typeof accounts[0].id).toBe("number");   
      expect(typeof accounts[0].number).toBe("number");   
      expect(typeof accounts[0].type).toBe("string");   
      expect(typeof accounts[0].balance).toBe("number");   
      expect(typeof accounts[0].clientName).toBe("string");     
      done()
    })
  })

  test('should return saved account', (done) => {

    
    const service = TestBed.get(AccountService)
    service.post(account).subscribe( accountResp => {

      account = accountResp; 
      account.client = client; 
      expect(typeof accountResp.id).toBe("number");   
      expect(accountResp.number).toBe(12345);   
      expect(accountResp.type).toBe("AHORROS");   
      expect(accountResp.balance).toBe(100);   
      expect(accountResp.clientName).toBe("JOSE LEMA");    
      done()
    })
  })

  test('should return updated account', (done) => {

    
    const service = TestBed.get(AccountService)

    account.number = 12345678
    account.balance = 200
    service.put(account).subscribe( accountResp => {

      expect(typeof accountResp.id).toBe("number");   
      expect(accountResp.number).toBe(12345678);   
      expect(accountResp.type).toBe("AHORROS");   
      expect(accountResp.balance).toBe(200);   
      expect(accountResp.clientName).toBe("JOSE LEMA");    
      done()
    })
  })

  test('should delete an account', (done) => {

    
    const service = TestBed.get(AccountService)

    service.delete(account.id).subscribe( message => {
      expect("Account with ID "+ account.id + " deleted.").toBe(message.message)
      done()
    })
  })
  
});
