import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { Transaction } from '../../../src/app/interfaces/transaction.interface';
import { TransactionService } from '../../../src/app/services/transaction.service';
import { Account } from 'src/app/interfaces/account.interfaces';
import { Client } from 'src/app/interfaces/client.interfaces';


describe('TransactionService', () => {

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
    number: 478758,
    id: 1,
    balance: 2000,
    type: 'AHORROS',
    state: false,
    clientName: '',
    clientId: 0, 
    client: client
  }
  let transaction: Transaction = {
    id: 0,
    date: new Date,
    type: 'CREDITO',
    amount: 100,
    balance: 0,
    accountId: 0,
    account: account
  }
  beforeEach(async() => 
  TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ]
    })
  );

  it('should be created', () => {
    const service: TransactionService = TestBed.get(TransactionService);
    expect(service).toBeTruthy();
  });

  test('should return transactions', (done) => {
    const service = TestBed.get(TransactionService)
    service.getAll().subscribe( transactions => {

      expect(transactions.length).toBeGreaterThan(0); 
      expect(typeof transactions[0].id).toBe("number");   
      expect(typeof transactions[0].date).toBe("string");   
      expect(typeof transactions[0].type).toBe("string");   
      expect(typeof transactions[0].balance).toBe("number");   
      expect(typeof transactions[0].amount).toBe("number");     
      done()
    })
  })

  test('should save transaction', (done) => {
    const service = TestBed.get(TransactionService)
    service.post(transaction).subscribe( transactionResp => {

      transaction = transactionResp
      transaction.account = account

      expect(typeof transactionResp.id).toBe("number");   
      expect(typeof transactionResp.date).toBe("string");   
      expect(transactionResp.type).toBe("CREDITO");   
      expect(typeof transactionResp.balance).toBe("number");   
      expect(transactionResp.amount).toBe(100);     
      done()
    })
  })

  test('should update transaction', (done) => {
    const service = TestBed.get(TransactionService)
    transaction.amount = 200; 
    service.put(transaction).subscribe( transactionResp => {
      expect(typeof transactionResp.id).toBe("number");   
      expect(typeof transactionResp.date).toBe("string");   
      expect(transactionResp.type).toBe("CREDITO");   
      expect(typeof transactionResp.balance).toBe("number");   
      expect(transactionResp.amount).toBe(200);     
      done()
    })
  })


  test('should delete a transaction', (done) => {

      
      const service = TestBed.get(TransactionService)

      service.delete(transaction.id).subscribe( message => {
        expect("Transaction with ID "+ transaction.id + " deleted.").toBe(message.message)
        done()
      })
    })
});
